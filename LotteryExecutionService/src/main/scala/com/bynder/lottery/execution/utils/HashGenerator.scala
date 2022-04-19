package com.bynder.lottery.execution.utils

object HashGenerator {
    
  def generateHash(num:Int):String = {
    var encoded = ""
    var temp = num
    val characterSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    while (temp>0) {
      var r = temp%62
      temp = temp/62
      encoded = characterSet(r).toString+encoded
    }
    "0" * (7 - encoded.length())+encoded 
  }
  
}