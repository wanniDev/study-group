package me.spring.studygroup.account.presentation;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.application.AccountInfoFinderService;
import me.spring.studygroup.account.application.AccountProfileService;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;
import me.spring.studygroup.account.presentation.form.ProfileForm;

@Controller
@RequiredArgsConstructor
public class AccountProfileController {

	private final AccountInfoFinderService accountInfoFinderService;
	private final AccountProfileService accountProfileService;
	private final ModelMapper modelMapper;

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

		accountProfileService.updateProfile(account, profileForm);
		attributes.addFlashAttribute("message", "Profile updated.");
		return "redirect:/settings/profile";
	}
}
