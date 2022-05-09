package com.smartling.marketo.sdk.domain.landingpage;

import com.smartling.it.marketo.sdk.testutils.TestUtils;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LandingPageContentTest
{

    @Test
    public void ordinaryContent() {
        List<LandingPageContentItem> items = TestUtils.fromJsonListByClassLoader(
                "json/ordinary_content.json", LandingPageContentItem.class
        );

        assertThat(items).hasSize(6);
        assertThat(items.get(5)).isInstanceOf(LandingPageTextContentItem.class);
    }

    @Test
    public void emptyFormContent() {
        List<LandingPageContentItem> items = TestUtils.fromJsonListByClassLoader(
                "json/empty_form_content.json", LandingPageContentItem.class
        );

        assertThat(items).hasSize(7);
        assertThat(items.get(6)).isInstanceOf(LandingPageFormContentItem.class);
        assertThat(((LandingPageFormContentItem) items.get(6)).getContent()).isNull();
    }

    @Test
    public void dynamicContent() {
        List<LandingPageContentItem> items = TestUtils.fromJsonListByClassLoader(
                "json/dynamic_content.json", LandingPageContentItem.class
        );

        assertThat(items).hasSize(3);
        assertThat(items.get(0)).isInstanceOf(LandingPageDynamicContentItem.class);
        assertThat(items.get(1)).isInstanceOf(LandingPageDynamicContentItem.class);
        assertThat(items.get(2)).isInstanceOf(LandingPageTextContentItem.class);
    }
}
