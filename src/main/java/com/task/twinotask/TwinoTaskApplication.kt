package com.task.twinotask

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TwinoTaskApplication

fun main(args: Array<String>) {
	SpringApplication.run(TwinoTaskApplication::class.java, *args)
}