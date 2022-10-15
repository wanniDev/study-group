package me.spring.studygroup.study.presentation;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.presentation.application.StudyFinderService;
import me.spring.studygroup.study.presentation.form.StudyDescriptionForm;

@Controller
@RequestMapping("/study/{path}/settings")
@RequiredArgsConstructor
public class StudyEditController {

	private final StudyFinderService studyFinderService;
	private final ModelMapper modelMapper;

	@GetMapping("/description")
	public String viewStudySetting(@AuthAccount Account account, @PathVariable String path, Model model) {
		Study study = studyFinderService.findByPath(path, account);
		model.addAttribute(account);
		model.addAttribute(study);
		model.addAttribute(modelMapper.map(study, StudyDescriptionForm.class));
		return "study/settings/description";
	}

	@PostMapping("/description")
	public String updateStudyInfo(@AuthAccount Account account, @PathVariable String path,
		@Valid StudyDescriptionForm studyDescriptionForm, Errors errors,
		Model model, RedirectAttributes attributes) {
		Study study = studyFinderService.findByPath(path, account);

		if (errors.hasErrors()) {
			model.addAttribute(account);
			model.addAttribute(study);
			return "study/settings/description";
		}

		studyService.updateStudyDescription(study, studyDescriptionForm);
		attributes.addFlashAttribute("message", "스터디 소개를 수정했습니다.");
		return "redirect:/study/" + study.getEncodedPath() + "/settings/description";
	}
}
