package scalajsreact.template

import japgolly.scalajs.react._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.document
import scalajsreact.template.components.{TodoFormComponent, TodoListComponent}
import scalajsreact.template.components.TodoListComponent.{Props => ListProps}
import scalajsreact.template.components.TodoFormComponent.{Props => FormProps}
import scala.scalajs.js
import scalajsreact.template.models.TodoItem
import scalajsreact.template.models.TodoItem.fromJson
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

@JSExport
object TodoApp extends JSApp {
  case class TodoState(items: List[TodoItem], text: String, nextIndex: Int)

  class TodoBackend($: BackendScope[TodoState, TodoState]) {

    def loadTodos(): Callback = $.props.map { p =>
      // Need to wrap for-yield in simple braces to get hold on the type when calling map and recover
      // Another alternative is defining for-yield result as a variable
      (for (
        xhr <- TodoItem.get()
      ) yield {
        js.JSON.parse(xhr.responseText) match {
          case json: js.Array[js.Dynamic] =>
            $.modState(_.copy(items = fromJson(json), nextIndex = json.length + 1))
          case other =>
            $.modState(_.copy(items = Nil))
        }
      }).map(_.runNow())
        .recover {
          case e => println("Something went horribly wrong: " + e)
        }
    }.void

    def onChange(e: ReactEventI) = {
      // this event is synthetic and must be persisted to enable access to its values, because modState is a callback
      e.persist()
      $.modState(_.copy(text = e.target.value))
    }

    def handleSubmit(e: ReactEventI) : Callback =
      // We use preventDefaultCB (CB stands for Callback) for combining the two monadic callbacks
      e.preventDefaultCB >>
        $.modState(s => s.copy(s.items ++ Seq(TodoItem(s.nextIndex, s.text, completed = false)), "", nextIndex = s.nextIndex + 1))

    def handleToggle(todo: TodoItem)(e: ReactEventI): Callback =
      // We use preventDefaultCB (CB stands for Callback) for combining the two monadic callbacks
      e.preventDefaultCB >>
        $.modState(s => s.copy(items = s.items.map(t =>
          if (t.id == todo.id) t.copy(completed = !t.completed)
          else t
        )))

    def handleDelete(todo: TodoItem)(e: ReactEventI): Callback =
      // We use preventDefaultCB (CB stands for Callback) for combining the two monadic callbacks
      e.preventDefaultCB >>
        $.modState(s => s.copy(items = s.items.filter(t => t.id != todo.id)))
  }

  @JSExport
  override def main(): Unit = {
    val TodoApp = ReactComponentB[TodoState]("TodoApp")
      .initialState_P(p => p)
      // The backend initialization done below enforces that state and props has the same type for TodoBackend
      // As you can see above we set initialState to the props value for TodoApp.
      // We could manipulate the properties before we return them in the method above, for example to set default state props.
      .backend(new TodoBackend(_))
      .render( $ =>
        <.div(
          <.h3("TODO"),
          TodoFormComponent(FormProps(
            $.backend.handleSubmit,
            $.backend.onChange,
            $.state.text,
            $.state.nextIndex
          )),
          TodoListComponent(ListProps(
            $.state.items,
            $.backend.handleToggle,
            $.backend.handleDelete
          ))
        )
      )
      // Note: Callback.when is not supported in scalaJSReactVersion version 0.10.1. Must use version 0.11.1 or higher.
      .componentDidMount($ => Callback.when($.isMounted())($.backend.loadTodos()))
      .build

    ReactDOM.render(TodoApp(TodoState(Nil, "", 0)), document.getElementById("app"))
  }

}
