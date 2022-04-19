package com.bynder.lottery.execution.utils

import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


object DateTimeUtil {
  
  val dateFormatter = DateTimeFormatter.ofPattern("yyyyMM")
  
  def convertDatetoMonthYear(date:Option[String]=None) = {
    
    (if (date.nonEmpty) LocalDate.parse(date.get, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    else java.time.LocalDate.now()).format(dateFormatter)  
    
  }
  
  
  def getPreviousDate(date:Option[String]=None) = {

  if (date.nonEmpty) LocalDate.parse(date.get, DateTimeFormatter.ofPattern("yyyy-MM-dd")).minusDays(1).toString()  
  else java.time.LocalDate.now().minusDays(1).toString()

  }
  }