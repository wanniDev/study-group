package me.spring.studygroup.tag.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.domain.AccountRepository;
import me.spring.studygroup.account.domain.AccountTag;
import me.spring.studygroup.account.domain.AccountTagRepository;
import me.spring.studygroup.tag.domain.Tag;
import me.spring.studygroup.tag.domain.TagRepository;
import me.spring.studygroup.tag.presentation.form.TagForm;

@Service
@Transactional
@RequiredArgsConstructor
public class TagManagerService {
	private final TagRepository tagRepository;
	private final AccountRepository accountRepository;

	private final AccountTagRepository accountTagRepository;

	public Tag findByTitle(String title) {
		return tagRepository.findByTitle(title).orElseThrow();
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
		Tag tag = findByTitle(title);
		AccountTag accountTag = accountTagRepository.findAccountTagByAccountAndTag(account, tag);
		accountTagRepository.delete(accountTag);
	}
}
