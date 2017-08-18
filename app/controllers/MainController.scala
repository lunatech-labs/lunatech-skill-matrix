package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc._

@Singleton
class MainController @Inject() (components: ControllerComponents) extends AbstractController(components) {

  def index = Action {
    Ok(views.html.index())
  }

}
