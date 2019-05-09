package com.boclips.apidocs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
class ApiDocsApplication

fun main(args: Array<String>) {
	runApplication<ApiDocsApplication>(*args)
}

@Controller
class MainController {
	@GetMapping("/")
	fun index() = "redirect:/docs/api-guide.html"
}
