package scalajsreact.template.components

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactComponentB, _}

object TodoFormComponent {
  case class Props(handleSubmit: ReactEventI => Callback, onChange: ReactEventI => Callback, text: String, nextId: Int)

  val component = ReactComponentB[Props]("TodoForm")
    .render_P ( p => {
      <.form(^.onSubmit ==> p.handleSubmit,
        <.input(^.onChange ==> p.onChange, ^.value := p.text),
        <.button("Add ", if (p.nextId == 0) "first" else "#" + p.nextId)
      )
    })
    .build

  def apply(p: Props) = component(p)
}
