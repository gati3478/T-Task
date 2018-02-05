package com.task.twinotask.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.task.twinotask.web.ClientRegistrationController
import com.task.twinotask.web.dto.ClientRegistrationDto
import com.task.twinotask.web.validator.RegistrationFormValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@WebMvcTest(controllers = ClientRegistrationController, secure = false)
class RegistrationSpec extends Specification {

	@Autowired
	MockMvc mvc

	@Autowired
	ClientService clientService

	def "controller returns proper registration view"() {
		expect: "registration view is returned"
		mvc.perform(MockMvcRequestBuilders.get("/registration"))
				.andExpect(MockMvcResultMatchers.view().name("registration"))
	}

	def "controller registers a new user"() {
		when: "user is registered"
		def result = mvc.perform(MockMvcRequestBuilders.post("/registration")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJson(testRegistration)))
		then: 
		result
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("login"))
		//3 * clientService.registerClient(testRegistration)

		where:
		testRegistration << [TestHelper.testUserDto("name1@domain.com", 21),
							 TestHelper.testUserDto("name2@domain.com", 32),
							 TestHelper.testUserDto("name3@domain.com", 43)]
	}

	def asJson(ClientRegistrationDto dto) {
		return new ObjectMapper().writeValueAsString(dto)
	}

	@TestConfiguration
	static class MockConfig {
		def detachedMockFactory = new DetachedMockFactory()

		@Bean
		ClientService clientService() {
			return detachedMockFactory.Mock(ClientService)
		}

		@Bean
		RegistrationFormValidator registrationFormValidator() {
			return detachedMockFactory.Mock(RegistrationFormValidator)
		}

	}
}
