package me.spring.studygroup.account.application;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.domain.AccountRepository;
import me.spring.studygroup.account.presentation.form.ProfileForm;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountProfileSettingService {
	private final AccountRepository accountRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void updateProfile(Account account, ProfileForm profileForm) {
		modelMapper.map(profileForm, account);
		accountRepository.save(account);
	}

	@Transactional
	public void updatePassword(Account account, String newPassword) {
		account.setPassword(passwordEncoder.encode(newPassword));
		accountRepository.save(account);
	}
}
