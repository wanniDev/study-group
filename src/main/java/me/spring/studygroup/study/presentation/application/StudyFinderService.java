package me.spring.studygroup.study.presentation.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.presentation.form.StudyRepository;

@Service
@RequiredArgsConstructor
public class StudyFinderService {
	private final StudyRepository studyRepository;

	public Study findByPath(String path) {
		return studyRepository.findByPath(path).orElseThrow();
	}
}
