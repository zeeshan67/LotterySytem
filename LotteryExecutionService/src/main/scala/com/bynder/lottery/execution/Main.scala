package com.bynder.lottery.execution

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import scala.concurrent.duration.DurationConversions
import java.util.concurrent.TimeUnit
import akka.http.scaladsl.Http
import com.bynder.lottery.execution.routes.LotteryExecutionRoutes
import com.bynder.lottery.exectuion.utils.Logging
import com.bynder.lottery.execution.model.db.Schema
import com.bynder.lottery.execution.routes.LotteryExecutionRoutes
import akka.actor.Props

object Main extends App with Logging {

  implicit val system = ActorSystem("lottery-execution")
  implicit val timeout: Timeout = Timeout(120,TimeUnit.SECONDS)
  implicit val materializer: ActorMaterializer =
    ActorMaterializer()(system)
  val config = system.settings.config
  val servicehost = config.getString("service.host")
  val serviceport = config.getString("service.port").toInt
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  Schema.createSchema
  
  val lotteryExecutionRoutes = new LotteryExecutionRoutes()
  val routes = lotteryExecutionRoutes.routes 
  
  val bindingFuture = Http().bindAndHandle(routes, servicehost, serviceport)
  StdIn.readLine()
  
  bindingFuture.failed.foreach { ex =>
    log.error("Failed to bind to {}:{}",servicehost,serviceport)
  }

}