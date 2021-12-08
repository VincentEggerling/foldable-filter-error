package example

import core.FoldableFilterError.FoldableFilterErrorOps
import zio.Task
import zio.console.putStrLn
import zio.interop.catz._

import scala.util.Try


object MainZIO extends zio.App  {

  val db1 = List("1", "2", "3", "bad", "5", "oops")
  /** Get all rows of the DB1. They should all be integer, but who knows... */
  def queryDB1: Task[List[Try[Int]]] = Task.effect(db1.map(str => Try(str.toInt)))


  val db2 = Map(
    1 -> "Alice",
    //2 -> "Bob",
    3 -> "Charlie",
    4 -> "Denis",
    5 -> "Eric",
    6 -> "Frank",
    7 -> "Greg"
  )
  case class MyError(msg: String)
  /** Get all people's name based on the primary keys. The keys are contained
   * in DB1. Also some people might be missing from DB2 so we handle that with an Either.
   * We return a Vector just for the example. */
  def queryDB2(key: List[Int]): Task[Vector[Either[MyError, String]]] = {
    Task.effect(key.map(k => db2.get(k).toRight(MyError(s"Cannot find person with key $k"))).toVector)
  }

  def run(args: List[String]) = {
    val zProgram = for {
      db1Result <- queryDB1
      db1FilteredResult <- db1Result.filterErrorM((lErr: List[Throwable]) => putStrLn(s"Failure parsing rows in DB1 : $lErr"))

      db2Result <- queryDB2(db1FilteredResult)
      db2FilteredResult <- db2Result.filterErrorM((lErr: Vector[MyError]) => putStrLn(s"Failure reading name in DB2: $lErr"))

      _ <- putStrLn(s"Final clean result: $db2FilteredResult")
    } yield ()
    zProgram.exitCode
  }
}
