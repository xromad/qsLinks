lazy val akkaHttpVersion = "10.2.4"
lazy val akkaVersion    = "2.6.14"
lazy val logBackVersion = "1.2.3"
lazy val scalaTestVersion = "3.1.4"
lazy val mongoScalaDriver = "4.2.3"
lazy val mockitoVersion = "3.9.0"
lazy val mockitoScalaTest = "1.16.37"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.13.5"
    )),

    name := "qsLinks",
    version := "0.0.1",

    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % logBackVersion,
      "org.mongodb.scala" %% "mongo-scala-driver"       % mongoScalaDriver,
      //---------------------------------------------------------------------------
      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion   % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion       % Test,
      "org.scalatest"     %% "scalatest"                % scalaTestVersion  % Test,
      "org.mockito"       % "mockito-core"              % mockitoVersion    % Test,
      "org.mockito"       %% "mockito-scala-scalatest"  % mockitoScalaTest  % Test,
      "org.mongodb.scala" %% "mongo-scala-driver"       % mongoScalaDriver  % Test
    )
  )
