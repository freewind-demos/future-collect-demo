package example
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Hello extends App {

  val result = Future("abc").collect({ case s if s.startsWith("a") => 111 })
  println(result)

  val result2 = Future.apply[Option[String]](Option.empty).transform({
    case Success(Some(s)) if s.startsWith("a") => Success(Some(123))
    case Success(None) => Success(None)
  }).collect({
    case Some(v) => println(v)
    case None => println("none")
  })
  Await.result(result2, 1.second)

  val result3 = Future.apply[Option[String]](throw new Exception("my exception")).transform({
    case Success(Some(s)) if s.startsWith("a") => Success(123)
    case Success(None) => Success(444)
    case Failure(e) => Success(999)
  })
  result3.foreach((x: Int) => println(x))
  Await.result(result3, 1.second)

}

