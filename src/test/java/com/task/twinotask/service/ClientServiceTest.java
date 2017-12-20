package com.task.twinotask.service;

import com.task.twinotask.entity.Client;
import com.task.twinotask.repository.ClientRepository;
import com.task.twinotask.web.dto.ClientRegistrationDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl.verify;

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
        ClientRegistrationDto client = new ClientRegistrationDto();
        client.setFirstName("A");
        client.setLastName("a");
        client.setPassword("a");
        client.setEmail("a@a.com");
        client.setSalary(100);
        client.setLiabilities(100);
        client.setBirthDate(Date.valueOf("1994-01-01"));
        client.setPhoneNumber("1");

        clientService.registerClient(client);

        Mockito.verify(clientRepository).save(any(Client.class));

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);

        Mockito.verify(clientRepository).save(captor.capture());
        assertEquals(captor.getValue().getEmail(), client.getEmail());
        // And the same for the other fields
    }
}