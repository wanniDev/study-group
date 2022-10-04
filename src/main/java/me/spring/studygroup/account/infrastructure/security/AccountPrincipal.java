package me.spring.studygroup.account.infrastructure.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import me.spring.studygroup.account.domain.Account;

@Getter
public class AccountPrincipal extends User {
	private final Account account;
	public AccountPrincipal(Account account) {
		super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
		this.account = account;
	}
}
