package com.task.twinotask.web

import com.task.twinotask.entity.Client
import com.task.twinotask.service.ClientService
import com.task.twinotask.util.DateUtils
import com.task.twinotask.web.dto.CreditInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class ClientRestController(private val clientService: ClientService) {

	@GetMapping(value = ["/find-all"])
	fun findAll() = clientService.findAll()

	@GetMapping(value = ["/find-by-id"])
	fun findById(@RequestParam("id") id: Long) = clientService.findById(id)

	@GetMapping(value = ["/find-by-email"])
	fun findByEmail(@RequestParam("email") email: String) = clientService.findByEmail(email)

	@GetMapping(value = ["/me"])
	fun getLoggedInUser(principal: Principal): Client? {
		val email = principal.name
		return clientService.findByEmail(email)
	}

	@GetMapping(value = ["/credit-limit"])
	fun getCreditLimit(@RequestParam("id") id: Long): CreditInfo {
		val client = clientService.findById(id)

		// User's age:
		val age = DateUtils.yearsSince(client!!.birthDate)
		var creditLimit = 0
		if (age < MAX_AGE_LIMIT) {
			creditLimit = age * 100 + client.salary - client.liabilities
		}

		return CreditInfo(client.id!!, creditLimit)
	}

	companion object {
		const val MAX_AGE_LIMIT = 70
	}

}
