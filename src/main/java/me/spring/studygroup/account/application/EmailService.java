package me.spring.studygroup.account.application;

import me.spring.studygroup.account.infrastructure.mail.EmailMessage;

public interface EmailService {
	void sendEmail(EmailMessage emailMessage);
}
