package com.bynder.lottery.user.registration.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import com.bynder.lottery.user.registration.model.entity.User
import com.bynder.lottery.user.registration.utils.Serializers
import com.bynder.lottery.user.registration.model.entity.UserLoginRequest
import com.bynder.lottery.user.registration.model.db.UserRepository
import scala.util.Success
import scala.util.Failure
import com.bynder.lottery.user.registration.model.entity.RoleType
import scala.concurrent._
import ExecutionContext.Implicits.global
import com.bynder.lottery.user.registration.utils.Logging
import com.bynder.lottery.user.registration.utils.TokenGeneration
import com.bynder.lottery.user.registration.model.entity.CreateUserRequest
import akka.http.scaladsl.server.Directives._

class UserRegistrationRoutes extends PlayJsonSupport with Logging {
  val routes =
      pathPrefix("user") {
        path("login") {
          (post & entity(as[UserLoginRequest])) { userRequest =>  
            {
             onComplete(UserRepository().userLogin(userRequest.userId,userRequest.password)){
              case Success(user) => if (user.nonEmpty) {
                                          log.info(s"User ${userRequest.userId} login successful")
                                          val token = TokenGeneration.generateToken(user.get.userId,userRequest.roleType)
                                          complete((StatusCodes.OK,token))
                                    } 
                                    else {
                                      log.error(s"Invalid credentials")
                                      complete(StatusCodes.Unauthorized,"Invalid credentials.")
                                    }
              case Failure(err) =>  log.error(s"Error - $err")
                                    complete(StatusCodes.InternalServerError,err.getMessage)
            }
             
          }
        }
       } ~ path("register") {
          (post & entity(as[CreateUserRequest])) { createUserRequest =>  
            {
             onComplete(UserRepository().byUserId(createUserRequest.userId)){
              case Success(user) => if (user.nonEmpty)
                                      complete(StatusCodes.BadRequest,"User already registered.")
                                    else {
                                      val token = TokenGeneration.generateToken(createUserRequest.userId,"User")
                                      UserRepository().add(User(userId=createUserRequest.userId,
                                                                password=createUserRequest.password,
                                                                firstName=createUserRequest.firstName,
                                                                lastName=createUserRequest.lastName,
                                                                mobileNumber=createUserRequest.mobileNumber))
                                      complete((StatusCodes.OK,token))
                                    }
              case Failure(err) =>  log.error(s"Error - $err")
                                    complete(StatusCodes.InternalServerError,err.getMessage)
            }
             
          }
        }
       }
      } ~ path("welcome") {
        get {
            complete("Welcome")
          }
        
      } 
}
