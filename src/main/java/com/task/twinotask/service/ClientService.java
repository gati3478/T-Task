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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ClientService implements UserDetailsService {

	private ClientRepository clientRepository;

	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public void setClientRepository(ClientRepository repository) {
		clientRepository = repository;
	}

	@Autowired
	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@SuppressWarnings("UnusedReturnValue")
	public Client registerClient(final ClientRegistrationDto registration) throws UserAlreadyExistException {
		if (userExists(registration.getEmail())) {
			throw new UserAlreadyExistException("There is an account with that email address: " + registration.getEmail());
		}

		Client client = new Client(
				Objects.requireNonNull(registration.getFirstName()),
				Objects.requireNonNull(registration.getLastName()),
				Objects.requireNonNull(registration.getEmail()),
				passwordEncoder.encode(Objects.requireNonNull(registration.getPassword())),
				registration.getPhoneNumber(),
				registration.getBirthDate(),
				registration.getSalary(),
				registration.getLiabilities(),
				Collections.singletonList(new Role("ROLE_USER", -1)),
				ProfileVisibility.REGISTERED,
				false,
				0L
		);

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
				mapRolesToAuthorities(Objects.requireNonNull(client.getRoles())));
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

	@SuppressWarnings("WeakerAccess")
	public boolean userExists(final String email) {
		return clientRepository.findByEmail(email) != null;
	}

	@SuppressWarnings("unused")
	public void delete(final Client user) {
		clientRepository.delete(user);
	}

}
