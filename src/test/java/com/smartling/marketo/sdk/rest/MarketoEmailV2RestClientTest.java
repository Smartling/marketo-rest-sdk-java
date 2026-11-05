package com.smartling.marketo.sdk.rest;

import com.smartling.marketo.sdk.domain.v2.email.EmailV2;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Filter;
import com.smartling.marketo.sdk.domain.v2.email.EmailV2Page;
import com.smartling.marketo.sdk.rest.command.v2.email.FilterEmails;
import com.smartling.marketo.sdk.rest.command.v2.email.GetEmailById;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MarketoEmailV2RestClientTest
{
    private static final String WORKSPACE_ID = "test-workspace";
    private static final String EMAIL_ID = "42";
    private static final String LEADING_ZERO_ID = "007";

    @Mock
    private HttpCommandExecutor executor;

    @InjectMocks
    private MarketoEmailV2RestClient testedInstance;

    @Test
    public void shouldExecuteFilterCommandAndReturnPage() throws Exception
    {
        EmailV2Page page = new EmailV2Page();
        given(executor.execute(any(FilterEmails.class))).willReturn(page);

        // Filter emails by workspace
        EmailV2Page result = testedInstance.filterEmails(EmailV2Filter.builder(WORKSPACE_ID).build());

        ArgumentCaptor<FilterEmails> captor = ArgumentCaptor.forClass(FilterEmails.class);
        verify(executor).execute(captor.capture());
        assertThat(result).isSameAs(page);
        assertThat(captor.getValue().getPath()).isEqualTo("/asset/v2/email/filter");
        assertThat(captor.getValue().getParameters()).isEqualTo(Collections.singletonMap("workspaceId", WORKSPACE_ID));
    }

    @Test
    public void shouldReturnEmptyPageWhenResultIsNull() throws Exception
    {
        given(executor.execute(any(FilterEmails.class))).willReturn(null);

        EmailV2Page result = testedInstance.filterEmails(EmailV2Filter.builder(WORKSPACE_ID).build());

        assertThat(result).usingRecursiveComparison().isEqualTo(EmailV2Page.empty());
    }

    @Test
    public void shouldReturnFirstEmailForGet() throws Exception
    {
        EmailV2 first = new EmailV2();
        EmailV2 second = new EmailV2();
        given(executor.execute(any(GetEmailById.class))).willReturn(Arrays.asList(first, second));

        EmailV2 result = testedInstance.loadEmailById(EMAIL_ID);

        assertThat(result).isSameAs(first);
    }

    @Test
    public void shouldThrowObjectNotFoundForEmptyArray() throws Exception
    {
        given(executor.execute(any(GetEmailById.class))).willReturn(Collections.emptyList());

        assertThatThrownBy(() -> testedInstance.loadEmailById(EMAIL_ID))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Email[id = 42] not found");
    }

    @Test
    public void shouldThrowObjectNotFoundForNullResult() throws Exception
    {
        given(executor.execute(any(GetEmailById.class))).willReturn(null);

        assertThatThrownBy(() -> testedInstance.loadEmailById(EMAIL_ID))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Email[id = 42] not found");
    }

    @Test
    public void shouldRejectUnsafeIds()
    {
        for (String unsafeId : Arrays.asList(null, "", " ", "filter", "1/2", "{a}", " 6873", "6873\n", "+6873", "-1"))
        {
            assertThatThrownBy(() -> testedInstance.loadEmailById(unsafeId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("id must be a number, but was: " + unsafeId);
        }

        verifyNoInteractions(executor);
    }

    @Test
    public void shouldSendLeadingZeroIdAsGiven() throws Exception
    {
        given(executor.execute(any(GetEmailById.class))).willReturn(Collections.singletonList(new EmailV2()));

        testedInstance.loadEmailById(LEADING_ZERO_ID);

        ArgumentCaptor<GetEmailById> captor = ArgumentCaptor.forClass(GetEmailById.class);
        verify(executor).execute(captor.capture());
        assertThat(captor.getValue().getPath()).isEqualTo("/asset/v2/email/007");
    }
}
