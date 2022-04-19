package com.bynder.lottery.user.registrations.model.db


import slick.jdbc.JdbcBackend.Database
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future
import com.bynder.lottery.user.registrations.model.entity.User
import com.bynder.lottery.user.registrations.model.entity.Role
import org.mindrot.jbcrypt.BCrypt
import scala.concurrent._
import ExecutionContext.Implicits.global

trait UserRepositoryComponent {
  def add(user: User): Future[Int]
  def update(user: User): Future[Int]
  def deleteBy(userId: String): Future[Int]
  def userLogin(userId:String,password:String):Future[Option[User]]
  def byUserId(userId: String): Future[Option[User]]
  def all(limit: Int, offset: Int): Future[Seq[User]]
}

object UserRepository {

  def apply() =
    new UserRepository

}

class UserRepository extends UserRepositoryComponent {

  lazy val userTblQuery: TableQuery[UserTable] = TableQuery[UserTable]

  override def add(user: User): Future[Int] = Connection.db.run {
    userTblQuery += user.copy(password=hashPassword(user.password))
  }

  override def update(user: User): Future[Int] = Connection.db.run {
    userTblQuery.filter(_.id === user.id).map(b => (b.firstName, b.lastName,b.mobileNumber)).update((user.firstName, user.lastName,user.mobileNumber))
  }

  override def userLogin(userId:String,password:String): Future[Option[User]] = Connection.db.run {
    userTblQuery.filter(_.userId === userId).result.map(_.headOption.filter(user => checkPassword(password,user.password)))
  }

  override def deleteBy(userId: String): Future[Int] = Connection.db.run {
    userTblQuery.filter(_.userId === userId).delete
  }

  override def byUserId(userId: String): Future[Option[User]] = Connection.db.run {
    userTblQuery.filter(_.userId === userId).result.headOption
  }

  override def all(limit: Int, offset: Int): Future[Seq[User]] = Connection.db.run {
    userTblQuery.drop(offset).take(limit).result
  }
  
  def hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(12))

  def checkPassword(password: String, passwordHash: String): Boolean = BCrypt.checkpw(password, passwordHash)

}

object RoleRepository {
  
  lazy val roleTblQuery: TableQuery[RoleTable] = TableQuery[RoleTable]
  
  def add(role: Role): Future[Int] = Connection.db.run {
    roleTblQuery += role
  }
  
  def deleteBy(id: Int): Future[Int] = Connection.db.run {
    roleTblQuery.filter(_.id === id).delete
  }
}