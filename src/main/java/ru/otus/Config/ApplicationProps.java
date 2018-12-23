package ru.otus.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

@ConfigurationProperties
public class ApplicationProps {
    private final String FILE_NAME = "questions_%s.csv";
    private final String DEFAULT_LOCALE = "en_US";
    private String locale = DEFAULT_LOCALE;
    private Map<String, String> languages = new HashMap<>();


    public Locale getLocale() {
        return new Locale(locale);
    }

    public String getFileName() {
        return String.format(FILE_NAME, locale.split("_")[0].toLowerCase());
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }


    public void setLanguages(String languages) {
        List<String> langs = Arrays.asList(languages.split(","));
        langs.stream().forEach(l -> {
            l = l.trim();
            this.languages.put(l.split("_")[0].toLowerCase(), l);
        });
    }

    public void changeLanguage(String lang) {
        this.locale = languages.get(lang) == null ? DEFAULT_LOCALE : languages.get(lang);
    }

    public List<String> getLanguages() {
        return new ArrayList<>(languages.keySet());
    }
}
