package com.bynder.lottery.execution.model.db

import com.datastax.driver.core._
import scala.collection.JavaConverters._
import com.google.common.util.concurrent._
import scala.concurrent._
import ExecutionContext.Implicits.global

object Schema {
  
  def createSchema = {  
  
  val keyspaceQuery = "CREATE KEYSPACE if not exists  lotteries WITH replication = {'class':'SimpleStrategy','replication_factor':1}";  
  Connection.session.execute(keyspaceQuery)
  
  Connection.session.execute("USE lotteries;")
  
  val lotteryTableQuery = s"create table if not exists lottery (id text, name text, startDate TIMESTAMP ,endDate TIMESTAMP, winningLottery text,shardKey text, PRIMARY KEY ((shardKey),startDate,name));"
  Connection.session.execute(lotteryTableQuery)
  
  val lotteryTicketTableQuery = s"create table if not exists lottery_ticket (id text,  lotteryId text , userId text , date TIMESTAMP , PRIMARY KEY ((lotteryId,date),id));"
  Connection.session.execute(lotteryTicketTableQuery)
  
  
  } 
}