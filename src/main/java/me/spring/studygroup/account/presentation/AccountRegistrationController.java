package me.spring.studygroup.account.presentation;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.application.AccountAuthService;
import me.spring.studygroup.account.application.AccountInfoFinderService;
import me.spring.studygroup.account.application.AccountRegisterService;
import me.spring.studygroup.account.presentation.validator.SignUpFormValidator;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;
import me.spring.studygroup.account.infrastructure.thymeleaf.ViewTemplateContextService;
import me.spring.studygroup.account.presentation.form.SignUpForm;

@Controller
@RequiredArgsConstructor
public class AccountRegistrationController {
	private final SignUpFormValidator signUpFormValidator;
	private final AccountRegisterService accountRegisterService;
	private final AccountAuthService accountAuthService;
	private final AccountInfoFinderService accountInfoFinderService;
	private final ViewTemplateContextService viewTemplateContextService;

	@InitBinder("signUpForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signUpFormValidator);
	}

	@GetMapping("sign-up")
	public String joinForm(Model model) {
		model.addAttribute("signUpForm", new SignUpForm());
		return "account/sign-up";
	}

	@PostMapping("/sign-up")
	public String joinSubmit(@Valid SignUpForm signUpForm, Errors errors) {
		if (errors.hasErrors())
			return "account/sign-up";

		Account account = accountRegisterService.processNewAccount(signUpForm);
		accountAuthService.login(account);
		return "redirect:/";
	}

	@GetMapping("/check-email")
	public String checkEmail(@AuthAccount Account account, Model model) {
		model.addAttribute("email", account.getEmail());
		return "account/check-email";
	}

	@GetMapping("/resend-confirm-email")
	public String resendConfirmEmail(@AuthAccount Account account, Model model) {
		if (!account.canSendConfirmEmail()) {
			model.addAttribute("error", "인증 이메일은 1시간에 한번만 전송할 수 있습니다.");
			model.addAttribute("email", account.getEmail());
			return "account/check-email";
		}

		viewTemplateContextService.sendSignUpConfirmEmail(account);
		return "redirect:/";
	}


	@GetMapping("/check-email-token")
	public String checkEmailToken(String token, String email, Model model) {
		Account account = accountInfoFinderService.findByEmail(email);
		String viewPath = "account/checked-email";

		if (account == null) {
			model.addAttribute("error", "wrong.email");
			return viewPath;
		}

		if (!account.isValidToken(token)) {
			model.addAttribute("error", "wrong.token");
			return viewPath;
		}

		accountRegisterService.completeSignUp(account);
		model.addAttribute("nickname", account.getNickname());

		return viewPath;
	}
}
