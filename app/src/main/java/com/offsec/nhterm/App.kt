package com.offsec.nhterm

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import io.nhterm.component.NeoInitializer
import io.nhterm.component.config.NeoPreference
import io.nhterm.ui.other.BonusActivity
import io.nhterm.utils.CrashHandler

/**
 * @author kiva
 */
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    _root_ide_package_.com.offsec.nhterm.App.Companion.app = this
    NeoPreference.init(this)
    CrashHandler.init()
    NeoInitializer.init(this)
  }

  fun errorDialog(context: Context, message: Int, dismissCallback: (() -> Unit)?) {
    errorDialog(context, getString(message), dismissCallback)
  }

  fun errorDialog(context: Context, message: String, dismissCallback: (() -> Unit)?) {
    AlertDialog.Builder(context)
      .setTitle(_root_ide_package_.io.nhterm.R.string.error)
      .setMessage(message)
      .setNegativeButton(android.R.string.no, null)
      .setPositiveButton(_root_ide_package_.io.nhterm.R.string.show_help) { _, _ ->
        openHelpLink()
      }
      .setOnDismissListener {
        dismissCallback?.invoke()
      }
      .show()
  }

  fun openHelpLink() {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://neoterm.gitbooks.io/neoterm-wiki/content/"))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
  }

  fun easterEgg(context: Context, message: String) {
    val happyCount = NeoPreference.loadInt(NeoPreference.KEY_HAPPY_EGG, 0) + 1
    NeoPreference.store(NeoPreference.KEY_HAPPY_EGG, happyCount)

    val trigger = NeoPreference.VALUE_HAPPY_EGG_TRIGGER

    if (happyCount == trigger / 2) {
      @SuppressLint("ShowToast")
      val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
      toast.setGravity(Gravity.CENTER, 0, 0)
      toast.show()
    } else if (happyCount > trigger) {
      NeoPreference.store(NeoPreference.KEY_HAPPY_EGG, 0)
      context.startActivity(Intent(context, BonusActivity::class.java))
    }
  }

  companion object {
    private var app: _root_ide_package_.com.offsec.nhterm.App? = null

    fun get(): _root_ide_package_.com.offsec.nhterm.App {
      return _root_ide_package_.com.offsec.nhterm.App.Companion.app!!
    }
  }
}
