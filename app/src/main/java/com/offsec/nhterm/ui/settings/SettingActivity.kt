package com.offsec.nhterm.ui.settings

import android.os.Bundle
import android.view.MenuItem
import com.offsec.nhterm.R

/**
 * @author Lody
 */
class SettingActivity : BasePreferenceActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.title = getString(R.string.settings)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    addPreferencesFromResource(R.xml.settings_main)
  }

  override fun onBuildHeaders(target: MutableList<Header>?) {
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      android.R.id.home ->
        finish()
    }
    return item?.let { super.onOptionsItemSelected(it) }
  }
}
