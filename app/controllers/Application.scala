package controllers

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object Application extends Controller {

  //Methods

  def simple: Int = {
    Thread.sleep(200)
    2
  }

  def withFuture: Future[Int] = Future {
    Thread.sleep(200)
    2
  }

  def withFuture2: Future[Int] = Future {
    Thread.sleep(200)
    2
  }

  def futureWithParam(i: Int): Future[Int] = Future {
    Thread.sleep(200)
    i + 2
  }

  def withOption: Option[Int] = {
    Thread.sleep(200)
    Some(2)
  }

  def withOptionNone: Option[Int] = {
    Thread.sleep(200)
    None
  }

  def withFutureOfOption: Future[Option[Int]] = Future {
    Thread.sleep(200)
    Some(2)
  }

  def withFutureOfOptionFails:Future[Option[Int]] = {
    Future {
      try {
        Some(2 / 0)
      }
      catch {
        case e => None
      }
    }
  }

  def withError: Future[Try[Int]] = {
    Future {
      Try(
        2 / 0
      )
    }
  }

  //Simple

  def index = Action {
    Ok(simple.toString)
  }

  //Futures

  def index2 = Action.async {
    withFuture
      .map(c => c.toString)
      .map(Ok(_))
  }

  def index3 = Action.async {
    withFuture
      .zip(withFuture2)
      .map(v => v._1 + v._2)
      .map(_.toString)
      .map(Ok(_))
  }

  def index4 = Action.async {
    val future = for {
      firstResult <- withFuture
      result <- futureWithParam(firstResult)
    } yield result

    future
      .map(_.toString)
      .map(Ok(_))
  }

  //Options

  def index5 = Action {
    Ok(withOption.map(_.toString).getOrElse("fail"))
  }

  def index6 = Action {
    Ok(withOptionNone.map(_.toString).getOrElse("fail"))
  }

  def index7 = Action.async {
    withFutureOfOption
      .map(_.getOrElse(0))
      .map(_.toString)
      .map(Ok(_))
  }

  // Try

  def index8 = Action.async {
    withFutureOfOptionFails
      .map(_.getOrElse(0))
      .map(_.toString)
      .map(Ok(_))
  }

  def index9 = Action.async {
    withError
      .map(_.getOrElse(0))
      .map(_.toString)
      .map(Ok(_))
  }

  def index10 = Action.async{
    withError
    .map(_.map(_.toString))
    .map(_.getOrElse("fail"))
    .map(Ok(_))
  }

}
