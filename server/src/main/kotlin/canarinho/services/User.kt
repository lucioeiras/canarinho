package canarinho.services

import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query

import org.springframework.security.crypto.bcrypt.BCrypt

import java.util.UUID
import java.sql.ResultSet

import canarinho.models.User
import canarinho.utils.Response

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

  fun createUser(user: User): Response {
    val id = user.id ?: UUID.randomUUID().toString()

    db.update(
      "INSERT INTO Users VALUES ( ?, ?, ?, ? )",
      id, user.name, user.email, encryptPassword(user.password)
    )

    return Response(201, "User created succesfully")
  }

  fun editUser(user: User): Response {
    if (findUserById(user.id ?: "").isEmpty() == true) {
      return Response(404, "User not found")
    }

    db.update(
      "UPDATE Users SET name = ?, email = ?, password = ? WHERE id = ?",
      user.name, user.email, encryptPassword(user.password), user.id
    )

    return Response(204, "User updated succesfully")
  }

  fun deleteUser(id: String): Response {
    if (findUserById(id).isEmpty() == true) {
      return Response(404, "User not found")
    }

    db.update("DELETE FROM Users WHERE id = ?", id)
    return Response(200, "User deleted succesfully")
  }

  private fun encryptPassword(password: String): String =
    BCrypt.hashpw(password, BCrypt.gensalt())

  private fun queryCallback(response: ResultSet): User = User(
    response.getString("id"),
    response.getString("name"),
    response.getString("email"),
    response.getString("password"),
  )
}
