package me.spring.studygroup.account.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.application.AccountInfoFinderService;
import me.spring.studygroup.account.domain.Account;

@Component
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {
	private final AccountInfoFinderService accountInfoFinderService;

	@Override
	public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
		String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

		Account account = emailOrNickname.matches(emailRegex) ?
			accountInfoFinderService.findByEmail(emailOrNickname) : accountInfoFinderService.findByNickName(emailOrNickname);

		if (account == null)
			throw new UsernameNotFoundException(emailOrNickname);

		return new AccountPrincipal(account);
	}
}
