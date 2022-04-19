package com.bynder.lottery.execution.utils

import com.bynder.lottery.execution.model.entity.LotteryTicket
import com.datastax.driver.core.ResultSet
import com.bynder.lottery.execution.model.entity.Lottery
import scala.collection.JavaConverters._

trait Serializer {
  
  type T
  
  def convert(result:ResultSet): T
  
  
}

object ResultSetToLotteryTicketCaseClass extends Serializer {
  
  type T = Array[LotteryTicket]
  
  def convert(result:ResultSet) = {
    
    result.asScala.toArray.map(row => LotteryTicket(row.getString("id"),row.getString("lotteryId"),
        row.getString("userId"),row.getDate("date").toString()))
    
  }
      
  
}

object ResultSetToLotteryCaseClass extends Serializer {
  
  type T = Array[Lottery]
  
  def convert(result:ResultSet) = {
    
    result.asScala.toArray.map(row =>Lottery(row.getString("id"),row.getString("name"),
        row.getDate("startDate").toString(),
        row.getDate("endDate").toString(),
           Option(row.getString("winningLottery")) ))
  }
  
}


