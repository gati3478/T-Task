package com.task.twinotask.service

import com.task.twinotask.entity.Client
import com.task.twinotask.entity.ProfileVisibility
import com.task.twinotask.entity.Role
import com.task.twinotask.exceptions.UserAlreadyExistException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification
import spock.lang.Subject

import java.sql.Date
import java.time.LocalDate

@Import(ClientService)
@DataJpaTest
class ClientServiceSpec extends Specification {

	@Subject
	@Autowired
	ClientService clientService

	@Autowired
	TestEntityManager entityManager

	@Autowired
	BCryptPasswordEncoder passwordEncoder

	def "client service registers new user"() {
		when: "registering with some user data"
		def client = clientService.registerClient(testUser)

		then: "registration ends with returning client"
		client.email == testUser.email
		passwordEncoder.matches(testUser.password, client.password)
		notThrown(UserAlreadyExistException)

		where:
		testUser << [TestHelper.testUserDto("name1@domain.com", 20),
					 TestHelper.testUserDto("name2@domain.com", 21),
					 TestHelper.testUserDto("name3@domain.com", 22),
					 TestHelper.testUserDto("aaaaaaaaaaaa@n.m", 23)]
	}

	def "client service registers only unique users"() {
		given: "repository containing some user"
		entityManager.persist(testClient("name@domain.com", 10))

		when: "registering by the same email"
		def testUserDto = TestHelper.testUserDto("name@domain.com", 40)
		clientService.registerClient(testUserDto)

		then: "registration results in exception"
		thrown(UserAlreadyExistException)
	}

	def "client service provides authorization info"() {
		given: "repository containing some user"
		def testClient = testClient("name@domain.com", 12)
		entityManager.persist(testClient)

		when: "getting authorization information for the given email"
		def userDetails = clientService.loadUserByUsername(testClient.email)

		then: "corresponding info should be returned"
		userDetails.username == testClient.email
		userDetails.password == testClient.password
	}

	def "client service cannot provide authorization for wrong user"() {
		given: "repository containing one user"
		def testClient = testClient("name@domain.com", 13)
		entityManager.persist(testClient)

		when: "getting authorization information for different email"
		clientService.loadUserByUsername(testClient.email + "?")

		then: "authorization process results in exception"
		thrown(UsernameNotFoundException)
	}

	def "client service returns all clients"() {
		given: "repository containing users"
		def testClient1 = testClient("name1@domain.com", 11)
		def testClient2 = testClient("name2@domain.com", 14)
		def testClient3 = testClient("name3@domain.com", 15)
		entityManager.persist(testClient1)
		entityManager.persist(testClient2)
		entityManager.persist(testClient3)

		when: "querying all users"
		List<Client> list = clientService.findAll()

		then: "the list is the length of registered users"
		list.size() == 3
		list.find { it -> it.email == "name2@domain.com" } == testClient2
	}

	def "client service can find user by id"() {
		given: "repository containing some user"
		def testClient = testClient("name@domain.com", 16)
		def id = entityManager.persistAndGetId(testClient)

		when: "querying user by specific id"
		Client client = clientService.findById(id as Long)

		then: "correct user is returned"
		client == testClient
	}

	def "client service can find user by email"() {
		given: "repository containing some user"
		def testClient = testClient("name@domain.com", 17)
		entityManager.persist(testClient)

		when: "querying user by specific email"
		Client client = clientService.findByEmail(testClient.email)

		then: "correct user is returned"
		client == testClient
	}

	@SuppressWarnings("GroovyPointlessBoolean")
	def "client service knows if user already exists"() {
		given: "repository containing some user"
		def testClient = testClient("name@domain.com", 18)
		entityManager.persist(testClient)

		expect: "the existence of that user can be easily verified"
		clientService.userExists(email) == expected

		where:
		email             || expected
		"name@domain.com" || true
		"nonexistent@a.a" || false
	}

	def "client service can delete user"() {
		given: "repository containing some user"
		def testClient = testClient("name@domain.com", 19)
		entityManager.persist(testClient)

		when: "the user deletion is invoked"
		clientService.delete(testClient)

		then: "the user shouldn't exist anymore"
		!clientService.userExists(testClient.email)
		clientService.findByEmail(testClient.email) == null
	}

	def "client service returns credit info"() {
		when: "credit info for certain user is queried"
		def creditInfo = clientService.getCreditInfoFor(client)

		then: "credit limit is calculated correctly"
		creditInfo.limit == expectedLimit

		where:
		client << [testClient("name1@domain.com", 34),
				   testClient("name2@domain.com", 51),
				   testClient("name3@domain.com", 69),
				   testClient("name3@domain.com", 70),
				   testClient("name4@domain.com", 71)]

		expectedLimit << [3980, 5680, 7480, 0, 0]
	}

	def testClient(String email, int age) {
		LocalDate ld = LocalDate.now().minusYears(age)
		Date birthDate = Date.valueOf(ld)

		if (passwordEncoder == null) {
			passwordEncoder = new BCryptPasswordEncoder()
		}

		return new Client(
				"Name",
				"Surname",
				email,
				passwordEncoder.encode("p"),
				"555-555-888",
				birthDate,
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

		@Bean
		BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder()
		}

	}
}
