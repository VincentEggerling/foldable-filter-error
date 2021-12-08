package example

import core.FoldableFilterError.FoldableFilterErrorOps

import scala.util.Try

object MainScala {

  val db1 = List("1", "2", "3", "bad", "5", "oops")
  /** Get all rows of the DB1. They should all be integer, but who knows... */
  def queryDB1: List[Try[Int]] = db1.map(str => Try(str.toInt))


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
  def queryDB2(key: List[Int]): Vector[Either[MyError, String]] = {
    key.map(k => db2.get(k).toRight(MyError(s"Cannot find person with key $k"))).toVector
  }

  def main(args: Array[String]): Unit = {
    val db1Result: List[Try[Int]] = queryDB1
    val db1FilteredResult: List[Int] = db1Result.filterError((lErr: List[Throwable]) => println(s"Failure parsing rows in DB1 : $lErr"))

    val db2Result: Vector[Either[MyError, String]]  = queryDB2(db1FilteredResult)
    val db2FilteredResult: Vector[String] = db2Result.filterError((lErr: Vector[MyError]) => println(s"Failure reading name in DB2: $lErr"))

    println("Final clean result: " + db2FilteredResult)
  }
}
