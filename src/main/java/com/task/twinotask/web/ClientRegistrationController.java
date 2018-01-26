package com.task.twinotask.web;

import com.task.twinotask.exceptions.UserAlreadyExistException;
import com.task.twinotask.service.ClientService;
import com.task.twinotask.web.dto.ClientRegistrationDto;
import com.task.twinotask.web.validator.RegistrationFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.sql.Date;

@Controller
@RequestMapping("/registration")
public class ClientRegistrationController {

	private ClientService clientService;
	private RegistrationFormValidator formValidator;

	@Autowired
	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}

	@Autowired
	public void setRegistrationValidator(RegistrationFormValidator validator) {
		this.formValidator = validator;
	}

	@SuppressWarnings("unused")
	@GetMapping
	public String showRegistrationForm(Model model) {
		return "registration";
	}

	@ModelAttribute("client")
	public ClientRegistrationDto clientRegistrationDto() {
		return new ClientRegistrationDto(
				"",
				"",
				"",
				"",
				"",
				Date.valueOf("1994-01-24"),
				0,
				0);
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("client") @Valid ClientRegistrationDto clientDto,
									  BindingResult result) {
		formValidator.validate(clientDto, result);

		if (result.hasErrors()) {
			return "registration";
		}

		try {
			clientService.registerClient(clientDto);
		} catch (UserAlreadyExistException e) {
			result.rejectValue("email", null, "E-mail is already used");
			return "registration";
		}

		return "login";
	}


}
