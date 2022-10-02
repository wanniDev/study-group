package me.spring.studygroup.account.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.domain.AccountRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountInfoFinderService {
	private final AccountRepository accountRepository;

	public boolean existsByEmail(String email) {
		return accountRepository.existsByEmail(email);
	}

	public boolean existsByNickname(String nickname) {
		return accountRepository.existsByNickname(nickname);
	}

	public Account findByEmail(String email) {
		return accountRepository.findByEmail(email).orElse(null);
	}
}
