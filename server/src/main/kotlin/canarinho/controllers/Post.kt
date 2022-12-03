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

import canarinho.models.Post
import canarinho.services.PostService
import canarinho.utils.Response

class PostBody(val authorId: String, val content: String)

@RequestMapping("/post")
@RestController
class PostController(val service: PostService) {
	@GetMapping
	fun index() = service.getPosts()

	@GetMapping("/{postId}")
	fun find(@PathVariable postId: String) = service.getPostsById(postId)

  @GetMapping("/user/{authorId}")
	fun findByAuthor(@PathVariable authorId: String) =
    service.getPostsByAuthor(authorId)

	@PostMapping
	fun store(
    @RequestBody body: PostBody
  ): ResponseEntity<String> {
    val response = service.createPost(body.authorId, body.content)
		return ResponseEntity.status(response.status).body(response.message)
	}

  @PutMapping("/{postId}")
	fun update(
		@RequestBody content: String, @PathVariable postId: String
	): ResponseEntity<String> {
		val response = service.editPost(postId, content)
    return ResponseEntity.status(response.status).body(response.message)
	}

  @DeleteMapping("/{postId}")
	fun delete(@PathVariable postId: String): ResponseEntity<String> {
    val response = service.deletePost(postId)
    return ResponseEntity.status(response.status).body(response.message)
  }
}
