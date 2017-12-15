package com.task.twinotask.web;

import com.task.twinotask.entity.Client;
import com.task.twinotask.entity.ProfileVisibility;
import com.task.twinotask.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
public class WebController {
    
    @Autowired
    ClientService clientService;

    @RequestMapping("/save")
    public String process() {
        Client client = new Client();
        client.setFirstName("Gati");
        client.setLastName("Petriashvili");
        client.setBirthDate(Date.valueOf("1994-01-24"));
        client.setEmail("giopetriashvili@gmail.com");
        client.setLiabilities(50);
        client.setVisibility(ProfileVisibility.REGISTERED);
        client.setPhoneNumber("+995595473533");
        client.setSalary(50);
        client.setPassword("password");
        clientService.saveClient(client);
        return "Done";
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
        result = clientService.findByEmailAddress(email).toString();
        return result;
    }

}