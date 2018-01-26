package com.task.twinotask.service;

import com.task.twinotask.entity.Client;
import com.task.twinotask.repository.ClientRepository;
import com.task.twinotask.web.dto.ClientRegistrationDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClientServiceTest {

	@MockBean
	private ClientRepository clientRepository;


	private ClientService clientService;

	@Before
	public void setup() {
		clientService = new ClientService();
		clientService.setClientRepository(clientRepository);
		clientService.setPasswordEncoder(new BCryptPasswordEncoder());
	}

	@Test
	public void testRegister() {
		ClientRegistrationDto client = new ClientRegistrationDto(
				"A",
				"a",
				"a",
				"a@a.com",
				"100-111-121",
				Date.valueOf("1994-01-01"),
				1200,
				100
		);

		clientService.registerClient(client);

		Mockito.verify(clientRepository).save(any(Client.class));

		ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);

		Mockito.verify(clientRepository).save(captor.capture());
		assertEquals(captor.getValue().getEmail(), client.getEmail());
		// And the same for the other fields
	}
}