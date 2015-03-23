package com.smartling.marketo.sdk;

public class Email {
    private int id;
    private String name;
    private Folder folder = new Folder();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Folder getFolder() {
        return folder;
    }
}
