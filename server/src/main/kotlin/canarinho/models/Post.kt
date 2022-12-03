package canarinho.models

import java.sql.Timestamp

data class Post(
  val id: String?,
  val authorId: String,
  val content: String,
  val createdAt: Timestamp,
  val updatedAt: Timestamp,
)
