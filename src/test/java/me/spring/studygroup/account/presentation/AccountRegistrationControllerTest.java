package me.spring.studygroup.account.presentation;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import me.spring.studygroup.account.application.EmailService;
import me.spring.studygroup.account.infrastructure.thymeleaf.ViewTemplateContextService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AccountRegistrationControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@DisplayName("회원가입 뷰를 가리키는지 확인하는 테스트")
	@Test
	void signUpForm() throws Exception {
		mockMvc.perform(get("/sign-up"))
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andExpect(model().attributeExists("signUpForm"));
	}

	@DisplayName("회원가입 처리 입력 오류 처리 확인하는 테스트")
	@Test
	void signUpSubmitWithWrongInput() throws Exception {
		mockMvc.perform(post("/sign-up")
				.param("nickname", "chj12345")
				.param("email", "wrong_email_format")
				.param("password", "123456")
			.with(csrf())
		).andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andExpect(unauthenticated());
	}

	@DisplayName("인증 메일 확인 - 입력값 오류")
	@Test
	void checkEmailToken_with_wrong_input() throws Exception {
		mockMvc.perform(get("/check-email-token")
				.param("token", "sdfjslwfwef")
				.param("email", "email@email.com"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("error"))
			.andExpect(view().name("account/checked-email"))
			.andExpect(unauthenticated());
	}
}