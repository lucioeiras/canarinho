package canarinho.services

import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query

import java.util.UUID
import java.sql.ResultSet

import canarinho.models.User

@Service
class UserService(val db: JdbcTemplate) {
	fun findUsers(): List<User> =
    db.query("SELECT * FROM Users") {
      response, _ -> queryCallback(response)
    }

  fun findUserById(id: String): List<User> =
    db.query("SELECT * FROM Users WHERE id = ?", id) {
      response, _ -> queryCallback(response)
    }

  fun createUser(user: User) {
    val id = user.id ?: UUID.randomUUID().toString()
    val password = ""

    db.update(
      "INSERT INTO Users VALUES ( ?, ?, ?, ? )",
      id, user.name, user.email, password
    )
  }

  fun editUser(user: User) {
    val password = ""

    db.update(
      "UPDATE Users SET name = ?, email = ?, password = ? WHERE id = ?",
      user.name, user.email, password, user.id
    )
  }

  fun deleteUser(id: String) {
    if (findUserById(id).isEmpty() == true) {
      return;
    }

    db.update("DELETE FROM Users WHERE id = ?", id)
  }

  private fun queryCallback(response: ResultSet): User = User(
    response.getString("id"),
    response.getString("name"),
    response.getString("email"),
    response.getString("password"),
  )
}
