package com.task.twinotask.web.dto

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotEmpty
import java.sql.Date
import javax.validation.constraints.Min

data class ClientRegistrationDto(
	@NotEmpty var firstName: String = "",
	@NotEmpty var lastName: String = "",
	@NotEmpty @Email var email: String = "",
	@NotEmpty var password: String = "",
	var phoneNumber: String? = null,
	var birthDate: Date = Date.valueOf("1994-01-24"),
	@Min(value = 0) var salary: Int = 0,
	@Min(value = 0) var liabilities: Int = 0
)
