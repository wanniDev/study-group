package me.spring.studygroup.common.annotation;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.application.AccountRegisterService;
import me.spring.studygroup.account.presentation.form.SignUpForm;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {
	private final AccountRegisterService accountRegisterService;
	private final UserDetailsService userDetailsService;

	@Override
	public SecurityContext createSecurityContext(WithAccount withAccount) {
		String nickname = withAccount.value();

		SignUpForm signUpForm = new SignUpForm();
		signUpForm.setNickname(nickname);
		signUpForm.setEmail(nickname + "@email.com");
		signUpForm.setPassword("12345678");
		accountRegisterService.processNewAccount(signUpForm);

		UserDetails principal = userDetailsService.loadUserByUsername(nickname);
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
	}
}
