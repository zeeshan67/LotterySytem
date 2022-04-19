package com.bynder.lottery.user.registration.model.db

import slick.jdbc.H2Profile.api._

object Connection {
  val db = Database.forConfig("h2mem")  
}