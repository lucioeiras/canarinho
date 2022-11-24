package canarinho.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.http.ResponseEntity

import canarinho.models.User
import canarinho.services.UserService

import canarinho.utils.Response

@RequestMapping("/user")
@RestController
class UserController(val service: UserService) {
	@GetMapping
	fun index(): List<User> = service.findUsers()

	@GetMapping("/{id}")
	fun find(@PathVariable id: String): List<User> =
		service.findUserById(id)

	@PostMapping
	fun store(@RequestBody user: User): ResponseEntity<String> {
    val response = service.createUser(user)
		return ResponseEntity.status(response.status).body(response.message)
	}

  @PutMapping
	fun update(@RequestBody user: User): ResponseEntity<String> {
		val response = service.editUser(user)
    return ResponseEntity.status(response.status).body(response.message)
	}

  @DeleteMapping("/{id}")
	fun delete(@PathVariable id: String): ResponseEntity<String> {
    val response = service.deleteUser(id)
    return ResponseEntity.status(response.status).body(response.message)
  }
}
