package com.bynder.lottery.user.registration.model.db

import slick.lifted.ProvenShape
import slick.jdbc.H2Profile.api._
import com.bynder.lottery.user.registration.model.entity.User
import com.bynder.lottery.user.registration.model.entity.Role
import com.bynder.lottery.user.registration.model.entity.RoleType

class UserTable(tag: Tag) extends Table[User](tag, None, "Users") {
  
    def id: Rep[Int] = column[Int]("id",O.AutoInc, O.PrimaryKey)
    def userId: Rep[String] = column[String]("userId")
    def password: Rep[String] = column[String]("password")
    def firstName: Rep[Option[String]] = column[String]("firstName")
    def lastName: Rep[Option[String]] = column[String]("lastName")
    def mobileNumber: Rep[String] = column[String]("mobileNumber")
    def idx = index("idx_a", (userId), unique = true)
    val role_id : Rep[Int] =  column[Int]("role_id")
    def roleFk = 
      foreignKey("role_FK", role_id, TableQuery[RoleTable])(_.id, ForeignKeyAction.Restrict, ForeignKeyAction.Cascade)
      

    def * : ProvenShape[User] =
      (id, userId,password, firstName,lastName,mobileNumber,role_id) <> (User.tupled, User.unapply)

    val userQuery: TableQuery[UserTable] = TableQuery[UserTable]
  
}

class RoleTable(tag: Tag) extends Table[Role](tag, None, "Roles") {
    def id: Rep[Int] = column[Int]("id",O.AutoInc, O.PrimaryKey)
    def roleType: Rep[String] = column[String]("roleType")

    def * : ProvenShape[Role] =
      (id, roleType) <> (Role.tupled, Role.unapply)

  val roleQuery: TableQuery[RoleTable] = TableQuery[RoleTable]

}