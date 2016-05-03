package scalajsreact.template.models
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.ext.Ajax.InputData
import scala.scalajs.js

case class TodoItem(id: Int, title: String, completed: Boolean)

object TodoItem {
  def fromJson(jsonArray: js.Array[js.Dynamic]): List[TodoItem] = {
    jsonArray.map(jsonObject =>
      TodoItem(jsonObject.id.toString.toInt, jsonObject.title.toString, jsonObject.completed.toString.toBoolean)
    ).toList
  }

  def get(
    url: String = "http://jsonplaceholder.typicode.com",
    data: Option[InputData] = None,
    timeout: Int = 0,
    headers: Map[String, String] = Map(
      "Accept" -> "application/json",
      "Content-Type" -> "application/json"
    ),
    withCredentials: Boolean = false,
    responseType: Option[String] = Some("")
  ) = Ajax.get(url + "/todos", data.orNull, timeout, headers, withCredentials, responseType.getOrElse(""))
}