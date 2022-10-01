package me.spring.studygroup.account.infrastructure.mail;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.spring.studygroup.account.application.EmailService;

@Slf4j
@Profile({"local", "test"})
@Component
public class ConsoleEmailService implements EmailService {

	@Override
	public void sendEmail(EmailMessage emailMessage) {
		log.info("sent email: {}", emailMessage.getMessage());
	}
}