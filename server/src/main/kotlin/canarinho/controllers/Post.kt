package canarinho.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.http.ResponseEntity

import canarinho.models.Post
import canarinho.services.PostService
import canarinho.interfaces.Controller
import canarinho.interfaces.BodyRequest

class PostBody(val authorId: String?, val content: String)

@CrossOrigin("http://localhost:5173/")
@RequestMapping("/post")
@RestController
class PostController(val service: PostService) : Controller {
	@GetMapping
	override fun index() = service.getPosts()

	@GetMapping("/{id}")
	override fun find(@PathVariable id: String) = service.getPostsById(id)

  @GetMapping("/user/{authorId}")
	fun findByAuthor(@PathVariable authorId: String) =
    service.getPostsByAuthor(authorId)

	@PostMapping
	override fun create(
    @RequestBody body: BodyRequest
  ): ResponseEntity<String> {
    if (body.post != null) {
      try {
        service.createPost(body.post.authorId ?: "", body.post.content)
		    return ResponseEntity.status(201).body("Post created succesfully")
      } catch (e: Exception) {
        return ResponseEntity.status(404).body(e.message)
      }
    }

    return ResponseEntity.status(500).body("Request Body error")
	}

  @PutMapping("/{id}")
	override fun update(
		@PathVariable id: String, @RequestBody body: BodyRequest
	): ResponseEntity<String> {
    if (body.post != null) {
      try {
        service.editPost(id, body.post.content)
        return ResponseEntity.status(203).body("Post updated succesfully")
      } catch (e: Exception) {
        return ResponseEntity.status(404).body(e.message)
      }
    }

    return ResponseEntity.status(500).body("Request Body error")
	}

  @DeleteMapping("/{id}")
	override fun delete(@PathVariable id: String): ResponseEntity<String> {
    service.deletePost(id)
    return ResponseEntity.status(204).body("Post created succesfully")
  }
}
