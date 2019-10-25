package com.tavarus.artabletop.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tavarus.artabletop.App
import com.tavarus.artabletop.R
import com.tavarus.artabletop.viewModels.EditorViewModel
import kotlinx.android.synthetic.main.editor_fragment.*
import javax.inject.Inject

class EditorFragment : Fragment() {

    @Inject
    lateinit var editorViewModel: EditorViewModel

    override fun onAttach(context: Context) {
        val coreComponent = (activity?.applicationContext as App).provideCoreComponent()
        coreComponent.componentManager().getOrCreateBoardComponent(activity?.applicationContext as Context, coreComponent).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.editor_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boardGrid.board = editorViewModel.board.value!!

    }

}
