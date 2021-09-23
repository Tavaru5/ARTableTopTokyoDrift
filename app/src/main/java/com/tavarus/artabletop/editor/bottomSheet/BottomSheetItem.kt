package com.tavarus.artabletop.editor.bottomSheet

import com.tavarus.artabletop.R
import com.tavarus.artabletop.editor.EditorViewModel

 data class BottomSheetItem(
    val title: String,
    val drawable: Int
)

enum class TrayItem(val sheetItem: BottomSheetItem) {
    SAVE(BottomSheetItem("SAVE", R.drawable.ic_save)),
    PALETTE(BottomSheetItem("Palette", R.drawable.ic_palette))
}
