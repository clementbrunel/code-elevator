import sbt._
import Keys._
import play.Project._
//import com.typesafe.plugin.SbtGoodiesPlugin

object ApplicationBuild extends Build {

  val appName = "elevator-brain"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.typesafe.slick" % "slick_2.10" % "1.0.0",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.h2database" % "h2" % "1.3.166",
//    "securesocial" %% "securesocial" % "master-SNAPSHOT",
    jdbc,
     "com.googlecode.usc" % "jdbcdslog" % "1.0.6.2"
  )
  
  //Il faut aller corriger la scala version dans le build.sbt en 2.10.0 puis publish-local dans le dossier enfin tilde run.
//  lazy val playAutoRefreshPlugin = file("modules/plugin-play-auto-refresh")



  lazy val sampleApp = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions ++= Seq("-feature", "-language:postfixOps,implicitConversions")
//    resolvers += Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
  )

  
}
