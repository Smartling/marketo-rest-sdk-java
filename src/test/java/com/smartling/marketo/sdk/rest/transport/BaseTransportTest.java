package com.smartling.marketo.sdk.rest.transport;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy;
import com.github.tomakehurst.wiremock.client.ValueMatchingStrategy;
import com.smartling.marketo.sdk.MarketoApiException;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

public abstract class BaseTransportTest {
    static final int PORT = 10000 + new Random().nextInt(9999);

    static final String BASE_URL = "http://localhost:" + PORT;
    static final String IDENTITY_URL = BASE_URL + "/identity";
    static final String REST_URL = BASE_URL + "/rest";

    static final String CLIENT_ID = "the_client_id";
    static final String CLIENT_SECRET = "a_secret_key";

    static Matcher<MarketoApiException> exceptionWithCode(final String code) {
        return new TypeSafeMatcher<MarketoApiException>() {
            @Override
            protected boolean matchesSafely(MarketoApiException item) {
                return code.equalsIgnoreCase(item.getErrorCode());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("exception with code ").appendValue(code);
            }
        };
    }

    ValueMatchingStrategy withFormParam(String key, String value) {
        return containing(key + "=" + value);
    }

    static UrlMatchingStrategy urlStartingWith(String path) {
        return urlMatching(path + ".*");
    }

    static UrlMatchingStrategy path(String path) {
        return urlMatching(path + "(\\?.+)?");
    }

    static ResponseDefinitionBuilder aJsonResponse(String json) {
        return aResponse().withHeader("Content-Type", "application/json").withBody(json);
    }

    ClientConnectionData getClientConnectionData(Client wsClient, String clientId) {
        return new ClientConnectionData(wsClient, IDENTITY_URL, clientId, CLIENT_SECRET);
    }

    ClientConnectionData getClientConnectionData(String clientId) {
        Client client = ClientBuilder.newClient().register(JacksonFeature.class).register(ObjectMapperProvider.class);
        return getClientConnectionData(client, clientId);
    }

}
