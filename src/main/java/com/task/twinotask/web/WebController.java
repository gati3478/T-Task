package com.task.twinotask.web;

import com.task.twinotask.entity.Client;
import com.task.twinotask.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class WebController {

    private static final int MAX_AGE_LIMIT = 70;

    @Autowired
    private ClientService clientService;

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
        return clientService.findById(id).toString();
    }

    @RequestMapping("/find-by-email")
    public String findByEmail(@RequestParam("email") String email) {
        return clientService.findByEmail(email).toString();
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    @ResponseBody
    public String getHomepage(HttpServletRequest request) {
        String result = "<h3>My Info:</h3><br/>";

        // Current user
        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        Client loggedClient = clientService.findByEmail(email);
        result += loggedClient.toString() + "<br/><br/><br/>";

        // User's age:
        int age = ClientRegistrationController.yearsSince(loggedClient.getBirthDate());
        int creditLimit = 0;
        if (age < MAX_AGE_LIMIT) {
            creditLimit = age * 100 + loggedClient.getSalary() - loggedClient.getLiabilities();
        }
        result += "<h3>My credit limit: </h3>" + creditLimit + "<br/><br/><br/>";

        // The rest of the users
        result += "<h3>All clients:</h3><br/>";
        StringBuilder resultBuilder = new StringBuilder(result);
        for (Client client : clientService.findAll()) {
            resultBuilder.append(client.toString()).append("<br/><br/>");
        }
        result = resultBuilder.toString();

        return result;
    }

}
