package me.spring.studygroup.account.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountInfoFinderService {
	private final AccountRepository accountRepository;

	public boolean existsByEmail(String email) {
		return accountRepository.existsByEmail(email);
	}

	public boolean existsByNickname(String nickname) {
		return accountRepository.existsByNickname(nickname);
	}
}
