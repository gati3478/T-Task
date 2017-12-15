package com.task.twinotask.web;

import com.task.twinotask.exceptions.UserAlreadyExistException;
import com.task.twinotask.service.ClientService;
import com.task.twinotask.web.dto.ClientRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@SuppressWarnings("unused")
@Controller
@RequestMapping("/registration")
public class ClientRegistrationController {

    @Autowired
    private ClientService clientService;

    @ModelAttribute("client")
    public ClientRegistrationDto clientRegistrationDto() {
        return new ClientRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("client") @Valid ClientRegistrationDto clientDto,
                                      BindingResult result) {

        try {
            clientService.registerClient(clientDto);

            return "redirect:/login?success=true";
        } catch (UserAlreadyExistException e) {
            result.rejectValue("email", null, "There is already an account registered with that email");

            if (result.hasErrors()) {
                return "registration";
            }
        }

        return "login?error=true";
    }

}