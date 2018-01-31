package com.task.twinotask.service

import com.task.twinotask.web.dto.ClientRegistrationDto

import java.sql.Date
import java.time.LocalDate

class TestHelper {
	static def testUserDto(String email, int age) {
		LocalDate ld = LocalDate.now().minusYears(age)
		Date birthDate = Date.valueOf(ld)

		return new ClientRegistrationDto(
				"Name",
				"Surname",
				email,
				"p",
				"123",
				birthDate,
				1200,
				120
		)
	}
}
