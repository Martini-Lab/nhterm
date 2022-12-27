package com.offsec.nhterm.ui.customize

import android.annotation.SuppressLint
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.nhterm.R
import com.offsec.nhterm.backend.TerminalSession
import io.nhterm.component.config.NeoTermPath
import io.nhterm.component.session.ShellParameter
import io.nhterm.frontend.session.terminal.BasicSessionCallback
import io.nhterm.frontend.session.terminal.BasicViewClient
import com.offsec.nhterm.frontend.session.view.TerminalView
import io.nhterm.frontend.session.view.extrakey.ExtraKeysView
import io.nhterm.utils.Terminals

/**
 * @author kiva
 */
@SuppressLint("Registered")
open class BaseCustomizeActivity : AppCompatActivity() {
  lateinit var terminalView: _root_ide_package_.com.offsec.nhterm.frontend.session.view.TerminalView
  lateinit var viewClient: BasicViewClient
  lateinit var sessionCallback: BasicSessionCallback
  lateinit var session: _root_ide_package_.com.offsec.nhterm.backend.TerminalSession
  lateinit var extraKeysView: ExtraKeysView

  fun initCustomizationComponent(layoutId: Int) {
    setContentView(layoutId)

    val toolbar = findViewById<Toolbar>(R.id.custom_toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    terminalView = findViewById(R.id.terminal_view)
    extraKeysView = findViewById(R.id.custom_extra_keys)
    viewClient = BasicViewClient(terminalView)
    sessionCallback = BasicSessionCallback(terminalView)
    Terminals.setupTerminalView(terminalView, viewClient)
    Terminals.setupExtraKeysView(extraKeysView)

    val script = resources.getStringArray(R.array.custom_preview_script_colors)
    val parameter = ShellParameter()
      .executablePath("${NeoTermPath.USR_PATH}/bin/echo")
      .arguments(arrayOf("echo", "-e", *script))
      .callback(sessionCallback)
      .systemShell(false)

    session = Terminals.createSession(this, parameter)
    terminalView.attachSession(session)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      android.R.id.home -> finish()
    }
    return super.onOptionsItemSelected(item)
  }
}
