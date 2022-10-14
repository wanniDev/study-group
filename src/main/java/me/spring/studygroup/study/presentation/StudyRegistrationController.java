package me.spring.studygroup.study.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;
import me.spring.studygroup.study.presentation.form.StudyForm;
import me.spring.studygroup.study.presentation.validator.StudyFormValidator;

@Controller
@RequiredArgsConstructor
public class StudyRegistrationController {

	private final StudyFormValidator studyFormValidator;

	@InitBinder("studyForm")
	public void studyFormInitBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(studyFormValidator);
	}

	@GetMapping("/new-study")
	public String newStudyForm(@AuthAccount Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(new StudyForm());
		return "study/form";
	}
}
