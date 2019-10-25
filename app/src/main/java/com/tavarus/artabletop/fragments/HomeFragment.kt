package com.tavarus.artabletop.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.tavarus.artabletop.App
import com.tavarus.artabletop.R
import com.tavarus.artabletop.adapters.BoardListAdapter
import com.tavarus.artabletop.models.BoardList
import com.tavarus.artabletop.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModel: HomeViewModel

    private lateinit var boardListAdapter: BoardListAdapter

    override fun onAttach(context: Context) {
        val coreComponent = (activity?.applicationContext as App).provideCoreComponent()
        coreComponent.componentManager().getOrCreateBoardComponent(activity?.applicationContext as Context, coreComponent).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Show loading

        boardListAdapter = BoardListAdapter(viewModel.boardsList.value!!.boards.toList(), context!!) { id: String ->
            viewModel.goToBoard(id)
        }

        boardsRV.layoutManager = GridLayoutManager(context, 2)
        boardsRV.adapter = boardListAdapter

        val boardObserver = Observer<BoardList> { boards ->
            if (boards?.boards?.size ?: 0 > 0) {
                boardListAdapter.boards = boards.boards.toList()
                boardListAdapter.notifyDataSetChanged()
            }
            // TODO: Hide loading
        }

        viewModel.boardsList.observe(this, boardObserver)

        signOutButton.setOnClickListener { viewModel.signOut() }

        addBoardFab.setOnClickListener { viewModel.goToAddBoard() }

    }
}
