package com.tavarus.artabletop.editor

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tavarus.artabletop.App
import com.tavarus.artabletop.R
import com.tavarus.artabletop.dataModels.toFirebaseObject
import com.tavarus.artabletop.editor.bottomSheet.TrayItem
import kotlinx.android.synthetic.main.editor_fragment.*
import javax.inject.Inject

class EditorFragment : Fragment(), SaveBoardListener {

    // We create the drawer view in here, viewstate has a 'selected'
    // Clicking in drawer view schnabbers a change in the board, which will be updated by observation

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

    override fun onResume() {
        super.onResume()
        editorViewModel.board.observe(this, Observer {newBoard ->
            boardGrid.board = newBoard
        })
        editorViewModel.trayItems.observe(this, Observer { trayItems ->
            val newTrayItems = trayItems.map { (trayItem, callBack) ->
                if (trayItem == TrayItem.SAVE) {
                    Pair(trayItem, { openSaveBoard() })
                } else {
                    Pair(trayItem, callBack)
                }
            }
            bottomSheetView.bind(newTrayItems)
        })
    }

    fun openSaveBoard() {
        val dialog = SaveBoardDialog(this)
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }

    override fun saveBoardWithTitle(title: String, successCallback: () -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val doc = firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
        val savingBoard = boardGrid.board.toFirebaseObject()
        savingBoard.title = title
        doc.update("boards", FieldValue.arrayUnion(savingBoard))
            .addOnSuccessListener {
                Log.d("KOG", "Board saved successfully!")
                successCallback()
            }
            .addOnFailureListener { e -> Log.w("KOG", "Error saving board", e) }
    }

}
