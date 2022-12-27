package com.offsec.nhterm.frontend.session.terminal

import com.offsec.nhterm.backend.TerminalSession
import io.nhterm.component.completion.OnAutoCompleteListener
import io.nhterm.component.session.ShellProfile
import io.nhterm.component.session.ShellTermSession
import com.offsec.nhterm.frontend.session.view.TerminalView
import io.nhterm.frontend.session.view.extrakey.ExtraKeysView

class TermSessionData {
  var termSession: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession? = null
  var sessionCallback: TermSessionCallback? = null
  var viewClient: TermViewClient? = null
  var onAutoCompleteListener: OnAutoCompleteListener? = null

  var termUI: TermUiPresenter? = null
  var termView: _root_ide_package_.com.offsec.nhterm.frontend.session.view.TerminalView? = null
  var extraKeysView: ExtraKeysView? = null

  var profile: ShellProfile? = null

  fun cleanup() {
    onAutoCompleteListener?.onCleanUp()
    onAutoCompleteListener = null

    sessionCallback?.termSessionData = null
    viewClient?.termSessionData = null

    termUI = null
    termView = null
    extraKeysView = null
    termSession = null

    profile = null
  }

  fun initializeSessionWith(
      session: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession,
      sessionCallback: TermSessionCallback?,
      viewClient: TermViewClient?
  ) {
    this.termSession = session
    this.sessionCallback = sessionCallback
    this.viewClient = viewClient
    this.sessionCallback?.termSessionData = this
    this.viewClient?.termSessionData = this

    if (session is ShellTermSession) {
      profile = session.shellProfile
    }
  }

  fun initializeViewWith(termUI: TermUiPresenter?, termView: _root_ide_package_.com.offsec.nhterm.frontend.session.view.TerminalView?, eks: ExtraKeysView?) {
    this.termUI = termUI
    this.termView = termView
    this.extraKeysView = eks
  }
}

interface TermUiPresenter {
  fun requireClose()
  fun requireToggleFullScreen()
  fun requirePaste()
  fun requireUpdateTitle(title: String?)
  fun requireOnSessionFinished()
  fun requireHideIme()
  fun requireFinishAutoCompletion(): Boolean
  fun requireCreateNew()
  fun requireSwitchToPrevious()
  fun requireSwitchToNext()
  fun requireSwitchTo(index: Int)
}
