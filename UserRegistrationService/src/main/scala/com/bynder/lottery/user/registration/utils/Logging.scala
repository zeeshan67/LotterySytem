package com.bynder.lottery.user.registrations.utils

import org.slf4j.{Logger, LoggerFactory}

trait Logging {
  val log: Logger = LoggerFactory.getLogger(this.getClass)
}