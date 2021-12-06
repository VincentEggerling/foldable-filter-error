import core.FoldableFilterError.FoldableFilterErrorOps
import zio.console.{Console, putStrLn}
import zio.interop.catz._
import zio.ZIO

import java.io.IOException
import scala.util.{Failure, Success, Try}
import zio.console._
import zio.test.Assertion._
import zio.test._
import zio.test.environment.TestConsole

object ConsoleSpec extends DefaultRunnableSpec {

  def spec: ZSpec[Environment, Failure] =
    suite("filterErrorM ZIO")(
      testM("List-Try console logging") {
        def consoleLog(l: List[Throwable]): ZIO[Console, IOException, Unit] = {
          val logString = l.map(err => err.getMessage).foldLeft("")((acc, elem) => acc + elem + "\n")
          putStr(logString)
        }

        val l1: List[Try[Int]] = List(Success(1), Failure(new Exception("fail")), Success(3))
        for {
          filteredList <- l1.filterErrorM(listErr => consoleLog(listErr))
          output <- TestConsole.output
        } yield assert(output)(equalTo(Vector("fail\n"))) && assert(filteredList)(equalTo(List(1,3)))
      },
      testM("List-Option console logging") {
        def consoleLog(l: List[Unit]): ZIO[Console, IOException, Unit] = {
          val logString = s"${l.size} empty options"
          putStr(logString)
        }

        val l1: List[Option[Int]] = List(None, None)
        for {
          filteredList <- l1.filterErrorM(listErr => consoleLog(listErr))
          output <- TestConsole.output
        } yield assert(output)(equalTo(Vector("2 empty options"))) && assert(filteredList)(equalTo(List.empty))
      }
    )
}