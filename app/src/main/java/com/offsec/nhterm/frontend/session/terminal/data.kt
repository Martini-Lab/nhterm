package com.offsec.nhterm.frontend.session.terminal

import com.offsec.nhterm.backend.TerminalSession
import com.offsec.nhterm.component.completion.OnAutoCompleteListener
import com.offsec.nhterm.component.session.ShellProfile
import com.offsec.nhterm.component.session.ShellTermSession
import com.offsec.nhterm.frontend.session.view.TerminalView
import com.offsec.nhterm.frontend.session.view.extrakey.ExtraKeysView

class TermSessionData {
  var termSession: TerminalSession? = null
  var sessionCallback: TermSessionCallback? = null
  var viewClient: TermViewClient? = null
  var onAutoCompleteListener: OnAutoCompleteListener? = null

  var termUI: TermUiPresenter? = null
  var termView: TerminalView? = null
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
      session: TerminalSession,
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

  fun initializeViewWith(termUI: TermUiPresenter?, termView: TerminalView?, eks: ExtraKeysView?) {
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
