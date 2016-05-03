package scalajsreact.template.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalajsreact.template.models.TodoItem

object TodoListComponent {
  case class Props(items: Seq[TodoItem], toggle: TodoItem => ReactEventI => Callback, delete: TodoItem => ReactEventI => Callback)

  val component = ReactComponentB[Props]("TodoList")
    .render_P ( p =>
      if (p.items.nonEmpty)
        <.ul(p.items.map(todo =>
          <.li(^.key := todo.id,
            TodoItemComponent(TodoItemComponent.Props(todo, p.toggle(todo), p.delete(todo))))))
      else <.div("No todos")
    )
    .build

  def apply(p: Props) = component(p)
}
