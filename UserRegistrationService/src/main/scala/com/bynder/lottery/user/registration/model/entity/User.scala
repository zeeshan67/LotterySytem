package com.bynder.lottery.user.registrations.model.entity

import java.util.Date

sealed case class RoleType(id:Int,value:String)
import play.api.libs.json.{Json, Reads}

object RoleType {
  object Admin extends RoleType(1,"Admin")
  object User extends RoleType(2,"User")
}

case class User(id: Int=0, userId: String,password:String, 
                firstName: Option[String]=None,lastName:Option[String]=None,
                mobileNumber:String,
                role_id:Int=2)

case class UserLoginRequest(userId:String,password:String,roleType:String="User")

                
object UserLoginRequest {
  implicit val loginRequestReads: Reads[UserLoginRequest] = Json.reads[UserLoginRequest]
}

case class CreateUserRequest( userId: String,password:String, 
                firstName: Option[String]=None,lastName:Option[String]=None,
                mobileNumber:String
                )

object CreateUserRequest {
  implicit val CreateUserRequestReads: Reads[CreateUserRequest] = Json.reads[CreateUserRequest]
}

                
case class Role(id :Int=0,roleType:String)
