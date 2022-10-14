package me.spring.studygroup.account.presentation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.application.AccountInfoFinderService;
import me.spring.studygroup.account.application.AccountProfileSettingService;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;
import me.spring.studygroup.account.presentation.form.NicknameForm;
import me.spring.studygroup.account.presentation.form.NotificationForm;
import me.spring.studygroup.account.presentation.form.PasswordForm;
import me.spring.studygroup.account.presentation.form.ProfileForm;
import me.spring.studygroup.account.presentation.validator.PasswordFormValidator;
import me.spring.studygroup.tag.domain.Tag;
import me.spring.studygroup.tag.presentation.form.TagForm;
import me.spring.studygroup.zone.application.ZoneInfoFinderService;
import me.spring.studygroup.zone.domain.Zone;
import me.spring.studygroup.zone.presentation.form.ZoneForm;

@Controller
@RequiredArgsConstructor
public class AccountProfileController {

	private final AccountInfoFinderService accountInfoFinderService;
	private final AccountProfileSettingService profileSettingService;
	private final ZoneInfoFinderService zoneInfoFinderService;
	private final ModelMapper modelMapper;
	private final ObjectMapper objectMapper;

	@InitBinder("passwordForm")
	public void passwordFormInitBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(new PasswordFormValidator());
	}

	@GetMapping("/profile/{nickname}")
	public String viewProfile(@PathVariable String nickname, Model model, @AuthAccount Account account) {
		Account accountToView = accountInfoFinderService.findByNickName(nickname);
		model.addAttribute(accountToView);
		model.addAttribute("isOwner", accountToView.equals(account));
		return "account/profile";
	}

	@GetMapping("/settings/profile")
	public String updateProfileForm(@AuthAccount Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(modelMapper.map(account, ProfileForm.class));
		return "settings/profile";
	}

	@PostMapping("/settings/profile")
	public String updateProfile(@AuthAccount Account account, @Valid ProfileForm profileForm, Errors errors,
		Model model, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			model.addAttribute(account);
			return "settings/profile";
		}

		profileSettingService.updateProfile(account, profileForm);
		attributes.addFlashAttribute("message", "Profile updated.");
		return "redirect:/settings/profile";
	}

	@GetMapping("/settings/password")
	public String updatePasswordForm(@AuthAccount Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(new PasswordForm());
		return "/settings/password";
	}

	@PostMapping("/settings/password")
	public String updatePassword(@AuthAccount Account account, @Valid PasswordForm passwordForm, Errors errors,
		Model model, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			model.addAttribute(account);
			return "/settings/password";
		}

		profileSettingService.updatePassword(account, passwordForm.getNewPassword());
		attributes.addFlashAttribute("message", "Password update successfully.");
		return "redirect:/settings/password";
	}

	@GetMapping("/settings/account")
	public String updateAccountForm(@AuthAccount Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(modelMapper.map(account, NicknameForm.class));
		return "settings/account";
	}

	@PostMapping("/settings/account")
	public String updateAccount(@AuthAccount Account account, @Valid NicknameForm nicknameForm, Errors errors,
		Model model, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			model.addAttribute(account);
			return "settings/account";
		}

		profileSettingService.updateNickname(account, nicknameForm.getNickname());
		attributes.addFlashAttribute("message", "닉네임을 수정했습니다.");
		return "redirect:/settings/account";
  }
  
	@GetMapping("/settings/notifications")
	public String updateNotificationsForm(@AuthAccount Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(modelMapper.map(account, NotificationForm.class));
		return "settings/notifications";
	}

	@PostMapping("/settings/notifications")
	public String updateNotifications(@AuthAccount Account account, @Valid NotificationForm notifications, Errors errors,
		Model model, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			model.addAttribute(account);
			return "settings/notifications";
		}

		profileSettingService.updateNotifications(account, notifications);
		attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
		return "redirect:/settings/notifications";
	}

	@GetMapping("/settings/tags")
	public String updateTags(@AuthAccount Account account, Model model) throws JsonProcessingException {
		model.addAttribute(account);

		Set<Tag> tags = accountInfoFinderService.findTagsBy(account);
		model.addAttribute("tags", tags.stream().map(Tag::getTitle).collect(Collectors.toList()));

		List<String> allTags = accountInfoFinderService.findTagsTitle();
		model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));

		return "settings/tags";
	}

	@PostMapping("/settings/tags/add")
	@ResponseBody
	public ResponseEntity addTag(@AuthAccount Account account, @RequestBody TagForm tagForm) {
		Tag tag = profileSettingService.findOrCreateNew(tagForm.getTagTitle());
		profileSettingService.addTag(account, tag);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/settings/tags/remove")
	@ResponseBody
	public ResponseEntity removeTag(@AuthAccount Account account, @RequestBody TagForm tagForm) {
		profileSettingService.removeTag(account, tagForm);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/settings/zones/add")
	@ResponseBody
	public ResponseEntity addZone(@AuthAccount Account account, @RequestBody ZoneForm zoneForm) {
		Zone zone = zoneInfoFinderService.findByCityAndProvince(zoneForm);

		profileSettingService.addZone(account, zone);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/settings/zones")
	public String updateZonesForm(@AuthAccount Account account, Model model) throws JsonProcessingException {
		model.addAttribute(account);

		Set<Zone> zones = accountInfoFinderService.findZonesFrom(account);
		model.addAttribute("zones", zones.stream().map(Zone::toString).collect(Collectors.toList()));

		List<String> allZones = accountInfoFinderService.findAllZonesWhitelist();
		model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));

		return "settings/zones";
	}
}
