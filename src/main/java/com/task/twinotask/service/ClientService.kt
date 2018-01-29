package com.task.twinotask.service

import com.task.twinotask.entity.Client
import com.task.twinotask.entity.Role
import com.task.twinotask.exceptions.UserAlreadyExistException
import com.task.twinotask.repository.ClientRepository
import com.task.twinotask.web.dto.ClientRegistrationDto
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class ClientService(
	private val clientRepository: ClientRepository,
	@Lazy private var passwordEncoder: BCryptPasswordEncoder
) : UserDetailsService {

	fun registerClient(registration: ClientRegistrationDto): Client {
		if (userExists(registration.email)) {
			throw UserAlreadyExistException(
				"There is an account with that email address: " + registration.email
			)
		}

		val client = Client(
			registration.firstName,
			registration.lastName,
			registration.email,
			passwordEncoder.encode(registration.password),
			registration.phoneNumber,
			registration.birthDate,
			registration.salary,
			registration.liabilities,
			listOf(Role("ROLE_USER"))
		)

		return clientRepository.save(client)
	}

	override fun loadUserByUsername(email: String): UserDetails {
		val (_, _, userEmail, password, _, _, _, _, roles) = clientRepository.findByEmail(email)
				?: throw UsernameNotFoundException("Invalid username.")
		return User(
			userEmail,
			password,
			mapRolesToAuthorities(roles!!)
		)
	}

	private fun mapRolesToAuthorities(roles: Collection<Role>): Collection<GrantedAuthority> =
		roles.map { (name) -> SimpleGrantedAuthority(name) }.toList()

	fun findAll(): MutableList<Client> = clientRepository.findAll()

	fun findById(id: Long?): Client? = clientRepository.findOne(id)

	fun findByEmail(email: String): Client? = clientRepository.findByEmail(email)

	fun userExists(email: String) = clientRepository.findByEmail(email) != null

	@Suppress("unused")
	fun delete(user: Client) {
		clientRepository.delete(user)
	}

}
