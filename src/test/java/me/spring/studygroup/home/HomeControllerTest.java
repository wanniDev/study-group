package me.spring.studygroup.home;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import me.spring.studygroup.account.application.AccountAuthService;
import me.spring.studygroup.account.application.AccountRegisterService;
import me.spring.studygroup.account.domain.AccountRepository;
import me.spring.studygroup.account.presentation.form.SignUpForm;
import me.spring.studygroup.common.annotation.MockMvcTest;

@MockMvcTest
class HomeControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	private AccountRegisterService accountRegisterService;

	@Autowired
	private AccountRepository accountRepository;

	@BeforeEach
	void beforeEach() {
		SignUpForm signUpForm = new SignUpForm();
		signUpForm.setNickname("wannidev");
		signUpForm.setEmail("wannidev0928@gmail.com");
		signUpForm.setPassword("asFEE2!#@@#!45aa");
		accountRegisterService.processNewAccount(signUpForm);
	}

	@AfterEach
	void afterEach() {
		accountRepository.deleteAll();
	}

	@Test
	@DisplayName("이메일 로그인")
	void emailLogin() throws Exception {
		mockMvc.perform(post("/login")
				.param("username", "wannidev0928@gmail.com")
				.param("password", "asFEE2!#@@#!45aa")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(authenticated().withUsername("wannidev"));
	}

	@Test
	@DisplayName("닉네임 로그인")
	void nicknameLogin() throws Exception {
		mockMvc.perform(post("/login")
				.param("username", "wannidev")
				.param("password", "asFEE2!#@@#!45aa")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(authenticated().withUsername("wannidev"));
	}

	@Test
	@DisplayName("로그인 실패")
	void loginFail() throws Exception {
		mockMvc.perform(post("/login")
				.param("username", "asxjcjsdj")
				.param("password", "as!@##@dsds123")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?error"))
			.andExpect(unauthenticated());
	}

	@WithMockUser
	@DisplayName("로그아웃")
	@Test
	void logout() throws Exception {
		mockMvc.perform(post("/logout")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(unauthenticated());
	}
}