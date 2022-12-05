package canarinho.services

import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query

import java.util.UUID
import java.sql.ResultSet
import java.sql.Timestamp

import canarinho.models.User
import canarinho.models.Post

class PostQueryResponse(val post: Post, val user: User)

@Service
class PostService(val db: JdbcTemplate) {
	fun getPosts(): List<PostQueryResponse> =
    db.query(
      "SELECT p.*, pr.*, u.email, u.password FROM Users AS u, Posts AS p, Profiles AS pr WHERE p.author_id = pr.id AND pr.id = u.profile_id"
    ) {
      response, _ -> queryCallback(response)
    }

  fun getPostsByAuthor(authorId: String): List<PostQueryResponse> =
    db.query(
      "SELECT p.*, pr.*, u.email, u.password FROM Users AS u, Posts AS p, Profiles AS pr WHERE p.author_id = ? AND pr.id = ? AND pr.id = u.profile_id",
      authorId, authorId
    ) {
      response, _ -> queryCallback(response)
    }

  fun getPostsById(postId: String): List<PostQueryResponse> =
    db.query(
      "SELECT p.*, pr.*, u.email, u.password FROM Posts AS p, Users AS u, Profiles AS pr WHERE p.id = ? AND p.author_id = pr.id AND pr.id = u.profile_id",
      postId
    ) {
      response, _ -> queryCallback(response)
    }

  fun createPost(authorId: String, content: String): Boolean {
    val id = UUID.randomUUID().toString()

    db.update(
      "INSERT INTO Posts VALUES ( ?, ?, ?, ?, ? )",
      id,
      authorId,
      content,
      Timestamp(System.currentTimeMillis()),
      Timestamp(System.currentTimeMillis())
    )

    return true
  }

  fun editPost(postId: String?, content: String): Boolean {
    if (getPostsById(postId ?: "").isEmpty() == true) {
      throw Exception("Post not found!")
    }

    db.update(
      "UPDATE Posts SET content = ?, updated_at = ? WHERE id = ?",
      content, Timestamp(System.currentTimeMillis()), postId
    )

    return true
  }

  fun deletePost(postId: String): Boolean {
    if (getPostsById(postId).isEmpty() == true) {
      throw Exception("Post not found!")
    }

    db.update("DELETE FROM Posts WHERE id = ?", postId)
    return true
  }

  private fun queryCallback(response: ResultSet): PostQueryResponse {
    val post = Post(
      response.getString("id"),
      response.getString("author_id"),
      response.getString("content"),
      Timestamp.valueOf(response.getString("created_at")),
      Timestamp.valueOf(response.getString("updated_at"))
    )

    val user = User(
      response.getString("id"),
      response.getString("name"),
      response.getString("email"),
      response.getString("password"),
    )

    return PostQueryResponse(post, user)
  }
}
