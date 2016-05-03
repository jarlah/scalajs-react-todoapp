package scalajsreact.template.components

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactComponentB, _}

/**
  * Created by jarlandre on 01/05/16.
  */
object TodoFormComponent {
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
