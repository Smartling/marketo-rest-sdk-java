package com.smartling.marketo.sdk.domain.emailtemplate;

import com.smartling.marketo.sdk.domain.Asset;

public class EmailTemplate extends Asset {
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static class TextField {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return "TextField{" + "value='" + value + '\'' + '}';
        }
    }

    @Override
    public String toString()
    {
        return "EmailTemplate{" + "version=" + version + "} " + super.toString();
    }
}
