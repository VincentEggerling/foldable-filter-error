
import core.FoldableFilterError.FoldableFilterErrorOps
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.immutable.Queue
import scala.util.{Failure, Success, Try}


class FoldableFilterErrorScalaTest extends AnyFunSuite {

  test("List-Try filterError") {
    val errorHandlerMutable1 = scala.collection.mutable.ListBuffer.empty[Throwable]
    val l1: List[Try[Int]] = List(Success(1), Failure(new Exception("2")), Success(3))
    val filteredL1: List[Int] = l1.filterError(listErr => listErr.foreach(err => errorHandlerMutable1.append(err)))
    assert(filteredL1 == List(1,3))
    assert(errorHandlerMutable1.toList.map(_.getMessage) == List("2"))

    val errorHandlerMutable2 = scala.collection.mutable.ListBuffer.empty[Throwable]
    val l2: List[Try[Int]] = List(Success(1))
    val filteredL2: List[Int] = l2.filterError(listErr => listErr.foreach(err => errorHandlerMutable2.append(err)))
    assert(filteredL2 == List(1))
    assert(errorHandlerMutable2.toList == List.empty[Throwable])

    val errorHandlerMutable3 = scala.collection.mutable.ListBuffer.empty[Throwable]
    val l3: List[Try[Int]] = List(Failure(new Exception("1")))
    val filteredL3: List[Int] = l3.filterError(listErr => listErr.foreach(err => errorHandlerMutable3.append(err)))
    assert(filteredL3 == List.empty)
    assert(errorHandlerMutable3.toList.map(_.getMessage) == List("1"))

    val errorHandlerMutable4 = scala.collection.mutable.ListBuffer.empty[Throwable]
    val l4: List[Try[Int]] = List()
    val filteredL4: List[Int] = l4.filterError(listErr => listErr.foreach(err => errorHandlerMutable4.append(err)))
    assert(filteredL4 == List.empty)
    assert(errorHandlerMutable4.toList.map(_.getMessage) == List.empty)
  }


  test("Option-Either filterError") {
    val errorHandlerMutable1 = scala.collection.mutable.ListBuffer.empty[String]
    val o1: Option[Either[String, Int]] = Some(Right(3))
    val filteredO1 = o1.filterError(oerr => oerr.foreach(err => errorHandlerMutable1.append(err)))
    assert(filteredO1 == Some(3))
    assert(errorHandlerMutable1.toList == List.empty)

    val errorHandlerMutable2 = scala.collection.mutable.ListBuffer.empty[String]
    val o2: Option[Either[String, Int]] = Some(Left("Bad"))
    val filteredO2 = o2.filterError(oerr => oerr.foreach(err => errorHandlerMutable2.append(err)))
    assert(filteredO2 == None)
    assert(errorHandlerMutable2.toList == List("Bad"))

    val errorHandlerMutable3 = scala.collection.mutable.ListBuffer.empty[String]
    val o3: Option[Either[String, Int]] = None
    val filteredO3 = o3.filterError(oerr => oerr.foreach(err => errorHandlerMutable3.append(err)))
    assert(filteredO3 == None)
    assert(errorHandlerMutable3.toList == List.empty)
  }


  test("Queue-Option filterError") {
    val errorHandlerMutable1 = scala.collection.mutable.ListBuffer.empty[Unit]
    val q1: Queue[Option[Int]] = Queue(Some(1), None)
    val filteredQ1 = q1.filterError(qerr => qerr.foreach(err => errorHandlerMutable1.append(err)))
    assert(filteredQ1 == Queue(1))
    assert(errorHandlerMutable1.toList == List(()))

    val errorHandlerMutable2 = scala.collection.mutable.ListBuffer.empty[Unit]
    val q2: Queue[Option[Int]] = Queue(None, None)
    val filteredQ2 = q2.filterError(qerr => qerr.foreach(err => errorHandlerMutable2.append(err)))
    assert(filteredQ2 == List.empty)
    assert(errorHandlerMutable2.toList == List((), ()))

    val errorHandlerMutable3 = scala.collection.mutable.ListBuffer.empty[Unit]
    val q3: Queue[Option[Int]] = Queue.empty
    val filteredQ3 = q3.filterError(qerr => qerr.foreach(err => errorHandlerMutable3.append(err)))
    assert(filteredQ3 == List.empty)
    assert(errorHandlerMutable3.toList == List.empty)

  }
}