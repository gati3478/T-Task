package com.task.twinotask.service

import com.task.twinotask.entity.Client
import com.task.twinotask.entity.ProfileVisibility
import com.task.twinotask.entity.Role
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
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
import java.util.stream.Collectors

@SpringBootTest
@AutoConfigureMockMvc
class ClientsInfoControllerSpec extends Specification {

	@Autowired
	ClientService clientService

	@Autowired
	WebApplicationContext context

	@Autowired
	RestTemplate mockRestTemplate

	MockMvc mvc

	@Autowired
	BCryptPasswordEncoder passwordEncoder

	def setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build()
	}


	@WithMockUser(username = "name9@domain.com", password = "p")
	def "rest service fetches logged in user info"() {
		given: "user is logged in and corresponding info can be returned"
		def testClient = testClient("name9@domain.com")
		mockRestTemplate.getForEntity(!null, !null) >> ResponseEntity.ok(testClient)

		expect: "logged in user info is correct"
		def res = mvc.perform(MockMvcRequestBuilders.get("/me"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(testClient.email)))
				.andReturn()
		//println res.getResponse().getContentAsString()
	}

	def testUser(String email) {
		return new User(
				email,
				passwordEncoder.encode("p"),
				Collections.singletonList(new Role("ROLE_USER", 0)).stream().
						map({ role -> new SimpleGrantedAuthority(role.name) })
						.collect(Collectors.toList())
		)
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
