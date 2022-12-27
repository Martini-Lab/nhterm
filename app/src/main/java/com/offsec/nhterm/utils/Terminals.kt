package com.offsec.nhterm.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.offsec.nhterm.backend.TerminalSession
import io.nhterm.component.ComponentManager
import io.nhterm.component.config.NeoPreference
import io.nhterm.component.font.FontComponent
import io.nhterm.component.session.SessionComponent
import io.nhterm.component.session.ShellParameter
import io.nhterm.component.session.XParameter
import io.nhterm.component.session.XSession
import com.offsec.nhterm.frontend.session.view.TerminalView
import com.offsec.nhterm.frontend.session.view.TerminalViewClient
import io.nhterm.frontend.session.view.extrakey.ExtraKeysView

/**
 * @author kiva
 */
object Terminals {
  fun setupTerminalView(terminalView: _root_ide_package_.com.offsec.nhterm.frontend.session.view.TerminalView?, terminalViewClient: _root_ide_package_.com.offsec.nhterm.frontend.session.view.TerminalViewClient? = null) {
    terminalView?.textSize = NeoPreference.getFontSize();

    val fontComponent = ComponentManager.getComponent<FontComponent>()
    fontComponent.applyFont(terminalView, null, fontComponent.getCurrentFont())

    if (terminalViewClient != null) {
      terminalView?.setTerminalViewClient(terminalViewClient)
    }
  }

  fun setupExtraKeysView(extraKeysView: ExtraKeysView?) {
    val fontComponent = ComponentManager.getComponent<FontComponent>()
    val font = fontComponent.getCurrentFont()
    fontComponent.applyFont(null, extraKeysView, font)
  }

  fun createSession(context: Context, parameter: ShellParameter): _root_ide_package_.com.offsec.nhterm.backend.TerminalSession {
    val sessionComponent = ComponentManager.getComponent<SessionComponent>()
    return sessionComponent.createSession(context, parameter)
  }

  fun createSession(activity: AppCompatActivity, parameter: XParameter): XSession {
    val sessionComponent = ComponentManager.getComponent<SessionComponent>()
    return sessionComponent.createSession(activity, parameter)
  }

  fun escapeString(s: String?): String {
    if (s == null) {
      return ""
    }

    val builder = StringBuilder()
    val specialChars = "\"\\$`!"
    builder.append('"')
    val length = s.length
    for (i in 0 until length) {
      val c = s[i]
      if (specialChars.indexOf(c) >= 0) {
        builder.append('\\')
      }
      builder.append(c)
    }
    builder.append('"')
    return builder.toString()
  }
}
