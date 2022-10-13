package me.spring.studygroup.account.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.domain.AccountRepository;
import me.spring.studygroup.account.domain.AccountTag;
import me.spring.studygroup.tag.domain.Tag;
import me.spring.studygroup.tag.domain.TagRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountInfoFinderService {
	private final AccountRepository accountRepository;
	private final TagRepository tagRepository;

	public boolean existsByEmail(String email) {
		return accountRepository.existsByEmail(email);
	}

	public boolean existsByNickname(String nickname) {
		return accountRepository.existsByNickname(nickname);
	}

	public Account findByEmail(String email) {
		return accountRepository.findByEmail(email).orElse(null);
	}

	public Account findByNickName(String nickname) {
		return accountRepository.findByNickname(nickname).orElse(null);
	}

	public Set<Tag> findTagsBy(Account account) {
		return accountRepository.findById(account.getId()).orElseThrow().getAccountTags()
			.stream().map(AccountTag::getTag).collect(Collectors.toSet());
	}

	public List<String> findTagsTitle() {
		return tagRepository.findTagsTitle();
	}
}
