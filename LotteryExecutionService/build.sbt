enablePlugins(JavaServerAppPackaging)

name := "lottery-execution-service"

version := "0.1"

organization := "com.bynder"

scalaVersion := "2.12.2"

resolvers ++= Seq("Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/",
				  "Spray Repository"    at "https://repo.spray.io"
                  )

libraryDependencies ++= {
  Seq("com.typesafe.akka" %% "akka-actor" % "2.5.23",
  "com.typesafe.akka" %% "akka-stream" % "2.5.23",
  "com.typesafe.akka" %% "akka-http" % "10.1.8",
  "com.typesafe.play" %% "play-json" % "2.6.7",
  "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.20.0",
  "com.h2database" % "h2" % "1.3.148" % Test,
  "com.typesafe.slick"  %% "slick"   % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp"   % "3.3.3",
  "redis.clients" % "jedis" % "1.5.2",
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.0",
  "ch.qos.logback"     % "logback-classic"  % "1.2.7",
  "org.scalatest"          %% "scalatest"        % "3.2.10",
  "org.json4s" %% "json4s-native" % "3.6.6"
  
  )
}

// Assembly settings
mainClass in Global := Some("com.bynder.lottery.execution.Main")

jarName in assembly := "lottery-execution-service.jar"