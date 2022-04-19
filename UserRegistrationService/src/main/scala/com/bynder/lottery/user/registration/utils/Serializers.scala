package com.bynder.lottery.user.registration.utils

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints, jackson}

trait Serializers extends PlayJsonSupport {

  implicit val serialization: jackson.Serialization.type = jackson.Serialization

  implicit val formats: Formats = Serialization.formats(NoTypeHints)

}