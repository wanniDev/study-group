package me.spring.studygroup.study.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.presentation.form.StudyRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyRegisterService {
	private final StudyRepository studyRepository;

	public Study createNewStudy(Study study, Account account) {
		Study newStudy = studyRepository.save(study);
		newStudy.addManager(account);
		return newStudy;
	}
}
