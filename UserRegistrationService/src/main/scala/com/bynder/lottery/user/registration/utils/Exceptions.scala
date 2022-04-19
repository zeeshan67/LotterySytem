package com.bynder.lottery.user.registration.utils

object Exceptions {
  class NotFoundException(message: String) extends NoSuchElementException(message)
}