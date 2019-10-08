package com.notyy.typeflow.editor

import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, FutureBinding, dom}
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.{Event, KeyboardEvent}
import org.scalajs.dom.{Node, document}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.{Failure, Success}

object TypeFlowEditor {
  val githubUser: Var[String] = Var("")

  @dom
  def render: Binding[Node] = {
    <div class="container">
      <div class="row" id="main-window">
        <div class="col-4" id="editor-area">
          <div class="row" id="command-input-area">
            <label for="commandInput">请输入模型操作命令</label>
            <input id="commandInput"
               onkeypress={
                  event: KeyboardEvent =>
                    val code = event.charCode
                    if(code == 13) {
                      githubUser := (commandInput.value)
                    } else {
                    }
               }
                   type="text" class="form-control" placeholder="输入命令"></input>
            <button id="commandSubmitButton"
               onclick={
                  event: Event => githubUser := (commandInput.value)}
            type="submit" class="btn btn-primary">提交</button>
          </div>
          <hr/>
          <div class="row" id="show-response-area">something</div>
        </div>
        <div class="col-8" id="diagram-area">
          {val name = githubUser.bind
              if(name.isEmpty){
                <div>等待用户输入</div>
              }else {
                val githubResult = FutureBinding(Ajax.get(s"https://api.github.com/users/${name}")) // 发起 Github API 请求，
                githubResult.bind match { // 并根据 API 结果显示不同的内容：
                  case None => // 如果尚未加载完毕：
                    <div>Loading the avatar for
                      {name}
                    </div> // 显示提示信息；
                  case Some(Success(response)) => // 如果成功加载：
                    val json = JSON.parse(response.responseText) // 把回应解析成 JSON；
                      <img src={json.avatar_url.toString}/> // 并显示头像；
                  case Some(Failure(exception)) => // 如果加载时出错，
                    <div>
                      {exception.toString}
                    </div> // 显示错误信息。
                }
              }
          }
        </div>
      </div>
    </div>
    }

  @JSExportTopLevel("bindingRender")
  def bindingRender(): Unit = {
    dom.render(document.body, render)
  }
}
