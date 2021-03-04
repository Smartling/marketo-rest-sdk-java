package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset;
import com.smartling.marketo.sdk.domain.BaseEntity;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.MarketoClientManager;
import com.smartling.marketo.sdk.rest.MarketoRestClientManager;
import com.smartling.marketo.sdk.rest.RetryPolicy;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseIntegrationTest {

    protected static String identityEndpoint;
    protected static String restEndpoint;
    protected static String clientId;
    protected static String clientSecret;
    protected static String nonRoutableHostUrl;
    protected MarketoClientManager marketoClientManager;

    @BeforeClass
    public static void checkPreconditions() throws Exception {
        // Avoid hitting Marketo rate limits
        Thread.sleep(500L);

        identityEndpoint = System.getProperty("marketo.identity");
        restEndpoint = System.getProperty("marketo.rest");
        clientId = System.getProperty("marketo.clientId");
        clientSecret = System.getProperty("marketo.clientSecret");

        nonRoutableHostUrl = "http://192.0.2.0:81";

        assertThat(identityEndpoint).overridingErrorMessage("Identity endpoint is missing").isNotEmpty();
        assertThat(restEndpoint).overridingErrorMessage("REST endpoint is missing").isNotEmpty();
        assertThat(clientId).overridingErrorMessage("Client ID is missing").isNotEmpty();
        assertThat(clientSecret).overridingErrorMessage("Client Secret is missing").isNotEmpty();
    }

    @Before
    public void setUpBase() throws Exception {
        marketoClientManager = MarketoRestClientManager.create(identityEndpoint, restEndpoint)
                .withConnectionTimeout(2000)
                .withSocketReadTimeout(20000)
                .withRetryPolicy(RetryPolicy.NONE)
                .withCredentials(clientId, clientSecret);
    }

    protected class EntityWithName extends Condition<BaseEntity> {
        private final String name;

        EntityWithName(String name) {
            this.name = name;
        }

        @Override
        public boolean matches(BaseEntity value) {
            return value.getName().toUpperCase().endsWith(name.toUpperCase());
        }
    }

    protected class EntityWithNameAndFolderId extends EntityWithName {
        private final FolderId folderId;

        EntityWithNameAndFolderId(String name, FolderId folderId) {
            super(name);
            this.folderId = folderId;
        }

        @Override
        public boolean matches(BaseEntity value) {
            return super.matches(value) && new FolderId(value.getFolder()).equals(folderId);
        }
    }

    protected class AssetWithNameAndStatus  extends Condition<Asset> {
        private final Asset.Status status;
        private final String name;

        AssetWithNameAndStatus(String name, Asset.Status status) {
            this.status = status;
            this.name = name;
        }

        @Override
        public boolean matches(Asset value) {
            return value.getName().toUpperCase().endsWith(name.toUpperCase()) && value.getStatus().equals(status);
        }
    }
}
