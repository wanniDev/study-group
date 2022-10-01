package me.spring.studygroup.member.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MemberRegistrationControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@DisplayName("회원가입 뷰를 가리키는지 확인하는 테스트")
	@Test
	void signUpForm() throws Exception {
		mockMvc.perform(get("/member/join"))
			.andExpect(status().isOk())
			.andExpect(view().name("member/join"));
	}
}