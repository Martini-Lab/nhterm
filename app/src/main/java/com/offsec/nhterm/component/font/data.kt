package com.offsec.nhterm.component.font

import android.graphics.Typeface
import com.offsec.nhterm.frontend.session.view.TerminalView
import io.nhterm.frontend.session.view.extrakey.ExtraKeysView
import java.io.File

class NeoFont {
  private var fontFile: File? = null
  private var typeface: Typeface? = null

  constructor(fontFile: File) {
    this.fontFile = fontFile
  }

  constructor(typeface: Typeface) {
    this.typeface = typeface
  }

  internal fun applyFont(terminalView: _root_ide_package_.com.offsec.nhterm.frontend.session.view.TerminalView?, extraKeysView: ExtraKeysView?) {
    val typeface = getTypeFace()
    terminalView?.setTypeface(typeface)
    extraKeysView?.setTypeface(typeface)
  }

  private fun getTypeFace(): Typeface? {
    if (typeface == null && fontFile == null) {
      return null
    }

    if (typeface == null) {
      typeface = Typeface.createFromFile(fontFile)
    }
    return typeface
  }
}
