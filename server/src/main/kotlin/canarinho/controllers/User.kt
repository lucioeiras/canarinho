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
import canarinho.models.Follows
import canarinho.services.UserService
import canarinho.utils.Response
import canarinho.interfaces.Controller
import canarinho.interfaces.BodyRequest

@RequestMapping("/user")
@RestController
class UserController(val service: UserService) : Controller {
	@GetMapping
	override fun index(): List<User> = service.findUsers()

	@GetMapping("/{id}")
	override fun find(@PathVariable id: String): List<User> =
		service.findUserById(id)

	@PostMapping
	override fun create(@RequestBody body: BodyRequest): ResponseEntity<String> {
    if (body.user != null) {
			val response = service.createUser(body.user)
			return ResponseEntity.status(response.status).body(response.message)
		}

		return ResponseEntity.status(500).body("Request Body error")
	}

  @PutMapping("/{id}")
	override fun update(
    @PathVariable id: String, @RequestBody body: BodyRequest
  ): ResponseEntity<String> {
		if (body.user != null) {
			val response = service.editUser(id, body.user)
    	return ResponseEntity.status(response.status).body(response.message)
		}

		return ResponseEntity.status(500).body("Request Body error")
	}

  @DeleteMapping("/{id}")
	override fun delete(@PathVariable id: String): ResponseEntity<String> {
    val response = service.deleteUser(id)
    return ResponseEntity.status(response.status).body(response.message)
  }

  @GetMapping("/followers/{id}")
	fun followers(@PathVariable id: String) =
		service.listFollowers(id)

  @GetMapping("/following/{id}")
  fun following(@PathVariable id: String) =
    service.listFollowing(id)

	@PostMapping("/follow")
	fun follow(@RequestBody users: Follows): ResponseEntity<String> {
    val response = service.follow(users)
		return ResponseEntity.status(response.status).body(response.message)
	}

  @DeleteMapping("/unfollow")
  fun unfollow(@RequestBody users: Follows): ResponseEntity<String> {
    val response = service.unfollow(users)
		return ResponseEntity.status(response.status).body(response.message)
	}
}
