package com.offsec.nhterm.frontend.session.terminal

import android.content.Context
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.offsec.nhterm.backend.TerminalSession
import io.nhterm.component.config.NeoPreference
import com.offsec.nhterm.frontend.session.view.TerminalView
import com.offsec.nhterm.frontend.session.view.TerminalViewClient

open class BasicSessionCallback(var terminalView: _root_ide_package_.com.offsec.nhterm.frontend.session.view.TerminalView) : _root_ide_package_.com.offsec.nhterm.backend.TerminalSession.SessionChangedCallback {
  override fun onTextChanged(changedSession: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession?) {
    if (changedSession != null) {
      terminalView.onScreenUpdated()
    }
  }

  override fun onTitleChanged(changedSession: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession?) {
  }

  override fun onSessionFinished(finishedSession: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession?) {
  }

  override fun onClipboardText(session: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession?, text: String?) {
  }

  override fun onBell(session: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession?) {
  }

  override fun onColorsChanged(session: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession?) {
    if (session != null) {
      terminalView.onScreenUpdated()
    }
  }
}

class BasicViewClient(val terminalView: _root_ide_package_.com.offsec.nhterm.frontend.session.view.TerminalView) :
    _root_ide_package_.com.offsec.nhterm.frontend.session.view.TerminalViewClient {
  override fun onScale(scale: Float): Float {
    if (scale < 0.9f || scale > 1.1f) {
      val increase = scale > 1f
      val changedSize = (if (increase) 1 else -1) * 2
      val fontSize = NeoPreference.validateFontSize(terminalView.textSize + changedSize)
      terminalView.textSize = fontSize
      return 1.0f
    }
    return scale
  }

  override fun onSingleTapUp(e: MotionEvent?) {
    if (terminalView.isFocusable && terminalView.isFocusableInTouchMode) {
      (terminalView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .showSoftInput(terminalView, InputMethodManager.SHOW_IMPLICIT)
    }
  }

  override fun shouldBackButtonBeMappedToEscape(): Boolean {
    return false
  }

  override fun copyModeChanged(copyMode: Boolean) {
  }

  override fun onKeyDown(keyCode: Int, e: KeyEvent?, session: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession?): Boolean {
    return false
  }

  override fun onKeyUp(keyCode: Int, e: KeyEvent?): Boolean {
    return false
  }

  override fun readControlKey(): Boolean {
    return false
  }

  override fun readAltKey(): Boolean {
    return false
  }

  override fun onCodePoint(codePoint: Int, ctrlDown: Boolean, session: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession?): Boolean {
    return false
  }

  override fun onLongPress(event: MotionEvent?): Boolean {
    return false
  }
}
