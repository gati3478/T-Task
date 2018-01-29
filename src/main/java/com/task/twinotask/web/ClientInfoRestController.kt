package com.task.twinotask.web

import com.task.twinotask.entity.Client
import com.task.twinotask.service.ClientService
import com.task.twinotask.util.yearsSince
import com.task.twinotask.web.dto.CreditInfo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.security.Principal

@RestController
class ClientInfoRestController(
	private val clientService: ClientService,
	private val restTemplate: RestTemplate
) {

	@GetMapping("/find-all")
	fun findAll() = clientService.findAll()

	@GetMapping(value = ["/find-by-id"])
	fun findById(@RequestParam("id") id: Long): ResponseEntity<Client> {
		val client = clientService.findById(id)
		return if (client != null) {
			ResponseEntity.ok(client)
		} else {
			ResponseEntity.notFound().build<Client>()
		}
	}

	@GetMapping("/find-by-email")
	fun findByEmail(@RequestParam("email") email: String): ResponseEntity<Client> {
		val client = clientService.findByEmail(email)
		return if (client != null) {
			ResponseEntity.ok(client)
		} else {
			ResponseEntity.notFound().build<Client>()
		}
	}

	@GetMapping("/me")
	fun getLoggedInUser(principal: Principal): ResponseEntity<Client> {
		val email = principal.name
		return restTemplate.getForEntity("http://localhost:8080/find-by-email?email=$email", Client::class.java)
	}

	@GetMapping("/credit-limit")
	fun getCreditLimit(@RequestParam("id") id: Long): ResponseEntity<CreditInfo> {
		val client = clientService.findById(id)

		if (client != null) {
			// User's age:
			val age = client.birthDate.yearsSince()
			var creditLimit = 0
			if (age < MAX_AGE_LIMIT) {
				creditLimit = age * 100 + client.salary - client.liabilities
			}

			return ResponseEntity.ok(CreditInfo(client.id, creditLimit))
		} else {
			return ResponseEntity.notFound().build()
		}
	}

	companion object {
		const val MAX_AGE_LIMIT = 70
	}

}
