package scalajsreact.template.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js
import scalajsreact.template.models.TodoItem

object TodoItemComponent {
  case class Props(item: TodoItem, markDone: ReactEventI => Callback, delete: ReactEventI => Callback)

  val component = ReactComponentB[Props]("TodoItem")
    .render_P(p => <.span(^.onClick ==> p.markDone,
      <.div(
        if(p.item.completed) <.span(^.style := js.Dictionary("textDecoration" -> "line-through"), p.item.title)
        else p.item.title,
        <.button(^.onClick ==> p.delete, "Delete")
      )))
    .build

  def apply(p: Props) = component(p)
}
