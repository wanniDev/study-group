package me.spring.studygroup.study.application;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.presentation.form.StudyDescriptionForm;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyEditService {
	private final ModelMapper modelMapper;
	public void saveStudyDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
		modelMapper.map(studyDescriptionForm, study);
		// TODO 알림 기능 구현시 활성화
		// eventPublisher.publishEvent(new StudyUpdateEvent(study, "스터디 소개를 수정했습니다."));
	}
}
