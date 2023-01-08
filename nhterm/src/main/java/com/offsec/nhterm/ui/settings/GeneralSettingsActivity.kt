package com.offsec.nhterm.ui.settings

import android.os.Bundle
import android.view.MenuItem
import com.offsec.nhterm.R

/**
 * @author kiva
 */
class GeneralSettingsActivity : BasePreferenceActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.title = getString(R.string.general_settings)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    addPreferencesFromResource(R.xml.setting_general)
  }

  override fun onBuildHeaders(target: MutableList<Header>?) {
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      android.R.id.home -> finish()
    }
    return item?.let { super.onOptionsItemSelected(it) }
  }
}
