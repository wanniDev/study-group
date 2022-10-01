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
import me.spring.studygroup.account.application.AccountRegisterService;
import me.spring.studygroup.account.application.SignUpFormValidator;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.presentation.form.SignUpForm;

@Controller
@RequiredArgsConstructor
public class AccountRegistrationController {
	private final SignUpFormValidator signUpFormValidator;
	private final AccountRegisterService accountRegisterService;
	private final AccountAuthService accountAuthService;

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
}
