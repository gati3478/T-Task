package com.task.twinotask.web;

import com.task.twinotask.entity.Client;
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

@Controller
@RequestMapping("/registration")
public class ClientRegistrationController {

    @Autowired
    private ClientService clientService;

    @ModelAttribute("user")
    public ClientRegistrationDto userRegistrationDto() {
        return new ClientRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid ClientRegistrationDto userDto,
                                      BindingResult result) {

        Client existing = clientService.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            return "registration";
        }

        clientService.registerClient(userDto);
        return "redirect:/registration?success";
    }

}