package com.bynder.lottery.execution.model.db

import com.datastax.driver.core._
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import com.datastax.driver.core.querybuilder.{QueryBuilder => QB}

object Connection  {
  
  val cluster = {
    Cluster.builder()
      .addContactPoint("127.0.0.1")
      .build()
  }

  val session = cluster.connect()
  
}