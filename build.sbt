name := "foldable-filter-error"

version := "0.1"

scalaVersion := "2.13.7"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.0"
libraryDependencies += "dev.zio" %% "zio" % "1.0.12"
libraryDependencies += "dev.zio" %% "zio-interop-cats" % "3.2.9.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.0"
libraryDependencies += "dev.zio" %% "zio-test" % "1.0.12" % "test"
libraryDependencies += "dev.zio" %% "zio-test-sbt" % "1.0.12" % "test"