package com.bynder.lottery.user.registration.model.db

import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import com.bynder.lottery.user.registration.model.entity.User
import com.bynder.lottery.user.registration.model.entity.Role
import scala.util.Success
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.Failure

object Schema {
  
  def createSchema() = {

  lazy val userTblQuery: TableQuery[UserTable] = TableQuery[UserTable]

  lazy val roleTblQuery: TableQuery[RoleTable] = TableQuery[RoleTable]


   val ddlSchemaQuery = userTblQuery.schema ++ roleTblQuery.schema 

  println(" \n \n Create  Statements ")
  ddlSchemaQuery.create.statements.foreach(println)


   val schemaActions =  DBIO.seq(  ddlSchemaQuery.create  )


   val futureResponse: Future[Unit] = Connection.db.run(schemaActions)
   val unitResponse:  Unit = Await.result(futureResponse, 5.seconds)
   
   (Await.result(RoleRepository.add(Role(0,"Admin")),5.seconds))
   (Await.result(RoleRepository.add(Role(0,"User")),5.seconds))
   (Await.result(UserRepository().add(User(0,"admin","admin",Some("Zeeshan"),Some("Patel"),"885855555",1)),5.seconds)) 
  
  println("Schema setup completed") 
      
  }
}
