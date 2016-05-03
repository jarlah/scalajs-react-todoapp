package scalajsreact.template.components

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

/**
  * Created by jarlandre on 01/05/16.
  */
object TodoListComponent {
  val component = ReactComponentB[Seq[TodoItem]]("TodoList")
    .render_P ( todos =>
      if (todos.nonEmpty)
        <.ul(todos.map(todo =>
          <.li(^.key := todo.id,
            TodoItemComponent(todo))))
      else <.div("No todos")
    )
    .build

  def apply(p: Seq[TodoItem]) = component(p)
}
