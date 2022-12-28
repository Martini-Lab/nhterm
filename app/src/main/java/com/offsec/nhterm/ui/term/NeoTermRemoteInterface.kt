package com.offsec.nhterm.ui.term

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.offsec.nhterm.App
import com.offsec.nhterm.R
import com.offsec.nhterm.bridge.Bridge.*
import com.offsec.nhterm.bridge.SessionId
import com.offsec.nhterm.component.ComponentManager
import com.offsec.nhterm.component.config.NeoPreference
import com.offsec.nhterm.component.session.ShellParameter
import com.offsec.nhterm.component.userscript.UserScript
import com.offsec.nhterm.component.userscript.UserScriptComponent
import com.offsec.nhterm.frontend.session.terminal.TermSessionCallback
import com.offsec.nhterm.services.NeoTermService
import com.offsec.nhterm.utils.Terminals
import com.offsec.nhterm.utils.getPathOfMediaUri
import java.io.File

/**
 * @author kiva
 */
class NeoTermRemoteInterface : AppCompatActivity(), ServiceConnection {
  private var termService: NeoTermService? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val serviceIntent = Intent(this, NeoTermService::class.java)
    startService(serviceIntent)
    if (!bindService(serviceIntent, this, 0)) {
      App.get().errorDialog(this, R.string.service_connection_failed, { finish() })
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    if (termService != null) {
      if (termService!!.sessions.isEmpty()) {
        termService!!.stopSelf()
      }
      termService = null
      unbindService(this)
    }
  }

  override fun onServiceDisconnected(name: ComponentName?) {
    if (termService != null) {
      finish()
    }
  }

  override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
    termService = (service as NeoTermService.NeoTermBinder).service
    if (termService == null) {
      finish()
      return
    }

    handleIntent()
  }

  private fun handleIntent() = when (intent.component?.className?.substringAfterLast('.')) {
    "TermHere" -> handleTermHere()
    "UserScript" -> handleUserScript()
    else -> handleNormal()
  }

  private fun handleNormal() {
    when (intent.action) {
      ACTION_EXECUTE -> {
        if (!intent.hasExtra(EXTRA_COMMAND)) {
          App.get().errorDialog(this, R.string.no_command_extra)
          { finish() }
          return
        }
        val executablePath = intent.getStringExtra(EXTRA_EXECUTABLE)
        val command = intent.getStringExtra(EXTRA_COMMAND)
        val foreground = intent.getBooleanExtra(EXTRA_FOREGROUND, true)
        val session = intent.getStringExtra(EXTRA_SESSION_ID)

        openTerm(executablePath, command, SessionId.of(session), foreground)
      }

      else -> openTerm(null, null)
    }
    finish()
  }

  private fun handleTermHere() {
    if (intent.hasExtra(Intent.EXTRA_STREAM)) {
      val extra = intent.extras?.get(Intent.EXTRA_STREAM)
      if (extra is Uri) {
        val path = this.getPathOfMediaUri(extra)
        val file = File(path)
        val dirPath = if (file.isDirectory) path else file.parent
        val command = "cd " + Terminals.escapeString(dirPath)
        openTerm(command, null)
      }
      finish()
    } else {
      App.get().errorDialog(
        this,
        getString(R.string.unsupported_term_here, intent?.toString())
      ) {
        finish()
      }
    }
  }

  private fun handleUserScript() {
    val filesToHandle = mutableListOf<String>()
    val comp = ComponentManager.getComponent<UserScriptComponent>()
    val userScripts = comp.userScripts
    if (userScripts.isEmpty()) {
      App.get().errorDialog(this, R.string.no_user_script_found, { finish() })
      return
    }

    if (intent.hasExtra(Intent.EXTRA_STREAM)) {
      // action send
      val extra = intent.extras?.get(Intent.EXTRA_STREAM)

      when (extra) {
        is ArrayList<*> -> {
          extra.takeWhile { it is Uri }
            .mapTo(filesToHandle) {
              val uri = it as Uri
              File(this.getPathOfMediaUri(uri)).absolutePath
            }
        }
        is Uri -> {
          filesToHandle.add(File(this.getPathOfMediaUri(extra)).absolutePath)
        }
      }
    } else if (intent.data != null) {
      // action view
      filesToHandle.add(File(intent.data?.path).absolutePath)
    }

    if (filesToHandle.isNotEmpty()) {
      setupUserScriptView(filesToHandle, userScripts)
    } else {
      App.get().errorDialog(
        this,
        getString(R.string.no_files_selected, intent?.toString())
      ) { finish() }
    }
  }

  private fun setupUserScriptView(filesToHandle: MutableList<String>, userScripts: List<UserScript>) {
    setContentView(R.layout.ui_user_script_list)
    val filesList = findViewById<ListView>(R.id.user_script_file_list)
    val filesAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filesToHandle)
    filesList.adapter = filesAdapter
    filesList.setOnItemClickListener { _, _, position, _ ->
      AlertDialog.Builder(this@NeoTermRemoteInterface)
        .setMessage(R.string.confirm_remove_file_from_list)
        .setPositiveButton(android.R.string.yes) { _, _ ->
          filesToHandle.removeAt(position)
          filesAdapter.notifyDataSetChanged()
        }
        .setNegativeButton(android.R.string.no, null)
        .show()
    }

    val scriptsList = findViewById<ListView>(R.id.user_script_script_list)
    val scriptsListItem = mutableListOf<String>()
    userScripts.mapTo(scriptsListItem, { it.scriptFile.nameWithoutExtension })

    val scriptsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scriptsListItem)
    scriptsList.adapter = scriptsAdapter

    scriptsList.setOnItemClickListener { _, _, position, _ ->
      val userScript = userScripts[position]
      val userScriptPath = userScript.scriptFile.absolutePath
      val arguments = buildUserScriptArgument(userScriptPath, filesToHandle)

      openCustomExecTerm(userScriptPath, arguments, userScript.scriptFile.parent)
      finish()
    }
  }

  private fun buildUserScriptArgument(userScriptPath: String, files: List<String>): Array<String> {
    val arguments = mutableListOf(userScriptPath)
    arguments.addAll(files)
    return arguments.toTypedArray()
  }

  private fun openTerm(
    parameter: ShellParameter,
    foreground: Boolean = true
  ) {
    val session = termService!!.createTermSession(parameter)

    val data = Intent()
    data.putExtra(EXTRA_SESSION_ID, session.mHandle)
    setResult(RESULT_OK, data)

    if (foreground) {
      // Set current session to our new one
      // In order to switch to it when entering NeoTermActivity
      NeoPreference.storeCurrentSession(session)

      ////
      // All needed for clean start
      // ( also takes care of ghost sessions that happened before... )
      val intent = Intent(this, NeoTermActivity::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
      intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
      startActivity(intent)
    }
  }

  private fun openTerm(
    executablePath: String?,
    initialCommand: String?,
    sessionId: SessionId? = null,
    foreground: Boolean = true
  ) {
    val parameter = ShellParameter()
      .executablePath(executablePath)
      .initialCommand(initialCommand)
      .callback(TermSessionCallback())
      .systemShell(detectSystemShell())
      .session(sessionId)
    openTerm(parameter, foreground)
  }

  private fun openCustomExecTerm(executablePath: String?, arguments: Array<String>?, cwd: String?) {
    val parameter = ShellParameter()
      .executablePath(executablePath)
      .arguments(arguments)
      .currentWorkingDirectory(cwd)
      .callback(TermSessionCallback())
      .systemShell(detectSystemShell())
    openTerm(parameter)
  }

  private fun detectSystemShell(): Boolean {
    return false
  }
}
