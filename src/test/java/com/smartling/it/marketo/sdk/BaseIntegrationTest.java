package com.smartling.it.marketo.sdk;

import com.smartling.marketo.sdk.domain.Asset;
import com.smartling.marketo.sdk.domain.folder.FolderId;
import com.smartling.marketo.sdk.MarketoClientManager;
import com.smartling.marketo.sdk.rest.MarketoRestClientManager;
import org.fest.assertions.core.Condition;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.fest.assertions.api.Assertions.assertThat;

public abstract class BaseIntegrationTest {

    protected static String identityEndpoint;
    protected static String restEndpoint;
    protected static String clientId;
    protected static String clientSecret;
    protected static String nonRoutableHostUrl;
    protected MarketoClientManager marketoClientManager;

    @BeforeClass
    public static void checkPreconditions() {
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
        marketoClientManager = MarketoRestClientManager.create(identityEndpoint, restEndpoint).withCredentials(clientId, clientSecret);
    }

    protected class AssetWithName extends Condition<Asset> {
        private final String name;

        public AssetWithName(String name) {
            this.name = name;
        }

        @Override
        public boolean matches(Asset value) {
            return value.getName().endsWith(name);
        }
    }

    protected class AssetWithNameAndFolderId extends AssetWithName {
        private final FolderId folderId;

        public AssetWithNameAndFolderId(String name, FolderId folderId) {
            super(name);
            this.folderId = folderId;
        }

        @Override
        public boolean matches(Asset value) {
            return super.matches(value) && new FolderId(value.getFolder()).equals(folderId);
        }
    }

    protected class AssetWithNameAndStatus extends AssetWithName {
        private final Asset.Status status;

        public AssetWithNameAndStatus(String name, Asset.Status status) {
            super(name);
            this.status = status;
        }

        @Override
        public boolean matches(Asset value) {
            return super.matches(value) && value.getStatus().equals(status);
        }
    }
}
