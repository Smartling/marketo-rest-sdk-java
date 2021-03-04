package com.smartling.it.marketo.sdk.testutils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

import java.net.URL;
import java.util.List;

public final class TestUtils
{

    private static final ObjectMapper MAPPER = mapper();

    private TestUtils()
    {

    }

    public static <T> List<T> fromJsonListByClassLoader(String classLoaderResource, Class<T> type)
    {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(classLoaderResource);
        JavaType collectionType = MAPPER.getTypeFactory().constructCollectionType(List.class, type);

        try
        {
            return MAPPER.readValue(resource, collectionType);
        } catch (Exception ex)
        {
            throw Throwables.propagate(ex);
        }
    }

    private static ObjectMapper mapper() {
        ObjectMapper result = new ObjectMapper();
        result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return result;
    }

}
