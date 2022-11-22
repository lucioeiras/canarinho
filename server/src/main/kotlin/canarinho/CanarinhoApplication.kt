package canarinho

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CanarinhoApplication

fun main(args: Array<String>) {
	runApplication<CanarinhoApplication>(*args)
}
