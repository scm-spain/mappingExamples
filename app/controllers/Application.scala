package controllers

import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

  def withOptionFail: Option[Int] = {
    Thread.sleep(200)
    None
  }

  def withFutureOfOption: Future[Option[Int]] = Future {
    Thread.sleep(200)
    Some(2)
  }

  //Simple

  def index = Action {
    Ok(simple.toString)
  }

  //Futures

  def index2 = Action.async {
    withFuture
      .map(_.toString)
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
    Ok(withOptionFail.map(_.toString).getOrElse("fail"))
  }

  def index7 = Action.async {
    withFutureOfOption
      .map(_.getOrElse(0))
      .map(_.toString)
      .map(Ok(_))
  }
  


}