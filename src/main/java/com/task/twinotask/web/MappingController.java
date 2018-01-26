package com.task.twinotask.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// TODO: Change with ViewResolver
@SuppressWarnings("unused")
@Controller
public class MappingController {

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping({"/", "index"})
	public String homepage(Model model) {
		return "index";
	}

}
