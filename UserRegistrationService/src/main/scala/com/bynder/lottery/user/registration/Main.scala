package com.bynder.lottery.user.registration

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
import com.bynder.lottery.user.registration.routes.UserRegistrationRoutes
import org.mindrot.jbcrypt.BCrypt
import com.bynder.lottery.user.registrations.model.db.Schema
import com.bynder.lottery.user.registrations.utils.Logging

object Main extends App with Logging{
 
  implicit val system = ActorSystem("lottery-registration")
  implicit val timeout: Timeout = Timeout(120,TimeUnit.SECONDS)
  implicit val materializer: ActorMaterializer =
    ActorMaterializer()(system)
  val config = system.settings.config
  val servicehost = config.getString("service.host")
  val serviceport = config.getString("service.port").toInt
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  Schema.createSchema()

  val userRegistrationRoutes = new UserRegistrationRoutes
  val routes = userRegistrationRoutes.routes 
  
  val bindingFuture = Http().bindAndHandle(routes, servicehost, serviceport)
  StdIn.readLine()
  
  bindingFuture.failed.foreach { ex =>
    log.error("Failed to bind to {}:{}",servicehost,serviceport)
  }

}