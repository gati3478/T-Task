package com.task.twinotask.web.validator;

import com.task.twinotask.util.DateUtils;
import com.task.twinotask.web.dto.ClientRegistrationDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RegistrationFormValidator implements Validator {

	private static final int AGE_LIMIT = 20;

	@Override
	public boolean supports(Class<?> clazz) {
		return ClientRegistrationDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ClientRegistrationDto clientDto = (ClientRegistrationDto) target;

		int age = DateUtils.yearsSince(clientDto.getBirthDate());

		if (age < AGE_LIMIT) {
			errors.rejectValue("birthDate", null, "You should be at least 21.");
		}
	}


}
