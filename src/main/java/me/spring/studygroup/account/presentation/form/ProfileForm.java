package me.spring.studygroup.account.presentation.form;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ProfileForm {

	@Length(max = 35)
	private String bio;

	@Length(max = 50)
	private String url;

	@Length(max = 50)
	private String occupation;

	@Length(max = 50)
	private String location;

	private String profileImage;
}
