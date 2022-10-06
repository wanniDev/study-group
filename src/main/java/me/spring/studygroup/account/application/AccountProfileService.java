package me.spring.studygroup.account.application;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.domain.AccountRepository;
import me.spring.studygroup.account.presentation.form.ProfileForm;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountProfileService {
	private final AccountRepository accountRepository;
	private final ModelMapper modelMapper;

	@Transactional
	public void updateProfile(Account account, ProfileForm profileForm) {
		modelMapper.map(profileForm, account);
		accountRepository.save(account);
	}
}
