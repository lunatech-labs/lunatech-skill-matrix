package acceptance

import java.io.File
import java.util
import java.util.Optional

import com.atlassian.oai.validator.SwaggerRequestResponseValidator
import com.atlassian.oai.validator.model.{Request, Response}
import com.atlassian.oai.validator.report.{ValidationReport, ValidationReportFormatter}
import org.scalatest.matchers.{MatchResult, Matcher}
import play.api.libs.json.JsValue
import play.api.mvc.{Result, Request => PlayRequest}
import play.api.test.Helpers._

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.io.Source

trait SwaggerValidator {

  def validateResponseAgainstSwagger(resource: String) = new ValidateResponseAgainstSwagger(resource)

  def validateAgainstSwagger(resource: String) = new ValidateAgainstSwagger(resource)

  def getReport(pair: (PlayRequest[Any], Future[Result]), resource: String): ValidationReport = {
    val swaggerPayload = Source.fromFile(new File(resource).getAbsolutePath).getLines.mkString("\n")

    val validator = SwaggerRequestResponseValidator.createFor(swaggerPayload).build()
    validator.validate(getRequest(pair._1), getResponse(pair._2))
  }

  def getRequest(request: PlayRequest[Any]) = new Request {

    def getBody: Optional[String] = request.body match {
      case jsValue: JsValue => Optional.of(jsValue.toString())
      case _ => Optional.empty()
    }

    override def getMethod: Request.Method = Request.Method.valueOf(request.method)

    override def getQueryParameters: util.Collection[String] = request.queryString.keys.toList.asJava

    override def getPath: String = request.uri


    override def getQueryParameterValues(name: String): util.Collection[String] = request.queryString.getOrElse(name, List.empty[String]).toList.asJava
  }

  def getResponse(response: Future[Result]) = new Response {
    override def getBody: Optional[String] = Optional.of(contentAsString(response))

    override def getStatus: Int = status(response)
  }

  class ValidateAgainstSwagger(resource: String) extends Matcher[(PlayRequest[Any], Future[Result])] {

    def apply(pair: (PlayRequest[Any], Future[Result])): MatchResult = {
      val report: ValidationReport = getReport(pair, resource)

      MatchResult(
        !report.hasErrors,
        s"Swagger validation did not pass!\n${ValidationReportFormatter.format(report)}",
        "Swagger validation passed."
      )
    }
  }

  class ValidateResponseAgainstSwagger(resource: String) extends Matcher[(PlayRequest[Any], Future[Result])] {

    def apply(pair: (PlayRequest[Any], Future[Result])): MatchResult = {
      val report: ValidationReport = getReport(pair, resource)

      val containsOnlyExpectedRequestErrors: Boolean =
        if (report.hasErrors)
          report.getMessages.asScala.toList
            .map(x => x.getMessage.contains("Object has missing required properties") || x.getMessage.contains("not found in enum "))
            .headOption.getOrElse(false)
        else true

      MatchResult(
        containsOnlyExpectedRequestErrors,
        s"Swagger validation did not pass!\n${ValidationReportFormatter.format(report)}",
        "Swagger validation passed."
      )
    }
  }

}


