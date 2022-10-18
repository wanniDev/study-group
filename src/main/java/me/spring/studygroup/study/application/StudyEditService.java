package me.spring.studygroup.study.application;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.presentation.form.StudyDescriptionForm;
import me.spring.studygroup.study.presentation.form.StudyRepository;
import me.spring.studygroup.tag.domain.Tag;
import me.spring.studygroup.zone.application.ZoneInfoFinderService;
import me.spring.studygroup.zone.domain.Zone;
import me.spring.studygroup.zone.presentation.form.ZoneForm;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyEditService {
	private final ModelMapper modelMapper;
	private final StudyRepository studyRepository;
	private final StudyFinderService studyFinderService;
	private final ZoneInfoFinderService zoneInfoFinderService;

	public void saveDescription(Study study, StudyDescriptionForm studyDescriptionForm) {
		modelMapper.map(studyDescriptionForm, study);
		// TODO 알림 기능 구현시 활성화
		// eventPublisher.publishEvent(new StudyUpdateEvent(study, "스터디 소개를 수정했습니다."));
	}

	public void saveImage(Study study, String image) {
		study.setImage(image);
	}

	public Study saveBanner(String path, Account account, String image) {
		Study study = studyFinderService.findByPathForManager(path, account);
		saveImage(study, image);
		return study;
	}

	public Study enableStudyBanner(String path, Account account) {
		Study study = studyFinderService.findByPathForManager(path, account);
		study.setUseBanner(true);
		return study;
	}

	public Study disableStudyBanner(String path, Account account) {
		Study study = studyFinderService.findByPathForManager(path, account);
		study.setUseBanner(true);
		return study;
	}

	public void addTag(Study study, Tag tag) {
		study.getTags().add(tag);
	}

	public void removeTag(Study study, Tag tag) {
		study.getTags().remove(tag);
	}

	public void saveZone(Account account, String path, ZoneForm zoneForm) {
		Study study = studyFinderService.findByZoneAndPath(path);
		checkIfManager(account, study);
		Zone zone = zoneInfoFinderService.findByCityAndProvince(zoneForm);
		study.getZones().add(zone);
	}

	public void removeZone(Account account, String path, ZoneForm zoneForm) {
		Study study = studyFinderService.findByZoneAndPath(path);
		checkIfExistingStudy(path, study);
		checkIfManager(account, study);
		Zone zone = zoneInfoFinderService.findByCityAndProvince(zoneForm);
		study.getZones().remove(zone);
	}

	private void checkIfManager(Account account, Study study) {
		if (!study.isManagedBy(account)) {
			throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
		}
	}

	private void checkIfExistingStudy(String path, Study study) {
		if (study == null) {
			throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
		}
	}

}
