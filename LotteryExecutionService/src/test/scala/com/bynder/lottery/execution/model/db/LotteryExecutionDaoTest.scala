package com.bynder.lottery.execution.model.db

import org.scalatest.flatspec.AnyFlatSpec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration._

class LotteryExecutionDaoTest extends AnyFlatSpec {
  
  Schema.createSchema
  
  "Lottery Event and Lottery ticket table query" should "return empty result initially" in {
    val lotteryTbl = Await.result(new LotteryExecutionDao(Connection.session).executeQuery("select * from lottery;"),5.seconds)
    assert(lotteryTbl.iterator().hasNext() === false)
    val lotteryTicketTbl = Await.result(new LotteryExecutionDao(Connection.session).executeQuery("select * from lottery_ticket;"),10.seconds)
    assert(lotteryTicketTbl.iterator().hasNext() === false)
    
  }
  
  "Lottery Event and Lottery ticket table query" should "return result after insert" in {
    
    val lotteryTbl = Await.result(new LotteryExecutionDao(Connection.session).executeQuery("insert into lottery (id,name,startDate,endDate,winningLottery,shardKey) values ('2','Bumper','2022-05-18','2022-05-18','test','202205');"),5.seconds)
    assert(lotteryTbl.asScala.toArray.length === 1)
    
    val lotteryTicketTbl = Await.result(new LotteryExecutionDao(Connection.session).executeQuery("insert into lottery_ticket (id,lotteryId,userId,date) values ('3','2','test1','2022-05-18');"),5.seconds)
    assert(lotteryTbl.asScala.toArray.length === 1)
  }
  
}