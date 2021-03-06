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

import javax.persistence.PersistenceException
import java.sql.Date

@DataJpaTest
class JpaIntegrationSpec extends Specification {

	@Autowired
	TestEntityManager entityManager

	@Autowired
	ClientRepository clientRepository

	@Autowired
	BCryptPasswordEncoder passwordEncoder

	def "repository saves clients"() {
		given: 'nothing has been done yet'
		
		when:
		def count = clientRepository.count()
		
		then:
		count == 0L
		
		when: "we register 3 users"
		entityManager.persist(testClient("name1@domain.com"))
		entityManager.persist(testClient("name2@domain.com"))
		entityManager.persist(testClient("name3@domain.com"))
		
		and:
		def savedClientsEmails = clientRepository.findAll().collect { it.email }

		then: "the correct count of clients inside the repository"
		clientRepository.count() == 3L
		savedClientsEmails.containsAll(["name1@domain.com", "name2@domain.com", "name3@domain.com"])
	}

	def "repository retrieves by email"() {
		given: "some registered users"
		entityManager.persist(testClient("name1@domain.com"))
		entityManager.persist(testClient("name2@domain.com"))

		when: "the user for the given email is queried"
		def client = clientRepository.findByEmail("name2@domain.com")

		then: "the correct client should be returned"
		client.email == "name2@domain.com"
		client.firstName == "First"
		client.lastName == "Last"
		client.salary == 100
		client.liabilities == 120
		passwordEncoder.matches("p", client.password)
		client.phoneNumber == "456-789-123"
		client.visibility == ProfileVisibility.REGISTERED
		!client.dateAdjusted
	}

	def "repository entities are unique by email"() {
		given: "a registered user"
		entityManager.persist(testClient("name1@domain.com"))

		when: "the same email is used again to register"
		entityManager.persist(testClient("name1@domain.com"))

		then: "repository won't save it"
		thrown(PersistenceException)
	}

	def testClient(String email) {
		return new Client(
				"First",
				"Last",
				email,
				passwordEncoder.encode("p"),
				"456-789-123",
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
