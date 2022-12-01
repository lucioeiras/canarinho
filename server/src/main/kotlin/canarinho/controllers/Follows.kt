package canarinho.controllers

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

import org.springframework.http.ResponseEntity

import canarinho.models.Follows
import canarinho.models.User

import canarinho.services.FollowsService

@RequestMapping("/user")
@RestController
class FollowsController(val service: FollowsService) {
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
