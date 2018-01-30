package com.task.twinotask.service

import com.task.twinotask.entity.Client
import com.task.twinotask.entity.ProfileVisibility
import com.task.twinotask.entity.Role
import com.task.twinotask.repository.ClientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

import java.sql.Date

@DataJpaTest
class JpaIntegrationSpec extends Specification {

	@Autowired
	TestEntityManager entityManager

	@Autowired
	ClientRepository clientRepository

	@Autowired
	BCryptPasswordEncoder passwordEncoder

	def "spring data jpa saves clients"() {
		given: "some registered users"
		entityManager.persist(testClient("name1@domain.com"))
		entityManager.persist(testClient("name2@domain.com"))

		expect: "the correct count of clients inside the repository"
		clientRepository.count() == 2L
	}

	def "spring data jpa retrieves by email"() {
		given: "some registered users"
		entityManager.persist(testClient("name1@domain.com"))
		entityManager.persist(testClient("name2@domain.com"))

		when: "the user for the given email is queried"
		def client = clientRepository.findByEmail("name2@domain.com")

		then: "the client should be returned"
		client.email == "name2@domain.com"
		client.salary == 100
		client.liabilities == 120
	}

	def testClient(String email) {
		return new Client(
				"First",
				"Last",
				email,
				passwordEncoder.encode("p"),
				null,
				new Date(System.currentTimeMillis()),
				100,
				120,
				Collections.singletonList(new Role("ROLE_USER", 0)),
				ProfileVisibility.REGISTERED,
				false,
				0
		)
	}

	@TestConfiguration
	static class EncoderConfig {

		@Bean
		BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder()
		}

	}
}
