package com.task.twinotask.web

import com.task.twinotask.entity.Client
import com.task.twinotask.service.ClientService
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

	@GetMapping("/find-by-id")
	fun findByIdNew(@RequestParam("id") id: Long): ResponseEntity<Client> =
		clientService.findById(id)?.let {
			ResponseEntity.ok(it)
		} ?: ResponseEntity.notFound().build<Client>()

	@GetMapping("/find-by-email")
	fun findByEmail(@RequestParam("email") email: String): ResponseEntity<Client> =
		clientService.findByEmail(email)?.let {
			ResponseEntity.ok(it)
		} ?: ResponseEntity.notFound().build<Client>()

	@GetMapping("/me")
	fun getLoggedInUser(principal: Principal): ResponseEntity<Client> {
		val email = principal.name
		return restTemplate.getForEntity(
			"http://localhost:8080/find-by-email?email=$email",
			Client::class.java
		)
	}

	@GetMapping("/credit-limit")
	fun getCreditLimit(@RequestParam("id") id: Long) =
		clientService.findById(id)?.let {
			ResponseEntity.ok(clientService.getCreditInfoFor(it))
		} ?: ResponseEntity.notFound().build()

}
