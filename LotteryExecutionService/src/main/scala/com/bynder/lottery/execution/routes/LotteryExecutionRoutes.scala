package com.bynder.lottery.execution.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import scala.util.Success
import scala.util.Failure
import scala.concurrent._
import ExecutionContext.Implicits.global
import akka.http.scaladsl.server.Directives._
import com.bynder.lottery.executions.utils.TokenAuthorization
import com.bynder.lottery.exectuion.utils.Logging
import com.bynder.lottery.execution.service.ExecutionService
import org.json4s.DefaultFormats
import com.bynder.lottery.execution.model.entity.Lottery
import org.json4s.JsonAST.JObject
import org.json4s.native.Serialization.{write => jWrite}
import com.bynder.lottery.execution.model.entity.SubmitLotteryRequest

class LotteryExecutionRoutes extends Logging with PlayJsonSupport{
  
  implicit val json4sFormats = DefaultFormats.preservingEmptyValues
  
  val routes: Route =      
    pathPrefix("lottery") {
        path("generateLotteryEvent") {
          get {
            parameters("lotteryName".as[String],"userId".as[String]) { (lotteryName,userId) => 
              TokenAuthorization.authenticated(userId) { tokenMap =>
                authorize(TokenAuthorization.autorize(tokenMap)) { 
                   complete((StatusCodes.OK,ExecutionService.generateLotteryEvent(lotteryName))) 
             }   
            }
           } 
         }
        } ~ path("getActiveLotteries") {
          get {
               complete((StatusCodes.OK,ExecutionService.getActiveLotteries))
          }
       
        } ~ path("getLotteryWinner") {
          get {
            parameters("date".as[String],"name".as[String]) { (date,name) => 
               complete((StatusCodes.OK,ExecutionService.getLotteryWinner(date, name)))
          }
       
        } 
      } ~ path("submitLottery") {
          (post & entity(as[SubmitLotteryRequest])) { submitLotteryRequest =>  
            TokenAuthorization.authenticated(submitLotteryRequest.userId) { tokenMap =>
                complete((StatusCodes.OK,ExecutionService.submitLottery(submitLotteryRequest.userId, submitLotteryRequest.name))) 
             }   
       }
      }     
  }
 
}