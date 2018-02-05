package com.task.twinotask.service

import com.task.twinotask.entity.Client
import com.task.twinotask.entity.ProfileVisibility
import com.task.twinotask.entity.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import java.sql.Date
import java.time.LocalDate

@SpringBootTest
class ClientsInfoControllerSpec extends Specification {

	@Autowired
	ClientService clientService

	@Autowired
	RestTemplate mockRestTemplate

	@Autowired
	WebApplicationContext context

	MockMvc mvc

	@Autowired
	BCryptPasswordEncoder passwordEncoder

	def setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build()
	}

	@WithMockUser(username = "somename@domain.com")
	def "rest service fetches logged in user info"() {
		setup:
		def testClient = testClient("somename@domain.com", 33)

		when: "user's info can be retrieved"
		mockRestTemplate.getForEntity(!null, !null) >> ResponseEntity.ok(testClient)

		then: "logged in user info is correct"
		mvc.perform(MockMvcRequestBuilders.get("/me"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath('$.email').value(testClient.email))
				.andExpect(MockMvcResultMatchers.jsonPath('$.firstName').value(testClient.firstName))
				.andExpect(MockMvcResultMatchers.jsonPath('$.lastName').value(testClient.lastName))
				.andExpect(MockMvcResultMatchers.jsonPath('$.liabilities').value(testClient.liabilities))
				.andReturn()
	}

	@WithMockUser(username = "name9@domain.com")
	def "rest service fetches credit info"() {
		given: "credit info and user can be retrieved"
		clientService.findById(_ as Long) >> client
		clientService.getCreditInfoFor(_ as Client) >> creditLimit

		expect: "user's credit info is correct"
		mvc.perform(MockMvcRequestBuilders.get("/credit-limit?id=0"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath('$.limit').value(creditLimit.limit))
				.andReturn()

		where:
		client << [testClient("name1@domain.com", 34),
				   testClient("name2@domain.com", 51),
				   testClient("name3@domain.com", 69),
				   testClient("name3@domain.com", 70),
				   testClient("name4@domain.com", 71)]

		creditLimit << [new CreditInfo(0, 3980),
						new CreditInfo(0, 5680),
						new CreditInfo(0, 7480),
						new CreditInfo(0, 0),
						new CreditInfo(0, 0)]
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
		def detachedMockFactory = new DetachedMockFactory()

		@Bean
		ClientService clientService() {
			return detachedMockFactory.Stub(ClientService)
		}

		@Bean
		RestTemplate restTemplate() {
			return detachedMockFactory.Stub(RestTemplate)
		}

		@Bean
		BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder()
		}

	}
}
