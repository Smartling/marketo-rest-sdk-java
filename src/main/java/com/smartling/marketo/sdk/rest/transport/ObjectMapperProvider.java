package com.smartling.marketo.sdk.rest.transport;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.text.SimpleDateFormat;

class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    @Override
    public ObjectMapper getContext(Class<?> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'Z"));

        objectMapper.registerModule(new SimpleModule()
                .addDeserializer(Void.TYPE, new VoidDeserializer())
                .addDeserializer(Enum.class, CaseInsensitiveEnumDeserializer.INSTANCE));

        return objectMapper;
    }

    private static final class VoidDeserializer extends JsonDeserializer<Void> {
        @Override
        public Void deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
            return null;
        }
    }

    private static final class CaseInsensitiveEnumDeserializer extends JsonDeserializer<Enum<?>> implements ContextualDeserializer {
        private static final CaseInsensitiveEnumDeserializer INSTANCE = new CaseInsensitiveEnumDeserializer(null);

        private final Class<Enum> type;

        public CaseInsensitiveEnumDeserializer(Class<Enum> type) {
            this.type = type;
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty)
                throws JsonMappingException {

            @SuppressWarnings("unchecked")
            Class<Enum> enumClass = (Class<Enum>) beanProperty.getType().getRawClass();

            return new CaseInsensitiveEnumDeserializer(enumClass);
        }

        @Override
        public Enum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String text = jsonParser.getText();
            return text == null ? null : Enum.valueOf(type, text.toUpperCase());
        }
    }
}
