package me.spring.studygroup.account.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.spring.studygroup.account.application.AccountInfoFinderService;
import me.spring.studygroup.account.application.AccountProfileSettingService;
import me.spring.studygroup.account.domain.Account;
import me.spring.studygroup.account.domain.AccountRepository;
import me.spring.studygroup.account.domain.AccountTag;
import me.spring.studygroup.account.domain.AccountTagRepository;
import me.spring.studygroup.common.annotation.MockMvcTest;
import me.spring.studygroup.common.annotation.WithAccount;
import me.spring.studygroup.tag.domain.Tag;
import me.spring.studygroup.tag.presentation.form.TagForm;

@MockMvcTest
class AccountProfileControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	AccountTagRepository accountTagRepository;
	@Autowired
	AccountInfoFinderService accountInfoFinderService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AccountProfileSettingService accountProfileSettingService;
	@Autowired
	ObjectMapper objectMapper;

	@AfterEach
	void afterEach() {
		accountRepository.deleteAll();
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("프로필 수정 폼")
	void updateProfileForm() throws Exception {
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

		Account wannidev = accountInfoFinderService.findByNickName("wannidev");
		assertNull(wannidev.getBio());
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("패스워드 수정 폼")
	void updatePassword_form() throws Exception {
		mockMvc.perform(get("/settings/password"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("passwordForm"));
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("정상적인 패스워드 수정")
	void updatePassword_success() throws Exception {
		mockMvc.perform(post("/settings/password")
				.param("newPassword", "12345678")
				.param("newPasswordConfirm", "12345678")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/settings/password"))
			.andExpect(flash().attributeExists("message"));

		Account wannidev = accountInfoFinderService.findByNickName("wannidev");
		assertTrue(passwordEncoder.matches("12345678", wannidev.getPassword()));
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("불일치한 비밀번호 입력값으로 발생하는 패스워드 수정 오류")
	void updatePassword_fail() throws Exception {
		mockMvc.perform(post("/settings/password")
				.param("newPassword", "12345678")
				.param("newPasswordConfirm", "11111111")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("/settings/password"))
			.andExpect(model().hasErrors())
			.andExpect(model().attributeExists("passwordForm"))
			.andExpect(model().attributeExists("account"));
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("닉네임 수정 폼")
	void updateAccountForm() throws Exception {
		mockMvc.perform(get("/settings/account"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("nicknameForm"));
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("닉네임 수정하기 - 입력값 정상")
	void updateAccount_success() throws Exception {
		String newNickname = "whiteship";
		mockMvc.perform(post("/settings/account")
				.param("nickname", newNickname)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/settings/account"))
			.andExpect(flash().attributeExists("message"));

		assertNotNull(accountRepository.findByNickname("whiteship"));
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("닉네임 수정하기 - 입력값 에러")
	void updateAccount_failure() throws Exception {
		String newNickname = "¯\\_(ツ)_/¯";
		mockMvc.perform(post("/settings/account")
				.param("nickname", newNickname)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("settings/account"))
			.andExpect(model().hasErrors())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("nicknameForm"));
	}

	@Test
	@DisplayName("계정의 태그 수정 폼")
	@WithAccount("wannidev")
	void updateTagsForm() throws Exception {
		mockMvc.perform(get("/settings/tags"))
			.andExpect(view().name("/settings/tags"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("whitelist"))
			.andExpect(model().attributeExists("tags"));
	}

	@Test
	@DisplayName("계정에 태그 추가")
	@WithAccount("wannidev")
	void addTag() throws Exception {
		TagForm tagForm = new TagForm();
		tagForm.setTagTitle("newTag");

		mockMvc.perform(post("/settings/tags/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(tagForm))
				.with(csrf()))
			.andExpect(status().isOk());

		Tag newTag = accountProfileSettingService.findOrCreateNew("newTag");
		assertNotNull(newTag);
		Account wannidev = accountRepository.findByNickname("wannidev").orElseThrow();
		assertTrue(wannidev.getAccountTags().stream()
			.map(AccountTag::getTag).collect(Collectors.toSet()).contains(newTag));
	}
}