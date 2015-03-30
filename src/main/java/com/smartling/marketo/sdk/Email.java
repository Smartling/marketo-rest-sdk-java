package com.smartling.marketo.sdk;

public class Email {
    private int id;
    private String name;
    private Folder folder = new Folder();
    private TextField subject = new TextField();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getFolder() {
        return folder;
    }

    public String getSubject() {
        return subject.getValue();
    }

    public static class TextField {
        private String value;

        public String getValue() {
            return value;
        }
    }
}
