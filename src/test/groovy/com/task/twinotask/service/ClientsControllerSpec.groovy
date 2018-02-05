package com.task.twinotask.service

import com.task.twinotask.entity.Client
import com.task.twinotask.entity.ProfileVisibility
import com.task.twinotask.entity.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import java.sql.Date

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientsControllerSpec extends Specification {

	@Autowired
	ClientService clientService

	@Autowired
	TestRestTemplate restTemplate

	@Autowired
	BCryptPasswordEncoder passwordEncoder

	def "rest service fetches user by email"() {
		given: "some test user"
		def testClient = testClient("name1@domain.com")
		clientService.findByEmail(_ as String) >> testClient

		when: "the test user is queried by email"
		def entity = restTemplate.getForEntity("/find-by-email?email=e@e.e", Client)

		then: "the correct user is returned"
		entity.statusCode == HttpStatus.OK
		entity.body.email == testClient.email
	}

	def "rest service fetches all users"() {
		given: "some test users"
		def testClient1 = testClient("name1@domain.com")
		def testClient2 = testClient("name2@domain.com")
		clientService.findAll() >> Arrays.asList(testClient1, testClient2)

		when: "all users are queried"
		def entity = restTemplate.getForEntity("/find-all", Client[])

		then: "the list of all users is returned"
		entity.statusCode == HttpStatus.OK
		def body = Arrays.asList(entity.body)
		body.size() == 2
		body.get(1).email == testClient2.email
	}

	def "rest service fetches client by id"() {
		given: "some test user"
		def testClient = testClient("name@domain.com")
		clientService.findById(_ as Long) >> testClient

		when: "the test user is queried by id"
		def entity = restTemplate.getForEntity("/find-by-id?id=800", Client)

		then: "the correct user is returned"
		entity.statusCode == HttpStatus.OK
		entity.body.email == testClient.email
	}

	def testClient(String email) {
		return new Client(
				"Name",
				"Surname",
				email,
				passwordEncoder.encode("p"),
				null,
				new Date(System.currentTimeMillis()),
				1000,
				420,
				Collections.singletonList(new Role("ROLE_USER", 0)),
				ProfileVisibility.REGISTERED,
				false,
				0
		)
	}

	@TestConfiguration
	static class MockConfig {
		def detachedMockFactory = new DetachedMockFactory()

		@Bean
		ClientService clientService() {
			return detachedMockFactory.Stub(ClientService)
		}

	}
}
