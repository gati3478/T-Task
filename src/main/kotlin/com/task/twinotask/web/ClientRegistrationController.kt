package com.task.twinotask.web

import com.task.twinotask.service.ClientService
import com.task.twinotask.web.dto.ClientRegistrationDto
import com.task.twinotask.web.validator.RegistrationFormValidator
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@Controller
@RequestMapping("/registration")
class ClientRegistrationController(
	private val clientService: ClientService,
	private var formValidator: RegistrationFormValidator
) {

	@GetMapping
	fun showRegistrationForm(model: Model) = "registration"

	@ModelAttribute("client")
	fun clientRegistrationDto() = ClientRegistrationDto()

	@PostMapping
	fun registerUserAccount(
		@ModelAttribute("client") @Valid clientDto: ClientRegistrationDto,
		result: BindingResult
	): String {
		formValidator.validate(clientDto, result)

		if (result.hasErrors()) {
			return "registration"
		}

		clientService.registerClient(clientDto)

		return "login"
	}

}
