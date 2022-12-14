package me.spring.studygroup.account.application;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.domain.AccountRepository;
import me.spring.studygroup.account.infrastructure.thymeleaf.ViewTemplateContextService;
import me.spring.studygroup.account.presentation.form.SignUpForm;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountRegisterService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final ViewTemplateContextService templateContextService;
	private final AccountAuthService accountAuthService;

	@Transactional
	public Account processNewAccount(SignUpForm signUpForm) {
		signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
		Account account = modelMapper.map(signUpForm, Account.class);
		account.generateEmailCheckToken();

		templateContextService.sendSignUpConfirmEmail(account);

		return accountRepository.save(account);
	}

	@Transactional
	public void completeSignUp(Account account) {
		account.completeSignUp();
		accountAuthService.login(account);
	}
}
