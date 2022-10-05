package me.spring.studygroup.home;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.infrastructure.security.AuthAccount;

@Controller
public class HomeController {
	@GetMapping("/")
	public String home(@AuthAccount Account account, Model model) {

		if (account != null) {
			model.addAttribute(account);
			return "index-after-login";
		}

		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
