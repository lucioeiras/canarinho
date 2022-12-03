package canarinho.services

import org.springframework.stereotype.Service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query

import java.sql.ResultSet

import canarinho.models.Follows
import canarinho.models.User

import canarinho.utils.Response

class FollowsQueryResponse(val user: User, val friends: Boolean)

@Service
class FollowsService(val db: JdbcTemplate) {
  fun listFollowers(userId: String): List<FollowsQueryResponse> =
    db.query(
      "SELECT u1.*, f.friends FROM Users AS u1, Users AS u2, Follows AS f WHERE f.followed_id = ? AND f.follower_id = u1.id AND f.followed_id = u2.id", userId
    ) {
      response, _ -> queryCallback(response)
    }

  fun listFollowing(userId: String): List<FollowsQueryResponse> =
    db.query(
      "SELECT u2.*, f.friends FROM Users AS u1, Users AS u2, Follows AS f WHERE f.follower_id = ? AND f.follower_id = u1.id AND f.followed_id = u2.id", userId
    ) {
      response, _ -> queryCallback(response)
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

  private fun queryCallback(response: ResultSet): FollowsQueryResponse {
    val user = User(
      response.getString("id"),
      response.getString("name"),
      response.getString("email"),
      response.getString("password"),
    )

    return FollowsQueryResponse(user, response.getString("friends").toBoolean())
  }
}
