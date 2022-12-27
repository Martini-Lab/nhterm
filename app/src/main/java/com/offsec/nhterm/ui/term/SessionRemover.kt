package com.offsec.nhterm.ui.term

import com.offsec.nhterm.backend.TerminalSession
import io.nhterm.component.session.XSession
import io.nhterm.services.NeoTermService

/**
 * @author kiva
 */
object SessionRemover {
  fun removeSession(termService: NeoTermService?, tab: TermTab) {
    tab.termData.termSession?.finishIfRunning()
    removeFinishedSession(termService, tab.termData.termSession)
    tab.cleanup()
  }

  fun removeXSession(termService: NeoTermService?, tab: XSessionTab?) {
    removeFinishedSession(termService, tab?.session)
  }

  private fun removeFinishedSession(termService: NeoTermService?, finishedSession: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession?) {
    if (termService == null || finishedSession == null) {
      return
    }

    termService.removeTermSession(finishedSession)
  }

  private fun removeFinishedSession(termService: NeoTermService?, finishedSession: XSession?) {
    if (termService == null || finishedSession == null) {
      return
    }

    termService.removeXSession(finishedSession)
  }
}
