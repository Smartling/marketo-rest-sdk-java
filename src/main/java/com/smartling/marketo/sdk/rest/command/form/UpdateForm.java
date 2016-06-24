package com.smartling.marketo.sdk.rest.command.form;

import com.google.common.collect.ImmutableMap;
import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.Command;

import java.lang.reflect.Type;
import java.util.Map;

public class UpdateForm implements Command<Form> {

    private final Form form;

    public UpdateForm(Form form) {
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
                .put("language", form.getLanguage())
                .put("locale", form.getLocale());

        if(form.getKnownVisitor().getType() == Form.KnownVisitorType.CUSTOM) {
            builder.put("knownVisitor", form.getKnownVisitor());
        }

        return builder.build();
    }
}
