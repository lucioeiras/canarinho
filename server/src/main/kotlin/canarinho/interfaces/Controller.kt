package canarinho.interfaces

import org.springframework.http.ResponseEntity

import canarinho.models.User
import canarinho.controllers.PostBody

class BodyRequest (val user: User?, val post: PostBody?)

interface Controller {
  abstract fun index(): List<Any>
  abstract fun find(id: String): List<Any>
  abstract fun create(body: BodyRequest): ResponseEntity<String>
  abstract fun update(id: String, body: BodyRequest): ResponseEntity<String>
  abstract fun delete(id: String): ResponseEntity<String>
}
