package com.offsec.nhterm.ui.other

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.licenses.GnuGeneralPublicLicense30
import de.psdev.licensesdialog.licenses.MITLicense
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices
import com.offsec.nhterm.App
import com.offsec.nhterm.R
import com.offsec.nhterm.utils.extractAssetsDir
import de.psdev.licensesdialog.licenses.SILOpenFontLicense11
import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * @author kiva
 */
class AboutActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.ui_about)
    setSupportActionBar(findViewById(R.id.about_toolbar))
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    try {
      val version = packageManager.getPackageInfo(packageName, 0).versionName
      (findViewById<TextView>(R.id.app_version)).text = version
    } catch (ignored: PackageManager.NameNotFoundException) {
    }

    findViewById<View>(R.id.about_licenses_view).setOnClickListener {
      val notices = Notices()
      notices.addNotice(
        Notice(
          "ADBToolkitInstaller",
          "https://github.com/Crixec/ADBToolKitsInstaller",
          "Copyright (c) 2017 Crixec",
          GnuGeneralPublicLicense30()
        )
      )
      notices.addNotice(
        Notice(
          "Android-Terminal-Emulator",
          "https://github.com/jackpal/Android-Terminal-Emulator",
          "Copyright (c) 2011-2016 Steven Luo",
          ApacheSoftwareLicense20()
        )
      )
      notices.addNotice(
        Notice(
          "ChromeLikeTabSwitcher",
          "https://github.com/michael-rapp/ChromeLikeTabSwitcher",
          "Copyright (c) 2016-2017 Michael Rapp",
          ApacheSoftwareLicense20()
        )
      )
      notices.addNotice(
        Notice(
          "Color-O-Matic",
          "https://github.com/GrenderG/Color-O-Matic",
          "Copyright 2016-2017 GrenderG",
          GnuGeneralPublicLicense30()
        )
      )
      notices.addNotice(
        Notice(
          "EventBus",
          "http://greenrobot.org",
          "Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)",
          ApacheSoftwareLicense20()
        )
      )
      notices.addNotice(
        Notice(
          "ModularAdapter",
          "https://wrdlbrnft.github.io/ModularAdapter",
          "Copyright (c) 2017 Wrdlbrnft",
          MITLicense()
        )
      )
      notices.addNotice(
        Notice(
          "RecyclerTabLayout",
          "https://github.com/nshmura/RecyclerTabLayout",
          "Copyright (C) 2017 nshmura",
          ApacheSoftwareLicense20()
        )
      )
      notices.addNotice(
        Notice(
          "RecyclerView-FastScroll",
          "Copyright (c) 2016, Tim Malseed",
          "Copyright (c) 2016, Tim Malseed",
          ApacheSoftwareLicense20()
        )
      )
      notices.addNotice(
        Notice(
          "SortedListAdapter",
          "https://wrdlbrnft.github.io/SortedListAdapter/",
          "Copyright (c) 2017 Wrdlbrnft",
          MITLicense()
        )
      )
      notices.addNotice(
        Notice(
          "Termux",
          "https://termux.com",
          "Copyright 2016-2017 Fredrik Fornwall",
          GnuGeneralPublicLicense30()
        )
      )
      notices.addNotice(
        Notice(
          "NeoTerm",
          "https://github.com/NeoTerm/NeoTerm",
          "Copyright (c) 2021 imkiva",
          GnuGeneralPublicLicense30()
        )
      )
      notices.addNotice(
        Notice(
          "Fira Code",
          "https://github.com/tonsky/FiraCode",
          "Copyright (c) 2022 Nikita Prokopov",
          SILOpenFontLicense11()
        )
      )
      notices.addNotice(
        Notice(
          "Zed Fonts",
          "https://github.com/zed-industries/zed-fonts",
          "Copyright 2015-2021, Renzhi Li (aka. Belleve Invis, belleve@typeof.net)",
          SILOpenFontLicense11()
        )
      )
      LicensesDialog.Builder(this)
        .setNotices(notices)
        .setIncludeOwnLicense(true)
        .build()
        .show()
    }

    findViewById<View>(R.id.about_version_view).setOnClickListener {
      App.get().easterEgg(this, "Emmmmmm...")
    }

    findViewById<View>(R.id.about_source_code_view).setOnClickListener {
      openUrl("https://gitlab.com/kalilinux/nethunter/apps/kali-nethunter-term")
    }

    findViewById<View>(R.id.about_reset_app_view).setOnClickListener {
      AlertDialog.Builder(this)
        .setMessage(R.string.reset_app_warning)
        .setPositiveButton(R.string.yes) { _, _ ->
          resetApp()
          resetisdone()
        }
        .setNegativeButton(android.R.string.no, null)
        .show()
    }
  }

  private fun resetisdone() {
    AlertDialog.Builder(this)
      .setMessage(R.string.done)
      .setPositiveButton(R.string.ok) { _, _ ->
        return@setPositiveButton
      }
      .show()
  }

  private fun resetApp() {
    // Manual way of resetting required assets
    Runtime.getRuntime().exec("mkdir -p "+" "+"/data/data/com.offsec.nhterm/files/usr/").waitFor()
    Executer("/system/bin/rm -rf /data/data/com.offsec.nhterm/files/usr/bin")
    Thread.sleep(1200)
    extractAssetsDir("bin", "/data/data/com.offsec.nhterm/files/usr/bin/")
    Thread.sleep(800)
    Executer("/system/bin/chmod +x /data/data/com.offsec.nhterm/files/usr/bin/bash") // Static bash for arm ( works for *64 too )
    Executer("/system/bin/chmod +x /data/data/com.offsec.nhterm/files/usr/bin/kali") // Kali chroot scriptlet
    Executer("/system/bin/chmod +x /data/data/com.offsec.nhterm/files/usr/bin/android-su") // Android su scriptlet
  }

  fun Executer(command: String?): String? {
    val output = StringBuilder()
    val p: Process
    try {
      p = Runtime.getRuntime().exec(command)
      p.waitFor()
      val reader = BufferedReader(InputStreamReader(p.inputStream))
      var line: String?
      while (reader.readLine().also { line = it } != null) {
        output.append(line).append('\n')
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return output.toString()
  }

  private fun openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(intent)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item?.itemId) {
      android.R.id.home ->
        finish()
    }
    return item?.let { super.onOptionsItemSelected(it) }
  }
}
