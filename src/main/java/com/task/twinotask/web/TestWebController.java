package com.task.twinotask.web;

import com.task.twinotask.entity.Client;
import com.task.twinotask.exceptions.UserAlreadyExistException;
import com.task.twinotask.service.ClientService;
import com.task.twinotask.web.dto.ClientRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
public class TestWebController {

    @Autowired
    private ClientService clientService;

    @RequestMapping("/test-save")
    public String process() {
        try {
            ClientRegistrationDto registration = new ClientRegistrationDto();

            registration.setFirstName("Gati");
            registration.setLastName("Petriashvili");
            registration.setBirthDate(Date.valueOf("1994-01-24"));
            registration.setEmail("giopetriashvili@gmail.com");
            registration.setLiabilities(50);
            registration.setPhoneNumber("+995595473533");
            registration.setSalary(50);
            registration.setPassword("password");
            clientService.registerClient(registration);
            return "success";
        } catch (UserAlreadyExistException e) {
            return "email";
        }
    }

    @RequestMapping("/find-all")
    public String findAll() {
        StringBuilder result = new StringBuilder();

        for (Client client : clientService.findAll()) {
            result.append(client.toString()).append("</br>");
        }

        return result.toString();
    }

    @RequestMapping("/find-by-id")
    public String findById(@RequestParam("id") long id) {
        String result = "";
        result = clientService.findById(id).toString();
        return result;
    }

    @RequestMapping("/find-by-email")
    public String findByEmail(@RequestParam("email") String email) {
        String result = "";
        result = clientService.findByEmail(email).toString();
        return result;
    }
}
