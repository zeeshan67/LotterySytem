package com.bynder.lottery.execution


import akka.actor.Actor
import com.bynder.lottery.execution.service.ExecutionService

class ScheduleActor extends Actor {

  import ScheduleActor._


  def receive: PartialFunction[Any, Unit] = {
    case GenerateLotteryWinner => ExecutionService.generateLotteryWinner
  }

}

/**
 * Created by Zeeshan on 05/16/2022.
 */
object ScheduleActor {

  case object GenerateLotteryWinner

}
