package me.spring.studygroup.account.presentation.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.account.application.AccountInfoFinderService;
import me.spring.studygroup.account.presentation.form.SignUpForm;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountInfoFinderService accountInfoFinderService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpForm signUpForm = (SignUpForm)object;
        if (accountInfoFinderService.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "Already existed email.");
        }

        if (accountInfoFinderService.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpForm.getEmail()}, "Already existed nickname");
        }
    }
}
