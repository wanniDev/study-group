package me.spring.studygroup.study.presentation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;
import me.spring.studygroup.study.application.StudyEditService;
import me.spring.studygroup.study.domain.Study;
import me.spring.studygroup.study.application.StudyFinderService;
import me.spring.studygroup.study.presentation.form.StudyDescriptionForm;
import me.spring.studygroup.tag.application.TagManagerService;
import me.spring.studygroup.tag.domain.Tag;
import me.spring.studygroup.tag.domain.TagRepository;
import me.spring.studygroup.tag.presentation.form.TagForm;
import me.spring.studygroup.zone.application.ZoneInfoFinderService;
import me.spring.studygroup.zone.domain.Zone;
import me.spring.studygroup.zone.presentation.form.ZoneForm;

@Controller
@RequestMapping("/study/{path}/settings")
@RequiredArgsConstructor
public class StudyEditController {

	private final StudyFinderService studyFinderService;
	private final StudyEditService studyEditService;
	private final TagManagerService tagManagerService;
	private final ZoneInfoFinderService zoneInfoFinderService;
	private final TagRepository tagRepository;
	private final ModelMapper modelMapper;
	private final ObjectMapper objectMapper;

	@GetMapping("/description")
	public String viewStudySetting(@AuthAccount Account account, @PathVariable String path, Model model) {
		Study study = studyFinderService.findByPathForManager(path, account);
		model.addAttribute(account);
		model.addAttribute(study);
		model.addAttribute(modelMapper.map(study, StudyDescriptionForm.class));
		return "study/settings/description";
	}

	@PostMapping("/description")
	public String updateStudyInfo(@AuthAccount Account account, @PathVariable String path,
		@Valid StudyDescriptionForm studyDescriptionForm, Errors errors,
		Model model, RedirectAttributes attributes) {
		Study study = studyFinderService.findByPathForManager(path, account);

		if (errors.hasErrors()) {
			model.addAttribute(account);
			model.addAttribute(study);
			return "study/settings/description";
		}

		studyEditService.saveDescription(study, studyDescriptionForm);
		attributes.addFlashAttribute("message", "스터디 소개를 수정했습니다.");
		return "redirect:/study/" + study.getEncodedPath() + "/settings/description";
	}

	@GetMapping("/banner")
	public String studyImageForm(@AuthAccount Account account, @PathVariable String path, Model model) {
		Study study = studyFinderService.findByPathForManager(path, account);
		model.addAttribute(account);
		model.addAttribute(study);
		return "study/settings/banner";
	}

	@PostMapping("/banner")
	public String studyImageSubmit(@AuthAccount Account account, @PathVariable String path,
		String image, RedirectAttributes attributes) {
		Study study = studyEditService.saveBanner(path, account, image);
		attributes.addFlashAttribute("message", "스터디 이미지를 수정했습니다.");
		return "redirect:/study/" + study.getEncodedPath() + "/settings/banner";
	}

	@PostMapping("/banner/enable")
	public String enableStudyBanner(@AuthAccount Account account, @PathVariable String path) {
		Study study = studyEditService.enableStudyBanner(path, account);
		return "redirect:/study/" + study.getEncodedPath() + "/settings/banner";
	}

	@PostMapping("/banner/disable")
	public String disableStudyBanner(@AuthAccount Account account, @PathVariable String path) {
		Study study = studyEditService.disableStudyBanner(path, account);
		return "redirect:/study/" + study.getEncodedPath() + "/settings/banner";
	}

	@GetMapping("/tags")
	public String studyTagsForm(@AuthAccount Account account, @PathVariable String path, Model model)
		throws JsonProcessingException {
		Study study = studyFinderService.findByPathForManager(path, account);
		model.addAttribute(account);
		model.addAttribute(study);

		model.addAttribute("tags", study.getTags().stream()
			.map(Tag::getTitle).collect(Collectors.toList()));
		List<String> allTagTitles = tagRepository.findTagsTitle();
		model.addAttribute("whitelist", objectMapper.writeValueAsString(allTagTitles));
		return "study/settings/tags";
	}

	@PostMapping("/tags/add")
	@ResponseBody
	public ResponseEntity addTag(@AuthAccount Account account, @PathVariable String path,
		@RequestBody TagForm tagForm) {
		Study study = studyFinderService.findByPathForManager(path, account);
		Tag tag = tagManagerService.findOrCreateNew(tagForm.getTagTitle());
		studyEditService.addTag(study, tag);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/tags/remove")
	@ResponseBody
	public ResponseEntity removeTag(@AuthAccount Account account, @PathVariable String path,
		@RequestBody TagForm tagForm) {
		Study study = studyFinderService.findByPathForManager(path, account);
		Tag tag = tagManagerService.findByTitle(tagForm.getTagTitle());

		studyEditService.removeTag(study, tag);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/zones")
	public String studyZonesForm(@AuthAccount Account account, @PathVariable String path, Model model)
		throws JsonProcessingException {
		Study study = studyFinderService.findByPathForManager(path, account);
		model.addAttribute(account);
		model.addAttribute(study);
		model.addAttribute("zones", study.getZones().stream()
			.map(Zone::toString).collect(Collectors.toList()));
		List<String> allZones = zoneInfoFinderService.findZonesWhiteList();
		model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));
		return "study/settings/zones";
	}

	@PostMapping("/zones/add")
	@ResponseBody
	public ResponseEntity addZone(@AuthAccount Account account, @PathVariable String path,
		@RequestBody ZoneForm zoneForm) {

		studyEditService.saveZone(account, path, zoneForm);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/zones/remove")
	@ResponseBody
	public ResponseEntity removeZone(@AuthAccount Account account, @PathVariable String path,
		@RequestBody ZoneForm zoneForm) {

		studyEditService.removeZone(account, path, zoneForm);
		return ResponseEntity.ok().build();
	}
}
