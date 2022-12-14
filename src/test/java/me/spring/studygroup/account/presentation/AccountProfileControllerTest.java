package me.spring.studygroup.account.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import me.spring.studygroup.zone.application.ZoneInfoFinderService;
import me.spring.studygroup.zone.domain.Zone;
import me.spring.studygroup.zone.domain.ZoneRepository;
import me.spring.studygroup.zone.presentation.form.ZoneForm;

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
	ZoneRepository zoneRepository;
	@Autowired
	ZoneInfoFinderService zoneInfoFinderService;
	@Autowired
	ObjectMapper objectMapper;

	private final Zone testZone = Zone.builder().city("test").localNameOfCity("????????????").province("????????????").build();

	@BeforeEach
	void beforeEach() {
		zoneRepository.save(testZone);
	}

	@AfterEach
	void afterEach() {
		accountRepository.deleteAll();
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("????????? ?????? ???")
	void updateProfileForm() throws Exception {
		mockMvc.perform(get("/settings/profile"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("profileForm"));
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("???????????? ????????? ??????")
	void updateProfile() throws Exception {
		String bio = "?????? ????????? ???????????? ??????.";
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
	@DisplayName("????????? ??????????????? ????????? ??????")
	void updateProfile_error() throws Exception {
		String bio = "?????? ????????? ???????????? ??????. ?????? ????????? ???????????? ??????. ?????? ????????? ???????????? ??????. ???????????? ?????? ????????? ???????????? ??????. ";
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
	@DisplayName("???????????? ?????? ???")
	void updatePassword_form() throws Exception {
		mockMvc.perform(get("/settings/password"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("passwordForm"));
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("???????????? ???????????? ??????")
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
	@DisplayName("???????????? ???????????? ??????????????? ???????????? ???????????? ?????? ??????")
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
	@DisplayName("????????? ?????? ???")
	void updateAccountForm() throws Exception {
		mockMvc.perform(get("/settings/account"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("nicknameForm"));
	}

	@Test
	@WithAccount("wannidev")
	@DisplayName("????????? ???????????? - ????????? ??????")
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
	@DisplayName("????????? ???????????? - ????????? ??????")
	void updateAccount_failure() throws Exception {
		String newNickname = "??\\_(???)_/??";
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
	@DisplayName("????????? ?????? ?????? ???")
	@WithAccount("wannidev")
	void updateTagsForm() throws Exception {
		mockMvc.perform(get("/settings/tags"))
			.andExpect(view().name("settings/tags"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("whitelist"))
			.andExpect(model().attributeExists("tags"));
	}

	@Test
	@DisplayName("????????? ?????? ??????")
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

	@Test
	@DisplayName("????????? ?????? ?????? ?????? ???")
	@WithAccount("wannidev")
	void updateZonesForm() throws Exception {
		mockMvc.perform(get("/settings/zones"))
			.andExpect(view().name("settings/zones"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("whitelist"))
			.andExpect(model().attributeExists("zones"));
	}

	@Test
	@DisplayName("????????? ?????? ?????? ??????")
	@WithAccount("wannidev")
	void addZone() throws Exception {
		ZoneForm zoneForm = new ZoneForm();
		zoneForm.setZoneName(testZone.toString());

		mockMvc.perform(post("/settings/zones/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(zoneForm))
				.with(csrf()))
			.andExpect(status().isOk());

		Account wannidev = accountInfoFinderService.findByNickName("wannidev");
		Zone zone = zoneInfoFinderService.findByCityAndProvince(zoneForm);
		assertTrue(wannidev.getZones().contains(zone));
	}

	@WithAccount("wannidev")
	@DisplayName("????????? ?????? ?????? ??????")
	@Test
	void removeZone() throws Exception {
		ZoneForm zoneForm = new ZoneForm();
		zoneForm.setZoneName(testZone.toString());

		Account wannidev = accountInfoFinderService.findByNickName("wannidev");
		Zone zone = zoneInfoFinderService.findByCityAndProvince(zoneForm);
		accountProfileSettingService.addZone(wannidev, zone);

		mockMvc.perform(post("/settings/zones/remove")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(zoneForm))
				.with(csrf()))
			.andExpect(status().isOk());

		assertFalse(wannidev.getZones().contains(zone));
	}
}