package com.task.twinotask.config;

import com.task.twinotask.web.validator.RegistrationFormValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Validators {

	@Bean
	public RegistrationFormValidator registrationFormValidator() {
		return new RegistrationFormValidator();
	}
}
