package com.tavarus.artabletop.editor.bottomSheet

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.tavarus.artabletop.R
import com.tavarus.artabletop.editor.TrayCallBack
import kotlinx.android.synthetic.main.bottom_sheet_view.view.*

class EditorBottomSheetView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.bottom_sheet_view, this)
    }

    fun bind(bottomSheetItems: List<Pair<TrayItem, TrayCallBack>>) {
        bottomItem0.bind(bottomSheetItems[0])
        bottomItem1.bind(bottomSheetItems[1])
    }

}
