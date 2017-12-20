package com.task.twinotask.service;

import com.task.twinotask.entity.Client;
import com.task.twinotask.entity.ProfileVisibility;
import com.task.twinotask.entity.Role;
import com.task.twinotask.exceptions.UserAlreadyExistException;
import com.task.twinotask.repository.ClientRepository;
import com.task.twinotask.web.dto.ClientRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
@Service
public class ClientService implements UserDetailsService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void setClientRepository(ClientRepository repository) {
        clientRepository = repository;
    }

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Client registerClient(final ClientRegistrationDto registration) throws UserAlreadyExistException {
        if (userExists(registration.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + registration.getEmail());
        }

        Client client = new Client();
        client.setFirstName(registration.getFirstName());
        client.setLastName(registration.getLastName());
        client.setEmail(registration.getEmail());
        client.setPassword(passwordEncoder.encode(registration.getPassword()));
        client.setBirthDate(registration.getBirthDate());
        client.setPhoneNumber(registration.getPhoneNumber());
        client.setSalary(registration.getSalary());
        client.setLiabilities(registration.getLiabilities());
        client.setVisibility(ProfileVisibility.REGISTERED);
        client.setRoles(Collections.singletonList(new Role("ROLE_USER")));
        return clientRepository.save(client);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new UsernameNotFoundException("Invalid username.");
        }
        return new User(client.getEmail(),
                client.getPassword(),
                mapRolesToAuthorities(client.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(final Long id) {
        return clientRepository.findOne(id);
    }

    public Client findByEmail(final String email) {
        return clientRepository.findByEmail(email);
    }

    public boolean userExists(final String email) {
        return clientRepository.findByEmail(email) != null;
    }

    public void delete(final Client user) {
        clientRepository.delete(user);
    }

}
