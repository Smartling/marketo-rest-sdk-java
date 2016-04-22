package com.smartling.marketo.sdk.rest.command.form;

import com.smartling.marketo.sdk.domain.form.Form;
import com.smartling.marketo.sdk.rest.command.BaseGetCommand;

public class LoadFormByIdCommand extends BaseGetCommand<Form> {
    private final int id;

    public LoadFormByIdCommand(int id) {
        super(Form.class);
        this.id = id;
    }

    @Override
    public String getPath() {
        return "/asset/v1/form/" + id + ".json";
    }
}
