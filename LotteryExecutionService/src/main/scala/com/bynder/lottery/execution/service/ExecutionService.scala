package com.bynder.lottery.execution.service

import java.util.concurrent.ConcurrentHashMap
import com.bynder.lottery.execution.model.entity.Lottery
import com.bynder.lottery.execution.model.db.LotteryExecutionDao
import com.bynder.lottery.execution.model.db.Connection
import scala.concurrent._
import ExecutionContext.Implicits.global
import com.bynder.lottery.execution.utils.Serializer
import com.bynder.lottery.execution.utils.ResultSetToLotteryCaseClass
import scala.collection.JavaConversions._
import com.bynder.lottery.execution.model.entity.LotteryTicket
import com.bynder.lottery.execution.utils.HashGenerator
import scala.concurrent.duration._
import scala.util.Success
import java.util.UUID
import com.bynder.lottery.execution.utils.DateTimeUtil
import com.bynder.lottery.exectuion.utils.Logging
import scala.util.Failure
import akka.http.scaladsl.server.RequestContext
import akka.actor.Actor
import akka.util.Timeout
import com.bynder.lottery.execution.model.entity.SubmitLotteryRequest
import java.util.function.BiFunction
import com.bynder.lottery.execution.utils.ResultSetToLotteryTicketCaseClass
import scala.util.Random

object ExecutionService extends Logging{
  
  val activeLotteryDetails = new ConcurrentHashMap[String,Lottery]
  
  var lotterySpecificCounter = new ConcurrentHashMap[String,Int]
  
  val lotteryResult = new ConcurrentHashMap[String,String]
  
  
  def getActiveLotteries :Future[List[String]]= {
    
    if (activeLotteryDetails.isEmpty()){
      new LotteryExecutionDao(Connection.session).getAllActiveLotteries.map(x => 
        {
         val values = ResultSetToLotteryCaseClass.convert(x) 
         values.map(lottery => {
           activeLotteryDetails.putIfAbsent(lottery.name, lottery)
         })
         
         values.map(_.name).mkString
       })
    }
    Future { activeLotteryDetails.keys().toList }
  }
  
  def getLotteryWinner(date:String,name:String):Future[String] = {
    if (!activeLotteryDetails.containsKey(name)) {
        if (!lotteryResult.containsKey(s"${name}_${date}")) {
         new LotteryExecutionDao(Connection.session).getLotteryWinner(date,name).map(x => {
           if (x.toList.nonEmpty){
             lotteryResult.put(s"${name}_${date}", x.one().getString("winningLottery"))
             x.one().getString("winningLottery") 
           }
           else s"No winning lottery present for given input $date - $name"
           } )
        }
        else Future { lotteryResult.get(s"${name}_${date}") }
    }
    else Future { s"currently given lottery input $date - $name is active" }
  }
  
  
  def submitLottery(userId:String,name:String) = {
    if (activeLotteryDetails.containsKey(name)) {
      val counter = lotterySpecificCounter.compute(name, (key,oldValue) => oldValue+1)
      val id = HashGenerator.generateHash(counter)
      val lotteryId = activeLotteryDetails.get(name).id    
      new LotteryExecutionDao(Connection.session).insertLotteryEvent(LotteryTicket(id,lotteryId,userId,java.time.LocalDate.now.toString())) 
      id
    }
    else "No active lottery present with the given input."
  }
  
  
  def generateLotteryEvent(name:String):String = {
    if (!activeLotteryDetails.containsKey(name)) {
      val id = UUID.randomUUID().toString()
      val lottery = Lottery(id,name,java.time.LocalDate.now.toString(),java.time.LocalDate.now.toString())    
      new LotteryExecutionDao(Connection.session).insertLottery(lottery)
      activeLotteryDetails.putIfAbsent(name, lottery) 
      lotterySpecificCounter.putIfAbsent(name, 1000)
      id
    }
    else "Already there is an active lottery with the given name."
  }

  def generateLotteryWinner = {
    
    activeLotteryDetails.keys().map(name => {
      
      new LotteryExecutionDao(Connection.session).getAllActiveLotteryTicketForSpecificLottery(activeLotteryDetails.get(name)).map(x => {
        val resultArray = ResultSetToLotteryTicketCaseClass.convert(x)
        val winningLotteryObj = resultArray(0 + Random.nextInt(resultArray.length-1))
        new LotteryExecutionDao(Connection.session).insertLotteryWinner(activeLotteryDetails.get(name).copy(winningLottery=Some(winningLotteryObj.id))).map(x => {
          activeLotteryDetails.remove(name)
      })
        
      })
      
    })
  }
  
}

case class SubmitLottery(submitLotteryReq:SubmitLotteryRequest)

class ExecutionServiceBase(requestContext:RequestContext) extends Actor {
  
  implicit val system = context.system
  implicit val timeout: Timeout = Timeout(120 seconds)
  
  import system.dispatcher
  
  val config = system.settings.config
  
  def receive = {
    case SubmitLottery(submitLotteryRequest) => 
      try {
        ExecutionService.submitLottery(submitLotteryRequest.name, submitLotteryRequest.userId)
      } catch {
        case e :Throwable => requestContext.fail(e)
      }
      finally {
        context.stop(self)
      }
  }
  
}
