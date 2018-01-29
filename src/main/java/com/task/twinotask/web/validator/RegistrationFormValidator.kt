package com.task.twinotask.web.validator

import com.task.twinotask.util.DateUtils
import com.task.twinotask.web.dto.ClientRegistrationDto
import org.springframework.validation.Errors
import org.springframework.validation.Validator

class RegistrationFormValidator : Validator {

	override fun supports(clazz: Class<*>) = ClientRegistrationDto::class.java == clazz

	override fun validate(target: Any, errors: Errors) {
		val (_, _, _, _, _, birthDate) = target as ClientRegistrationDto

		val age = DateUtils.yearsSince(birthDate)

		if (age < AGE_LIMIT) {
			errors.rejectValue("birthDate", null, "You should be at least 21.")
		}
	}

	companion object {
		const val AGE_LIMIT = 20
	}


}
