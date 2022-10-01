package me.spring.studygroup.member.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("member")
public class MemberRegistrationController {
	@GetMapping("join")
	public String joinPage(Model model) {
		return "member/join";
	}
}
