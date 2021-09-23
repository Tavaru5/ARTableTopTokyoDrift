package com.tavarus.artabletop.editor.bottomSheet

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tavarus.artabletop.R
import com.tavarus.artabletop.editor.TrayCallBack
import kotlinx.android.synthetic.main.bottom_sheet_item.view.*

class BottomSheetItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr)  {

    init {
        View.inflate(context, R.layout.bottom_sheet_item, this)
    }

    fun bind(pair: Pair<TrayItem, TrayCallBack>) {
        bottomItemImage.setImageResource(pair.first.sheetItem.drawable)
        bottomItemImage.contentDescription = pair.first.sheetItem.title
        setOnClickListener { pair.second() }
        bottomItemText.text = pair.first.sheetItem.title
    }
}
