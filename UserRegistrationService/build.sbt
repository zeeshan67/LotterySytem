enablePlugins(JavaServerAppPackaging)

name := "lottery-user-registration-service"

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
  "com.h2database" % "h2" % "1.3.148" ,
  "redis.clients" % "jedis" % "1.5.2",
  "com.typesafe.slick"  %% "slick"   % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp"   % "3.3.3",
  "org.postgresql"     % "postgresql"  % "42.3.1",
  "ch.qos.logback"     % "logback-classic"  % "1.2.7",
  "org.scalatest"          %% "scalatest"        % "3.2.10",
  "org.mindrot"  % "jbcrypt"   % "0.3m"
  )
}

// Assembly settings
mainClass in Global := Some("com.bynder.lottery.user.registration.Main")

jarName in assembly := "lottery-user-registration-service.jar"