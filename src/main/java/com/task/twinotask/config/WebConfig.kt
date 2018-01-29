package com.task.twinotask.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
class WebConfig : WebMvcConfigurerAdapter() {

	override fun addViewControllers(registry: ViewControllerRegistry) {
		super.addViewControllers(registry)

		registry.addViewController("/index").setViewName("index")
		registry.addViewController("/login").setViewName("login")
	}

}
