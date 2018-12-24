package ru.otus.Examiner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.Config.ApplicationProps;
import ru.otus.Config.MessageService;
import ru.otus.Domain.Result;
import ru.otus.Domain.ResultDao;
import ru.otus.OutService.OutService;
import ru.otus.ResourceService.ResourceService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Examiner {

    private ResultDao storage;

    private ResourceService resourceService;
    private OutService outService;
    private MessageService messageService;

    public Examiner(ResourceService resourceService, OutService outService) {
        this.resourceService = resourceService;
        this.outService = outService;
    }

    public void start() {
        Result result = new Result();
        result.setResultString(messageService.getResultString());
        Map<String, List<String>> questions = resourceService.readQuestions();
        Map<String, String> questionsAnswers = prepareQuestions(questions);
        String name = outService.getName();
        Map<String, String> answers = outService.getAnswers(questions);
        result.setName(name);
        result.setCorrect(getCorrectAnswers(questionsAnswers, answers));
        result.setTotal(answers.size());
        storage.save(result);
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setStorage(ResultDao storage) {
        this.storage = storage;
    }

    private int getCorrectAnswers(Map<String, String> questionAnswer, Map<String, String> answers) {
        int score = 0;
        for(Map.Entry entry: answers.entrySet()) {
            if (StringUtils.equals(questionAnswer.get(entry.getKey()), (String)entry.getValue())) {
                score++;
            }
        }
        return score;
    }

    private Map<String, String> prepareQuestions(Map<String, List<String>> questions) {
        Map<String, String> questionAnswer = new HashMap<>();
        questions.forEach((k, v) -> {
            questionAnswer.put(k, v.get(0));
            Collections.shuffle(v);
        });
        return questionAnswer;
    }
}
