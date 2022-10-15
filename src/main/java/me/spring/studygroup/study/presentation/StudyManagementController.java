package me.spring.studygroup.study.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.presentation.application.StudyFinderService;

@Controller
@RequiredArgsConstructor
public class StudyManagementController {
	private final StudyFinderService studyFinderService;

	@GetMapping("/study/{path}/members")
	public String viewStudyMembers(@AuthAccount Account account, @PathVariable String path, Model model) {
		Study study = studyFinderService.findByPath(path);
		model.addAttribute(account);
		model.addAttribute(study);
		return "study/members";
	}
}
