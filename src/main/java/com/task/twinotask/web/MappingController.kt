package com.task.twinotask.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

// TODO: Change with ViewResolver
@Controller
class MappingController {

	@GetMapping("/login")
	fun login(model: Model) = "login"

	@GetMapping("/", "index")
	fun homepage(model: Model) = "index"

}
