package com.task.twinotask.web;

import com.task.twinotask.entity.Client;
import com.task.twinotask.service.ClientService;
import com.task.twinotask.util.DateUtils;
import com.task.twinotask.web.dto.CreditInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class ClientRestController {

	private static final int MAX_AGE_LIMIT = 70;

	private ClientService clientService;

	@Autowired
	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}

	@RequestMapping(value = "/find-all", method = RequestMethod.GET)
	public List<Client> findAll() {
		return clientService.findAll();
	}

	@RequestMapping(value = "/find-by-id", method = RequestMethod.GET)
	public Client findById(@RequestParam("id") long id) {
		return clientService.findById(id);
	}

	@RequestMapping(value = "/find-by-email", method = RequestMethod.GET)
	public Client findByEmail(@RequestParam("email") String email) {
		return clientService.findByEmail(email);
	}

	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public Client getLoggedInUser(Principal principal) {
		String email = principal.getName();
		return clientService.findByEmail(email);
	}

	@RequestMapping(value = "/credit-limit", method = RequestMethod.GET)
	public CreditInfo getCreditLimit(@RequestParam("id") long id) {
		Client client = clientService.findById(id);

		// User's age:
		int age = DateUtils.yearsSince(client.getBirthDate());
		int creditLimit = 0;
		if (age < MAX_AGE_LIMIT) {
			creditLimit = age * 100 + client.getSalary() - client.getLiabilities();
		}

		return new CreditInfo(client.getId(), creditLimit);
	}

}
