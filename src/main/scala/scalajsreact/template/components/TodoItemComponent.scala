package scalajsreact.template.components

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

/**
  * Created by jarlandre on 01/05/16.
  */
object TodoItemComponent {
  val component = ReactComponentB[TodoItem]("TodoItem")
    .render_P(t => <.span(t.title))
    .build

  def apply(t: TodoItem) = component(t)
}
