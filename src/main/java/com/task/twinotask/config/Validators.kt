package com.task.twinotask.config

import com.task.twinotask.web.validator.RegistrationFormValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Validators {

	@Bean
	fun registrationFormValidator() = RegistrationFormValidator()
}
