package me.spring.studygroup.account.infrastructure.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.application.EmailService;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.mail.EmailMessage;
import me.spring.studygroup.config.AppProperties;

@Component
@RequiredArgsConstructor
public class ViewTemplateContextService {
	private final TemplateEngine templateEngine;
	private final AppProperties appProperties;
	private final EmailService emailService;

	public void sendSignUpConfirmEmail(Account newAccount) {

		Context context = new Context();
		context.setVariable("link", "/check-email-token?token=" + newAccount.getEmailCheckToken() +
			"&email=" + newAccount.getEmail());
		context.setVariable("nickname", newAccount.getNickname());
		context.setVariable("linkName", "이메일 인증하기");
		context.setVariable("message", "스터디올래 서비스를 사용하려면 링크를 클릭하세요.");
		context.setVariable("host", appProperties.getHost());
		String message = templateEngine.process("mail/simple-link", context);

		EmailMessage emailMessage = EmailMessage.builder()
			.to(newAccount.getEmail())
			.subject("스터디올래, 회원 가입 인증")
			.message(message)
			.build();

		emailService.sendEmail(emailMessage);
	}
}
