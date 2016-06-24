package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class GetFormById extends BaseGetCommand<Form> {
    private final int id;

    public GetFormById(int id) {
        super(Form.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + id + ".json";
    }
}
