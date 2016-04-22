package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateFormCommand implements Command<Void> {

    private final Form form;

    public UpdateFormCommand(Form form) {
        this.form = form;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + form.getId() + ".json";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public Type getResultType() {
        return Void.TYPE;
    }

    @Override
    public Map<String, Object> getParameters() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
                .put("description", form.getDescription())
                .put("formLanguage", form.getFormLanguage())
                .put("formLocale", form.getFormLocale());
//                .put("knownVisitor", form.getKnownVisitor());
        return builder.build();
    }
}
