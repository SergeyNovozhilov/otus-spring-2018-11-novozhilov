package ru.otus.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class ApplicationProps {
    private String locale;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
