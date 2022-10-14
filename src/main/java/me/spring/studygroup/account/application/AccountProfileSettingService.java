package me.spring.studygroup.account.application;

import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.domain.AccountRepository;
import me.spring.studygroup.account.domain.AccountTag;
import me.spring.studygroup.account.presentation.form.NotificationForm;
import me.spring.studygroup.account.presentation.form.ProfileForm;
import me.spring.studygroup.tag.domain.Tag;
import me.spring.studygroup.tag.domain.TagRepository;
import me.spring.studygroup.tag.presentation.form.TagForm;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountProfileSettingService {

	private final AccountAuthService accountAuthService;
	private final AccountRepository accountRepository;
	private final TagRepository tagRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	public void updateProfile(Account account, ProfileForm profileForm) {
		modelMapper.map(profileForm, account);
		accountRepository.save(account);
	}

	public void updatePassword(Account account, String newPassword) {
		account.setPassword(passwordEncoder.encode(newPassword));
		accountRepository.save(account);
	}

	public void updateNickname(Account account, String nickname) {
		account.setNickname(nickname);
		accountRepository.save(account);
		accountAuthService.login(account);
	}

	public void updateNotifications(Account account, NotificationForm notificationForm) {
		modelMapper.map(notificationForm, account);
		accountRepository.save(account);
	}

	public Tag findOrCreateNew(String tagTitle) {
		return tagRepository.findByTitle(tagTitle)
			.orElseGet(() -> tagRepository.save(Tag.builder().title(tagTitle).build()));
	}

	public void addTag(Account account, Tag tag) {
		accountRepository.findById(account.getId())
			.ifPresent(a -> a.getAccountTags().add(AccountTag.createNewAccountTag(account, tag)));
	}

	public void removeTag(Account account, TagForm tagForm) {
		String title = tagForm.getTagTitle();
		Tag tag = tagRepository.findByTitle(title).orElseThrow();
		accountRepository.findById(account.getId())
			.ifPresent(a -> a.getAccountTags().stream().map(AccountTag::getTag)
				.collect(Collectors.toList()).remove(tag));
	}
}
