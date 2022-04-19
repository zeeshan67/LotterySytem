package com.bynder.lottery.execution

import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}

import scala.concurrent.duration._

import akka.actor.{ActorSystem, Props}
import com.bynder.lottery.execution.ScheduleActor.GenerateLotteryWinner
import scala.concurrent.ExecutionContext
/**
 * Created by Zeeshan on 05/16/2022.
 */

object Constant {
  val DAYHOURS = 86400000
}

object ScheduleJob extends App {

  val system = ActorSystem("SchedulerSystem")
  val schedulerActor = system.actorOf(Props(classOf[ScheduleActor]), "Actor")
  implicit val ec = system.dispatcher
  system.scheduler
    .schedule(calculateInitialDelay().milliseconds, 60.seconds)(
      schedulerActor ! GenerateLotteryWinner)

  def calculateInitialDelay(): Long = {
    val now = new Date()
    val sdf = new SimpleDateFormat("HH:mm:ss")
    sdf.setTimeZone(TimeZone.getTimeZone("IST"))
    val time1 = sdf.format(now)
    val time2 = "00:00:00"
    val format = new SimpleDateFormat("HH:mm:ss")
    val date1 = format.parse(time1)
    val date2 = format.parse(time2)
    val timeDifference = date2.getTime() - date1.getTime()
    println(timeDifference + "timedifference" + "}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}")
    val calculatedTime = if (timeDifference < 0) (Constant.DAYHOURS + timeDifference) else timeDifference
    
    println("calculated time ======================" + calculatedTime)
    calculatedTime
  }

}