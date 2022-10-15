package me.spring.studygroup.study.application;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.presentation.form.StudyDescriptionForm;
import me.spring.studygroup.tag.domain.Tag;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyEditService {
	private final ModelMapper modelMapper;
	private final StudyFinderService studyFinderService;

	public void saveDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
		modelMapper.map(studyDescriptionForm, study);
		// TODO 알림 기능 구현시 활성화
		// eventPublisher.publishEvent(new StudyUpdateEvent(study, "스터디 소개를 수정했습니다."));
	}

	public void saveImage(Study study, String image) {
		study.setImage(image);
	}

	public Study saveBanner(String path, Account account, String image) {
		Study study = studyFinderService.findByPath(path, account);
		saveImage(study, image);
		return study;
	}

	public Study enableStudyBanner(String path, Account account) {
		Study study = studyFinderService.findByPath(path, account);
		study.setUseBanner(true);
		return study;
	}

	public Study disableStudyBanner(String path, Account account) {
		Study study = studyFinderService.findByPath(path, account);
		study.setUseBanner(true);
		return study;
	}

	public void addTag(Study study, Tag tag) {
		study.getTags().add(tag);
	}

	public void removeTag(Study study, Tag tag) {

	}
}
