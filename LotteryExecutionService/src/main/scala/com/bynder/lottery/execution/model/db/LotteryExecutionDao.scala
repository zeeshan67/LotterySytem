package com.bynder.lottery.execution.model.db
import com.datastax.driver.core.querybuilder.{QueryBuilder => QB}
import com.datastax.driver.core.querybuilder.Clause
import com.datastax.driver.core._
import scala.collection.JavaConverters._
import com.google.common.util.concurrent._
import scala.concurrent._
import com.bynder.lottery.execution.utils.Serializer
import com.bynder.lottery.execution.model.entity.Lottery
import com.bynder.lottery.execution.model.entity.LotteryTicket
import com.bynder.lottery.execution.utils.DateTimeUtil

class LotteryExecutionDao(session:Session)(implicit ec: ExecutionContext)  {
  
  implicit def cassandraFutureToScalaFuture(future: ResultSetFuture): Future[ResultSet] = {
    val promise = Promise[ResultSet]()

    val callback = new FutureCallback[ResultSet] {
      def onSuccess(result: ResultSet): Unit = {
        promise success result
      }

      def onFailure(err: Throwable): Unit = {
        promise failure err
      }
    }

    Futures.addCallback(future, callback)
    promise.future
  }
  
  def getAllActiveLotteryTicketForSpecificLottery(lottery:Lottery) = executeQuery(s"select * from lottery_ticket where lotteryId='${lottery.id}' and startDate='${lottery.startDate}' and name='${lottery.name}'")
  
  def getAllActiveLotteries = executeQuery(s"select * from lottery where shardKey='${DateTimeUtil.convertDatetoMonthYear()}' and startDate='${java.time.LocalDate.now().toString()}' and winningLottery='null' ALLOW FILTERING;")
  
  def executeQuery(selectParams:Seq[String],condition:Clause,table:String):Future[ResultSet] = {
    val query = QB.select(selectParams:_*).from(table).where(condition)
    session.executeAsync(query).map(x => x)
  }
  
  def executeQuery(query:String):Future[ResultSet] = {
    session.executeAsync(query).map(x => x)
  }
  
  def checkActiveLotteryWithSameName(name:String) = executeQuery(s"select id from lottery where shardKey='${DateTimeUtil.convertDatetoMonthYear()}' and startDate='${java.time.LocalDate.now().toString()}' and name='${name}'; ")
  
  def checkLotteryIsPassive(name:String) = executeQuery(s"select id from lottery where shardKey='${DateTimeUtil.convertDatetoMonthYear()}' and startDate='${java.time.LocalDate.now().toString()}' and name='${name}' and winningLottery='null' ALLOW FILTERING;") 
  
  def insertLottery(lottery:Lottery) = executeQuery(s"insert into lottery (id,name,startDate,endDate,winningLottery,shardKey) values ('${lottery.id}','${lottery.name}','${lottery.startDate}','${lottery.endDate}','null','${DateTimeUtil.convertDatetoMonthYear()}');")
  
  def insertLotteryEvent(lotteryTicket:LotteryTicket) = executeQuery(s"insert into lottery_ticket (id,lotteryId,userId,date) values ('${lotteryTicket.id}','${lotteryTicket.lotteryId}','${lotteryTicket.userId}','${lotteryTicket.date}');")
  
  def getLotteryWinner(date:String,name:String) = executeQuery(s"select winningLottery from lottery where shardKey='${DateTimeUtil.convertDatetoMonthYear(Some(date))}' and startDate='${DateTimeUtil.getPreviousDate(Some(date))}' and name='${name}' ALLOW FILTERING;")

  def insertLotteryWinner(lottery:Lottery) = executeQuery(s"insert into lottery (id,name,startDate,endDate,winningLottery,shardKey) values ('${lottery.id}','${lottery.name}','${lottery.startDate}','${lottery.endDate}','${lottery.winningLottery.get}','${DateTimeUtil.convertDatetoMonthYear()}');")
  
  def endLotteryAndSelectLotteryWinner(lottery:Lottery) = executeQuery(s"select id from lottery_ticket where lotteryId='${lottery.id}' and date='${lottery.startDate}' ALLOW FILTERING;")
  

}