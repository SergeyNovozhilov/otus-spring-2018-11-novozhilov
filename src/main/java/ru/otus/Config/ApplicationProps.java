package ru.otus.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class ApplicationProps {
    private String result;
    private String askName;
    private String askQuestions;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAskName() {
        return askName;
    }

    public void setAskName(String askName) {
        this.askName = askName;
    }

    public String getAskQuestions() {
        return askQuestions;
    }

    public void setAskQuestions(String askQuestions) {
        this.askQuestions = askQuestions;
    }
}
