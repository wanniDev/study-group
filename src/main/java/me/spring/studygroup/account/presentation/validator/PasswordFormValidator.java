package me.spring.studygroup.account.presentation.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.spring.studygroup.account.presentation.form.PasswordForm;

public class PasswordFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PasswordForm passwordForm = (PasswordForm)target;
		if (!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfirm())) {
			errors.rejectValue("newPassword", "wrong.value", "Invalid new password...");
		}
	}
}
