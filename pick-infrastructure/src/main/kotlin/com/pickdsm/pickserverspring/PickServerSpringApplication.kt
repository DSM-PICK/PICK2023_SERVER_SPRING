package com.pickdsm.pickserverspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PickServerSpringApplication

fun main(args: Array<String>) {
	runApplication<PickServerSpringApplication>(*args)
}
