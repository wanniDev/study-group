package me.spring.studygroup.study.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;
import me.spring.studygroup.study.presentation.form.StudyForm;

@Controller
public class StudyRegistrationController {
	@GetMapping("/new-study")
	public String newStudyForm(@AuthAccount Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(new StudyForm());
		return "study/form";
	}
}
