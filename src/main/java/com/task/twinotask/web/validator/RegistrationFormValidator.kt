package com.task.twinotask.web.validator

import com.task.twinotask.service.ClientService
import com.task.twinotask.util.yearsSince
import com.task.twinotask.web.dto.ClientRegistrationDto
import org.springframework.validation.Errors
import org.springframework.validation.Validator

const val AGE_LIMIT = 20

class RegistrationFormValidator(private val clientService: ClientService) : Validator {

	override fun supports(clazz: Class<*>) = ClientRegistrationDto::class.java == clazz

	override fun validate(target: Any, errors: Errors) {
		val (_, _, email, _, _, birthDate) = target as ClientRegistrationDto

		if (clientService.userExists(email)) {
			errors.rejectValue("email", null, "E-mail is already used")
		}

		val age = birthDate.yearsSince()

		if (age < AGE_LIMIT) {
			errors.rejectValue("birthDate", null, "You should be at least 20.")
		}
	}

}
