package scalajsreact.template

import japgolly.scalajs.react._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.document

case class TodoItem(id: Int, title: String, completed: Boolean)

case class TodoState(items: List[TodoItem], text: String)

class TodoBackend($: BackendScope[Unit, TodoState]) {
  def onChange(e: ReactEventI) = $.modState(_.copy(text = e.target.value))

  def handleSubmit(e: ReactEventI) : Callback =
    e.preventDefaultCB >>
      $.modState(s => TodoState(s.items ++ Seq(TodoItem(s.items.length, s.text, completed = false)), ""))

  def render(state: TodoState) =
    <.div(
      <.h3("TODO"),
      TodoList(state.items),
      TodoForm(TodoForm.Props(handleSubmit, onChange, state.text, state.items.length + 1))
    )
}

object TodoList {
  val component = ReactComponentB[Seq[TodoItem]]("TodoList")
    .render_P ( todos =>
      if (todos.nonEmpty) <.ul(todos.map(todo => <.li(^.key := todo.id, todo.title)))
      else <.div("No todos")
    )
    .build

  def apply(p: Seq[TodoItem]) = component(p)
}

object TodoForm {
  case class Props(handleSubmit: ReactEventI => Callback, onChange: ReactEventI => Callback, text: String, nextId: Int)

  val component = ReactComponentB[Props]("TodoForm")
    .render_P ( p => {
      <.form(^.onSubmit ==> p.handleSubmit,
        <.input(^.onChange ==> p.onChange, ^.value := p.text),
        <.button("Add #", p.nextId)
      )
    })
    .build

  def apply(p: Props) = component(p)
}

@JSExport
object TodoApp extends JSApp {

  @JSExport
  override def main(): Unit = {
    val TodoApp = ReactComponentB[Unit]("TodoApp")
      .initialState(TodoState(Nil, ""))
      .renderBackend[TodoBackend]
      .build

    ReactDOM.render(TodoApp(Unit), document.body)
  }

}

