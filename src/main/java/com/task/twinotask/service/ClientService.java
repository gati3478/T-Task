package com.task.twinotask.service;

import com.task.twinotask.entity.Client;
import com.task.twinotask.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long id) {
        return clientRepository.findOne(id);
    }

    public Client findByEmailAddress(String email) {
        return clientRepository.findByEmail(email);
    }

    public boolean isExistingUser(String email) {
        return clientRepository.findByEmail(email) != null;
    }

    public void delete(Client user) {
        clientRepository.delete(user);
    }

}
