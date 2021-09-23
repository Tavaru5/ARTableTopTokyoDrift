package com.tavarus.artabletop.editor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tavarus.artabletop.R
import kotlinx.android.synthetic.main.save_board_dialog.*

class SaveBoardDialog(val listener: SaveBoardListener) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.save_board_dialog, container)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeButton.setOnClickListener { dismiss() }
        saveButton.setOnClickListener {
            listener.saveBoardWithTitle(titleEditText.text.toString()) { dismiss() }
        }
        titleEditText.setOnEditorActionListener { _, _, _ ->
            listener.saveBoardWithTitle(titleEditText.text.toString()) { dismiss() }
            true
        }
    }
}

interface SaveBoardListener {
    fun saveBoardWithTitle(title: String, successCallback: () -> Unit)
}
