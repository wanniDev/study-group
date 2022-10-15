package me.spring.studygroup.study.application;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.presentation.form.StudyRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyFinderService {
	private final StudyRepository studyRepository;

	public Study findByPath(String path) {
		return studyRepository.findByPath(path).orElseThrow();
	}

	public Study findByPath(String path, Account account) {
		Study study = findByPath(path);
		if (!study.isManagedBy(account)) {
			throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
		}
		return study;
	}

	public Study findByZoneAndPath(String path) {
		return studyRepository.findStudyWithZonesByPath(path).orElseThrow();
	}
}
