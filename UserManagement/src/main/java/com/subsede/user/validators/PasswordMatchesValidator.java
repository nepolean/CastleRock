package com.subsede.user.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.subsede.user.model.user.PasswordDTO;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		PasswordDTO user = (PasswordDTO) obj;
		return user.getPassword().equals(user.getVerifyPassword());
	}
}