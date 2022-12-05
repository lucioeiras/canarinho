package canarinho.services

import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query

import org.springframework.security.crypto.bcrypt.BCrypt

import java.util.UUID
import java.sql.ResultSet

import canarinho.models.User
import canarinho.models.Follows

class FollowsQueryResponse(val user: User, val friends: Boolean)

@Service
class UserService(val db: JdbcTemplate) {
	fun findUsers(): List<User> =
    db.query("SELECT p.*, u.email, u.password FROM Users AS u, Profiles AS p WHERE u.profile_id = p.id") {
      response, _ -> userQueryCallback(response)
    }

  fun findUserById(id: String): List<User> =
    db.query("SELECT p.*, u.email, u.password FROM Users AS u, Profiles AS p WHERE u.profile_id = p.id AND p.id = ?", id) {
      response, _ -> userQueryCallback(response)
    }

  fun createUser(user: User): User {
    db.update(
      "INSERT INTO Profiles VALUES ( ?, ? )",
      user.id, user.name
    )

    db.update(
      "INSERT INTO Users VALUES ( ?, ?, ? )",
      user.email, encryptPassword(user.password), user.id
    )

    return findUserById(user.id ?: "")[0]
  }

  fun editUser(id: String?, user: User): User {
    if (findUserById(id ?: "").isEmpty() == true) {
      throw Exception("User not found")
    }

    db.update(
      "UPDATE Users SET email = ?, password = ? WHERE profile_id = ?",
      user.email, encryptPassword(user.password), id
    )

    db.update(
      "UPDATE Profiles SET name = ? WHERE id = ?",
      user.name, id
    )

    return findUserById(id ?: "")[0]
  }

  fun deleteUser(id: String): Boolean {
    if (findUserById(id).isEmpty() == true) {
      throw Exception("User not found")
    }

    db.update("DELETE FROM Profiles WHERE id = ?", id)
    return true
  }

  private fun encryptPassword(password: String): String =
    BCrypt.hashpw(password, BCrypt.gensalt())

  fun listFollowers(userId: String): List<FollowsQueryResponse> =
    db.query(
      "SELECT p1.*, u1.email, u1.password, f.friends FROM Users AS u1, Profiles AS p1, Profiles AS p2, Follows AS f WHERE f.followed_id = ? AND f.follower_id = p1.id AND f.followed_id = p2.id AND p1.id = u1.profile_id", userId
    ) {
      response, _ -> followsQueryCallback(response)
    }

  fun listFollowing(userId: String): List<FollowsQueryResponse> =
    db.query(
      "SELECT p2.*, u2.email, u2.password, f.friends FROM Users AS u2, Profiles AS p1, Profiles AS p2, Follows AS f WHERE f.follower_id = ? AND f.follower_id = p1.id AND f.followed_id = p2.id AND p2.id = u2.profile_id", userId
    ) {
      response, _ -> followsQueryCallback(response)
    }

  fun follow(users: Follows): Boolean {
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

    return true
  }

  fun unfollow(users: Follows): Boolean {
    db.update(
      "DELETE FROM Follows WHERE follower_id = ? AND followed_id = ?",
      users.followerId, users.followedId
    )

    return true
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
