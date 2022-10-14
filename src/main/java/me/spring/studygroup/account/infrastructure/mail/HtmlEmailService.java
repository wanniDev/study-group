package me.spring.studygroup.account.infrastructure.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.spring.studygroup.account.application.EmailService;

@Profile("dev")
@Slf4j
@Component
@RequiredArgsConstructor
public class HtmlEmailService implements EmailService {
	private final JavaMailSender javaMailSender;

	@Override
	public void sendEmail(EmailMessage emailMessage) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(emailMessage.getTo());
			mimeMessageHelper.setSubject(emailMessage.getSubject());
			mimeMessageHelper.setText(emailMessage.getMessage(), true);
			javaMailSender.send(mimeMessage);
			log.info("sent email: {}", emailMessage.getMessage());
		} catch (MessagingException e) {
			log.error("failed to send email", e);
			throw new RuntimeException(e);
		}
	}
}
