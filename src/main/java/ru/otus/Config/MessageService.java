package ru.otus.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	private MessageSource messageSource;
	private ApplicationProps applicationProps;

	public String getAskName() {
		return messageSource.getMessage("ask.name", null, applicationProps.getLocale());
	}

	public String getAskQuestions() {
		return messageSource.getMessage("ask.questions", null, applicationProps.getLocale());
	}

	public String getResultString() {
		return messageSource.getMessage("result.string", null, applicationProps.getLocale());
	}

	@Autowired
	public void setMessageConfig(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Autowired
	public void setApplicationProps(ApplicationProps applicationProps) {
		this.applicationProps = applicationProps;
	}
}
