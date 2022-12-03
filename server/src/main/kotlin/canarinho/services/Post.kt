package canarinho.services

import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query

import java.util.UUID
import java.sql.ResultSet
import java.sql.Timestamp

import canarinho.models.User
import canarinho.models.Post

import canarinho.utils.Response

class PostQueryResponse(val post: Post, val user: User)

@Service
class PostService(val db: JdbcTemplate) {
	fun getPosts(): List<PostQueryResponse> =
    db.query(
      "SELECT p.*, u.* FROM Posts AS p, Users AS u WHERE p.author_id = u.id"
    ) {
      response, _ -> queryCallback(response)
    }

  fun getPostsByAuthor(authorId: String): List<PostQueryResponse> =
    db.query(
      "SELECT p.*, u.* FROM Posts AS p, Users AS u WHERE p.author_id = ? AND u.id = ?",
      authorId, authorId
    ) {
      response, _ -> queryCallback(response)
    }

  fun getPostsById(postId: String): List<PostQueryResponse> =
    db.query(
      "SELECT p.*, u.* FROM Posts AS p, Users AS u WHERE p.id = ? AND p.author_id = u.id",
      postId
    ) {
      response, _ -> queryCallback(response)
    }

  fun createPost(authorId: String, content: String): Response {
    val id = UUID.randomUUID().toString()

    db.update(
      "INSERT INTO Posts VALUES ( ?, ?, ?, ?, ? )",
      id,
      authorId,
      content,
      Timestamp(System.currentTimeMillis()),
      Timestamp(System.currentTimeMillis())
    )

    return Response(201, "Post created succesfully")
  }

  fun editPost(postId: String?, content: String): Response {
    if (getPostsById(postId ?: "").isEmpty() == true) {
      return Response(404, "Post not found")
    }

    db.update(
      "UPDATE Posts SET content = ?, updated_at = ? WHERE id = ?",
      content, Timestamp(System.currentTimeMillis()), postId
    )

    return Response(204, "Post updated succesfully")
  }

  fun deletePost(postId: String): Response {
    if (getPostsById(postId).isEmpty() == true) {
      return Response(404, "Post not found")
    }

    db.update("DELETE FROM Posts WHERE id = ?", postId)
    return Response(200, "Post deleted succesfully")
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
