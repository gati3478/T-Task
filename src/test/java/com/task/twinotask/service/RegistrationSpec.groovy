package com.task.twinotask.service

import com.task.twinotask.web.ClientRegistrationController
import com.task.twinotask.web.validator.RegistrationFormValidator
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@WebMvcTest(ClientRegistrationController)
class RegistrationSpec extends Specification {

	@TestConfiguration
	static class MockConfig {
		def detachedMockFactory = new DetachedMockFactory()

		@Bean
		ClientService clientService() {
			return detachedMockFactory.Stub(ClientService)
		}

		@Bean
		RegistrationFormValidator registrationFormValidator() {
			return detachedMockFactory.Stub(RegistrationFormValidator)
		}
	}
}
