package com.bynder.lottery.execution.model.db

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import com.bynder.lottery.execution.utils.HashGenerator
import java.util.concurrent.ConcurrentHashMap
import java.time.format.DateTimeFormatter
import com.bynder.lottery.execution.utils.DateTimeUtil
import java.time.ZoneId
import java.time.LocalDate
import com.bynder.lottery.execution.model.entity.Lottery
import com.bynder.lottery.execution.model.entity.LotteryTicket

object Test extends App {
  
//  Schema.createSchema
val dateFormatter = DateTimeFormatter.ofPattern("yyyyMM")  
println(java.time.LocalDate.now.format(dateFormatter))  

println(DateTimeUtil.convertDatetoMonthYear(Some(java.time.LocalDate.now.toString())))

println(java.time.LocalDate.now.toString())

println(LocalDate.parse(java.time.LocalDate.now.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).minusDays(1).toString())
println(DateTimeUtil.getPreviousDate(Some(java.time.LocalDate.now.toString())))

println(HashGenerator.generateHash(1000000))
import scala.concurrent.duration._
Schema.createSchema
//Await.result(new LotteryExecutionDao(Connection.session).insertLotteryWinner(Lottery("2","ABC","2022-04-19","2022-04-19",Some("ABC"))),5.seconds)
//Await.result(new LotteryExecutionDao(Connection.session).insertLotteryEvent(LotteryTicket("XYZ","2","Test","2022-04-19")),5.seconds)
val x = Await.result(new LotteryExecutionDao(Connection.session).endLotteryAndSelectLotteryWinner(Lottery("2","ABC","2022-04-19","2022-04-19",Some("ABC"))),5.seconds)
//  new LotteryExecutionDao(Connection.session).executeQuery("insert into lottery (id,name,startDate,endDate,winningLottery,shardKey) values ('2','test','2022-05-18','2022-05-18','test','test');") onComplete { 
//    case Success(s) => println("success")
//    case Failure(er) => println(er.getMessage)
//  }
//  new LotteryExecutionDao(Connection.session).executeQuery("insert into lottery_ticket (id,lotteryId,userId,date) values ('3','test','test1','2022-05-18');") onComplete { 
//    case Success(s) => println("success")
//    case Failure(er) => println(er.getMessage)
//  }

}