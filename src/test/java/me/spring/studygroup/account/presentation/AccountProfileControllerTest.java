package me.spring.studygroup.account.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import me.spring.studygroup.account.application.AccountInfoFinderService;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.domain.AccountRepository;
import me.spring.studygroup.common.annotation.MockMvcTest;
import me.spring.studygroup.common.annotation.WithAccount;

@MockMvcTest
class AccountProfileControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountInfoFinderService accountInfoFinderService;

	@AfterEach
	void afterEach() {
		accountRepository.deleteAll();
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("프로필 수정 폼")
	void updateAccountForm() throws Exception {
		mockMvc.perform(get("/settings/profile"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("profileForm"));
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("정상적인 프로필 수정")
	void updateProfile() throws Exception {
		String bio = "짧은 소개를 수정하는 경우.";
		mockMvc.perform(post("/settings/profile")
				.param("bio", bio)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/settings/profile"))
			.andExpect(flash().attributeExists("message"));

		Account wannidev = accountInfoFinderService.findByNickName("wannidev");
		assertEquals(bio, wannidev.getBio());
	}
	
	@Test
	@WithAccount("wannidev")
	@DisplayName("잘못된 입력값으로 프로필 수정")
	void updateProfile_error() throws Exception {
		String bio = "길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 너무나도 길게 소개를 수정하는 경우. ";
		mockMvc.perform(post("/settings/profile")
				.param("bio", bio)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("settings/profile"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("profileForm"))
			.andExpect(model().hasErrors());

		Account keesun = accountInfoFinderService.findByNickName("wannidev");
		assertNull(keesun.getBio());
	}
}