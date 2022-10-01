package me.spring.studygroup.account.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
	boolean existsByEmail(String email);
	boolean existsByNickname(String nickname);
}
