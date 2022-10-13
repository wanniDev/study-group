package me.spring.studygroup.account.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.application.AccountAuthService;
import me.spring.studygroup.account.application.AccountInfoFinderService;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.thymeleaf.ViewTemplateContextService;

@Controller
@RequiredArgsConstructor
public class AccountAuthController {

	private final AccountInfoFinderService accountInfoFinderService;
	private final AccountAuthService accountAuthService;
	private final ViewTemplateContextService viewTemplateContextService;

	@GetMapping("/email-login")
	public String emailLoginForm() {
		return "account/email-login";
	}

	@PostMapping("/email-login")
	public String sendEmailLoginLink(String email, Model model, RedirectAttributes attributes) {
		Account account = accountInfoFinderService.findByEmail(email);
		if (account == null) {
			model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
			return "account/email-login";
		}

		if (!account.canSendConfirmEmail()) {
			model.addAttribute("error", "이메일 로그인은 1시간 뒤에 사용할 수 있습니다.");
			return "account/email-login";
		}

		viewTemplateContextService.sendSignUpConfirmEmail(account);
		attributes.addFlashAttribute("message", "이메일 인증 메일을 발송했습니다.");
		return "redirect:/email-login";
	}

	@GetMapping("/login-by-email")
	public String loginByEmail(String token, String email, Model model) {
		Account account = accountInfoFinderService.findByEmail(email);
		String view = "account/logged-in-by-email";
		if (account == null || !account.isValidToken(token)) {
			model.addAttribute("error", "로그인할 수 없습니다.");
			return view;
		}

		accountAuthService.login(account);
		return view;
	}
}
