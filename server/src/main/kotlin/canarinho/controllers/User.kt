package canarinho.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

import canarinho.models.User
import canarinho.services.UserService

@RequestMapping("/user")
@RestController
class UserController(val service: UserService) {
	@GetMapping
	fun index(): List<User> = service.findUsers()

	@GetMapping("/{id}")
	fun find(@PathVariable id: String): List<User> =
		service.findUserById(id)

	@PostMapping
	fun store(@RequestBody user: User) {
		service.createUser(user)
	}

  @PutMapping
	fun update(@RequestBody user: User) {
		service.editUser(user)
	}

  @DeleteMapping("/{id}")
	fun delete(@PathVariable id: String){
    service.deleteUser(id)
  }
}
