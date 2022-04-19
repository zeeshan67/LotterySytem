package com.bynder.lottery.execution.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import play.api.libs.json.Reads
import play.api.libs.json.Json

case class LotteryTicket(id:String,lotteryId:String,userId:String,date:String)

case class Lottery(id:String,name:String,startDate:String,endDate:String,winningLottery:Option[String]=None)


case class SubmitLotteryRequest(userId:String,name:String)

                
object SubmitLotteryRequest {
  implicit val submitLotteryRequestReads: Reads[SubmitLotteryRequest] = Json.reads[SubmitLotteryRequest]
}
