package me.spring.studygroup.account.presentation.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.spring.studygroup.util.validation.Password;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

	@NotBlank
	@Length(min = 3, max = 20)
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
	private String nickname;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Password(message = "Password must contains at least one uppercase letter, "
		+ "one lowercase letter, one number and one special character between 10 ~ 50 characters...")
	private String password;

}
