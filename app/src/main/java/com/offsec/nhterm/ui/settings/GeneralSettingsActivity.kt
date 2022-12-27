package com.offsec.nhterm.ui.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import io.nhterm.R
import io.nhterm.component.config.NeoPreference
import io.nhterm.utils.runApt

/**
 * @author kiva
 */
class GeneralSettingsActivity : BasePreferenceActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.title = getString(R.string.general_settings)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    addPreferencesFromResource(R.xml.setting_general)

    val currentShell = NeoPreference.getLoginShellName()
    findPreference(getString(R.string.key_general_shell)).setOnPreferenceChangeListener { _, value ->
      val shellName = value.toString()
      val newShell = NeoPreference.findLoginProgram(shellName)
      if (newShell == null) {
        requestInstallShell(shellName, currentShell)
      } else {
        postChangeShell(shellName)
      }
      return@setOnPreferenceChangeListener true
    }
  }

  private fun postChangeShell(shellName: String) = NeoPreference.setLoginShellName(shellName)

  private fun requestInstallShell(shellName: String, currentShell: String) {
    AlertDialog.Builder(this)
      .setTitle(getString(R.string.shell_not_found, shellName))
      .setMessage(R.string.shell_not_found_message)
      .setPositiveButton(R.string.install) { _, _ ->
        runApt("install", "-y", shellName) {
          it.onSuccess { postChangeShell(shellName) }
        }
      }
      .setNegativeButton(android.R.string.no, null)
      .setOnDismissListener { postChangeShell(currentShell) }
      .show()
  }

  override fun onBuildHeaders(target: MutableList<Header>?) {
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      android.R.id.home -> finish()
    }
    return super.onOptionsItemSelected(item)
  }
}
