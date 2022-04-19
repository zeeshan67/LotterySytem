name := "Lotter System"


version := "0.1"

organization := "com.bynder"


scalaVersion := "2.12.2"


lazy val executionService = (project in file("LotteryExecutionService"))

lazy val userregistrationservice = (project in file("UserRegistrationService"))

lazy val root = (project in file("."))
  .aggregate(userregistrationservice, executionService)




