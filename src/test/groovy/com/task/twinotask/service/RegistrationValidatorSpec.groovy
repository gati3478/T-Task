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
class RegistrationValidatorSpec extends Specification {

	@Subject
	@Autowired
	RegistrationFormValidator validator

	@Autowired
	ClientService clientService

	def "validator reports already used email"() {
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
		given: "user with some email can be registered"
		clientService.userExists(_ as String) >> false

		when: "validating registration of underage"
		def testUserDto = TestHelper.testUserDto("name1@domain.com", 19)
		def errors = new BeanPropertyBindingResult(testUserDto, "testUserDto")
		validator.validate(testUserDto, errors)

		then: "validator should report that user is underage"
		errors.hasErrors()
		errors.getFieldError("birthDate").rejectedValue == testUserDto.birthDate
	}

	def "validator reports bad email and age"() {
		given: "some users already exist"
		clientService.userExists(_ as String) >> true

		when: "registration is validated"
		def errors = new BeanPropertyBindingResult(testUserDto, "testUserDto")
		validator.validate(testUserDto, errors)

		then: "validator should report both bad email and age"
		errors.hasErrors()
		errors.getFieldError("email").rejectedValue == testUserDto.email
		errors.getFieldError("birthDate").rejectedValue == testUserDto.birthDate

		where:
		testUserDto << [TestHelper.testUserDto("name1@domain.com", 1),
						TestHelper.testUserDto("name1@domain.com", 10),
						TestHelper.testUserDto("name1@domain.com", 19),
						TestHelper.testUserDto("name1@domain.com", 0)]
	}

	def "validator reports correct registration data"() {
		given: "user with correct age and unused email"
		clientService.userExists(_ as String) >> false

		when: "registration is validated"
		def errors = new BeanPropertyBindingResult(testUserDto, "testUserDto")
		validator.validate(testUserDto, errors)

		then: "there should be no validation errors"
		!errors.hasErrors()

		where:
		testUserDto << [TestHelper.testUserDto("name1@domain.com", 20),
						TestHelper.testUserDto("name1@domain.com", 22),
						TestHelper.testUserDto("name1@domain.com", 26),
						TestHelper.testUserDto("name1@domain.com", 42)]
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
