package me.spring.studygroup.study.presentation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.presentation.application.StudyRegisterService;
import me.spring.studygroup.study.presentation.form.StudyForm;
import me.spring.studygroup.study.presentation.validator.StudyFormValidator;

@Controller
@RequiredArgsConstructor
public class StudyRegistrationController {

	private final StudyFormValidator studyFormValidator;
	private final ModelMapper modelMapper;
	private final StudyRegisterService studyRegisterService;


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

	@PostMapping("/new-study")
	public String newStudySubmit(@AuthAccount Account account, @Valid StudyForm studyForm, Errors errors, Model model) {
		if (errors.hasErrors()) {
			model.addAttribute(account);
			return "study/form";
		}

		Study newStudy = studyRegisterService.createNewStudy(modelMapper.map(studyForm, Study.class), account);
		return "redirect:/study/" + URLEncoder.encode(newStudy.getPath(), StandardCharsets.UTF_8);
	}
}
