package com.task.twinotask.service

import com.task.twinotask.web.validator.RegistrationFormValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification
import spock.lang.Subject
import spock.mock.DetachedMockFactory

@SpringBootTest
class ValidatorSpec extends Specification {

	@Subject
	@Autowired
	RegistrationFormValidator validator

	@Autowired
	ClientService clientService

	def "validator reports duplicated email"() {
		given: "some users already exists"
		clientService.userExists(_ as String) >> true

		when: "validating registration with reused email"
		def testUserDto = TestHelper.testUserDto("name1@domain.com", 20)
		def errors = new BeanPropertyBindingResult(testUserDto, "testUserDto")
		validator.validate(testUserDto, errors)

		then: "validator should report that email is already used"
		errors.hasErrors()
		errors.getFieldError("email").rejectedValue == testUserDto.email
	}

	def "validator reports underage user"() {
		given: "user with this email can be registered"
		clientService.userExists(_ as String) >> false

		when: "validating registration with < AGE_LIMIT"
		def testUserDto = TestHelper.testUserDto("name1@domain.com", 19)
		def errors = new BeanPropertyBindingResult(testUserDto, "testUserDto")
		validator.validate(testUserDto, errors)

		then: "validator should report that user is underage"
		errors.hasErrors()
		errors.getFieldError("birthDate").rejectedValue == testUserDto.birthDate
	}

	def "validator reports correct registration data"() {
		given: "user with good correct age and unused email"
		clientService.userExists(_ as String) >> false
		def testUserDto = TestHelper.testUserDto("name1@domain.com", 20)

		when: "registration is validated"
		def errors = new BeanPropertyBindingResult(testUserDto, "testUserDto")
		validator.validate(testUserDto, errors)

		then: "there should be no validation errors"
		!errors.hasErrors()
	}

	@TestConfiguration
	static class MockConfig {
		def detachedMockFactory = new DetachedMockFactory()

		@Bean
		ClientService clientService() {
			return detachedMockFactory.Stub(ClientService)
		}

		@Bean
		RegistrationFormValidator registrationFormValidator() {
			return new RegistrationFormValidator(clientService())
		}
	}
}
