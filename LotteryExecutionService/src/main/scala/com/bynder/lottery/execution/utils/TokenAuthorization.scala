package com.bynder.lottery.executions.utils

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{complete, optionalHeaderValueByName, provide}
import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import com.bynder.lottery.exectuion.utils.Logging

object TokenAuthorization extends Logging {
  private val secretKey = "689b49fe-4604-42c5-9f81-dd2a4f0d5566"
  private val header = JwtHeader("HS256")
  private val tokenExpiryPeriodInDays = 1

  
  def authenticated(userId:String): Directive1[Map[String, Any]] = {

    log.info(s"Validating user ${userId}")
    optionalHeaderValueByName("Authorization").flatMap { tokenFromUser =>
      try {
        val jwtToken = tokenFromUser.get.split(" ")
        jwtToken(1) match {
          case token if isTokenExpired(token) =>
            complete(StatusCodes.Unauthorized -> "Session expired.")
  
          case token if JsonWebToken.validate(token, secretKey) =>
            provide(getClaims(token))
            
          case _ =>  complete(StatusCodes.Unauthorized ->"Invalid Token")
        }
      }
      catch {
        case e:Exception => complete(StatusCodes.Unauthorized ->"No Token supplied")
      }
    }
  }
  
  def autorize(tokenMap : Map[String,Any]):Boolean = {
    tokenMap.getOrElse("roleType", "User").asInstanceOf[String].equalsIgnoreCase("Admin")
  }

  private def isTokenExpired(jwt: String): Boolean =
    getClaims(jwt).get("expiredAt").exists(_.toLong < System.currentTimeMillis())

  private def getClaims(jwt: String): Map[String, String] =
    JsonWebToken.unapply(jwt) match {
      case Some(value) => value._2.asSimpleMap.getOrElse(Map.empty[String, String])
      case None => Map.empty[String, String]

    }
}