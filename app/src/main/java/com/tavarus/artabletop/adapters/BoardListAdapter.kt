package com.tavarus.artabletop.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tavarus.artabletop.R
import com.tavarus.artabletop.models.Board
import kotlinx.android.synthetic.main.board_list_item.view.*

class BoardListAdapter(var boards: List<Board>, val context: Context, val onPress: (id: Long) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount(): Int {
        return (boards.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTV.text = boards[position].title
        holder.listItemContainer.setOnClickListener {
            onPress(boards[position].id)
        }
        holder.titleTV.setOnClickListener {
            onPress(boards[position].id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.board_list_item,
                parent,
                false
            )
        )
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val titleTV : TextView = view.titleTV
    val listItemContainer = view.listItemContainer
}
