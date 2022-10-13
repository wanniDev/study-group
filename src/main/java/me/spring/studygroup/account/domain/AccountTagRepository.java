package me.spring.studygroup.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import me.spring.studygroup.tag.domain.Tag;

public interface AccountTagRepository extends JpaRepository<AccountTag, Long> {
	AccountTag findAccountTagByAccountAndTag(Account account, Tag tag);
}
