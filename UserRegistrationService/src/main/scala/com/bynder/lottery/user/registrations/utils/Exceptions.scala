package com.bynder.lottery.user.registrations.utils

object Exceptions {
  class NotFoundException(message: String) extends NoSuchElementException(message)
}