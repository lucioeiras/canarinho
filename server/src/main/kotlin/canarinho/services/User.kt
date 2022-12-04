package canarinho.services

import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query

import org.springframework.security.crypto.bcrypt.BCrypt

import java.util.UUID
import java.sql.ResultSet

import canarinho.models.User
import canarinho.models.Follows

import canarinho.utils.Response

class FollowsQueryResponse(val user: User, val friends: Boolean)

@Service
class UserService(val db: JdbcTemplate) {
	fun findUsers(): List<User> =
    db.query("SELECT * FROM Users") {
      response, _ -> userQueryCallback(response)
    }

  fun findUserById(id: String): List<User> =
    db.query("SELECT * FROM Users WHERE id = ?", id) {
      response, _ -> userQueryCallback(response)
    }

  fun createUser(user: User): Response {
    val id = user.id ?: UUID.randomUUID().toString()

    db.update(
      "INSERT INTO Users VALUES ( ?, ?, ?, ? )",
      id, user.name, user.email, encryptPassword(user.password)
    )

    return Response(201, "User created succesfully")
  }

  fun editUser(id: String?, user: User): Response {
    if (findUserById(id ?: "").isEmpty() == true) {
      return Response(404, "User not found")
    }

    db.update(
      "UPDATE Users SET name = ?, email = ?, password = ? WHERE id = ?",
      user.name, user.email, encryptPassword(user.password), id
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

  fun listFollowers(userId: String): List<FollowsQueryResponse> =
    db.query(
      "SELECT u1.*, f.friends FROM Users AS u1, Users AS u2, Follows AS f WHERE f.followed_id = ? AND f.follower_id = u1.id AND f.followed_id = u2.id", userId
    ) {
      response, _ -> followsQueryCallback(response)
    }

  fun listFollowing(userId: String): List<FollowsQueryResponse> =
    db.query(
      "SELECT u2.*, f.friends FROM Users AS u1, Users AS u2, Follows AS f WHERE f.follower_id = ? AND f.follower_id = u1.id AND f.followed_id = u2.id", userId
    ) {
      response, _ -> followsQueryCallback(response)
    }

  fun follow(users: Follows): Response {
    val user1Followers = listFollowers(users.followerId)
    var isFriends = false

    for (follower in user1Followers) {
      if (follower.user.id == users.followedId) {
        isFriends = true

        db.update(
          "UPDATE Follows SET friends = ? WHERE follower_id = ? AND followed_id = ?",
          true, users.followedId, users.followerId
        )
      }
    }

    db.update(
      "INSERT INTO Follows VALUES ( ?, ?, ? )",
      users.followerId, users.followedId, isFriends
    )

    return Response(200, "Followed")
  }

  fun unfollow(users: Follows): Response {
    db.update(
      "DELETE FROM Follows WHERE follower_id = ? AND followed_id = ?",
      users.followerId, users.followedId
    )

    return Response(200, "Unfollowed")
  }

  private fun userQueryCallback(response: ResultSet): User = User(
    response.getString("id"),
    response.getString("name"),
    response.getString("email"),
    response.getString("password"),
  )

  private fun followsQueryCallback(response: ResultSet): FollowsQueryResponse {
    val user = User(
      response.getString("id"),
      response.getString("name"),
      response.getString("email"),
      response.getString("password"),
    )

    return FollowsQueryResponse(user, response.getString("friends").toBoolean())
  }
}
