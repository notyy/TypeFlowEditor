package com.notyy.typeflow.editor

import com.thoughtworks.binding.Binding.{BindingSeq, Var, Vars}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.{Node, document}
import org.scalajs.dom.html.Table
import org.scalajs.dom.raw.Event

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
 * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
 */
//@JSExport
object SampleMain {

  case class Contact(name: Var[String], email: Var[String])

  val data = Vars.empty[Contact]

  @dom
  def table: Binding[BindingSeq[Node]] = {
    <div>
      <button
      onclick={ event: Event =>
        data.value += Contact(Var("Yang Bo"), Var("yang.bo@rea-group.com"))
      }
      >
        Add a contact
      </button>
    </div>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>E-mail</th>
        </tr>
      </thead>
      <tbody>
        {
        for (contact <- data) yield {
          <tr>
            <td>
              {contact.name.bind}
            </td>
            <td>
              {contact.email.bind}
            </td>
            <td>
              <button
              onclick={ event: Event =>
                contact.name.value = "Modified Name"
              }
              >
                Modify the name
              </button>
            </td>
          </tr>
        }
        }
      </tbody>
    </table>
  }

  @JSExportTopLevel("showTable")
  def showTable(): Unit = {
    dom.render(document.body, table)
  }

}
