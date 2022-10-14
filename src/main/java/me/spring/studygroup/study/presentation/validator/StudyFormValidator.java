package me.spring.studygroup.study.presentation.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.study.presentation.form.StudyForm;
import me.spring.studygroup.study.presentation.form.StudyRepository;

@Component
@RequiredArgsConstructor
public class StudyFormValidator implements Validator {

	private final StudyRepository studyRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return StudyForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		StudyForm studyForm = (StudyForm)target;
		if (studyRepository.existsByPath(studyForm.getPath())) {
			errors.rejectValue("path", "wrong.path", "해당 스터디 경로값을 사용할 수 없습니다.");
		}
	}
}