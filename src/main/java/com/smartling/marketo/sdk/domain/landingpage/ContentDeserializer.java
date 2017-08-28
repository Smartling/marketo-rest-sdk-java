package com.smartling.marketo.sdk.domain.landingpage;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ContentDeserializer extends StdDeserializer<String>
{

    public ContentDeserializer()
    {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        String result = p.getValueAsString();
        p.skipChildren();
        return result;
    }

}
