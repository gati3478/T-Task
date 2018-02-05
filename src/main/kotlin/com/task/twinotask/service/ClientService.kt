package com.task.twinotask.service

import com.task.twinotask.entity.Client
import com.task.twinotask.entity.Role
import com.task.twinotask.exceptions.UserAlreadyExistException
import com.task.twinotask.repository.ClientRepository
import com.task.twinotask.util.yearsSince
import com.task.twinotask.web.dto.ClientRegistrationDto
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

private const val MAX_AGE_LIMIT = 70

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
			firstName = registration.firstName,
			lastName = registration.lastName,
			email = registration.email,
			password = passwordEncoder.encode(registration.password),
			phoneNumber = registration.phoneNumber,
			birthDate = registration.birthDate,
			salary = registration.salary,
			liabilities = registration.liabilities,
			roles = listOf(Role("ROLE_USER"))
		)

		return clientRepository.save(client)
	}

	override fun loadUserByUsername(email: String): UserDetails {
		val (_, _, userEmail, password, _, _, _, _, roles) = clientRepository.findByEmail(email)
				?: throw UsernameNotFoundException("Invalid username.")
		return User(
			userEmail,
			password,
			roles.mapRolesToAuthorities()
		)
	}

	private fun Collection<Role>.mapRolesToAuthorities() =
		map { (name) -> SimpleGrantedAuthority(name) }.toList()

	fun findAll(): List<Client> = clientRepository.findAll()

	fun findById(id: Long?): Client? = clientRepository.findOne(id)

	fun findByEmail(email: String): Client? = clientRepository.findByEmail(email)

	fun userExists(email: String) = clientRepository.findByEmail(email) != null

	fun getCreditInfoFor(client: Client): CreditInfo {
		val age = client.birthDate.yearsSince()

		var creditLimit = 0
		if (age < MAX_AGE_LIMIT) {
			creditLimit = age * 100 + client.salary - client.liabilities
		}

		return CreditInfo(client.id, creditLimit)
	}

	@Suppress("unused")
	fun delete(user: Client) {
		clientRepository.delete(user)
	}

}

data class CreditInfo(val id: Long, val limit: Int)