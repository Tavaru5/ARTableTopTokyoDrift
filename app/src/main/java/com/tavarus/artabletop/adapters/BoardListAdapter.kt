package com.tavarus.artabletop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tavarus.artabletop.R
import com.tavarus.artabletop.dataModels.Board
import kotlinx.android.synthetic.main.board_list_item.view.*

class BoardListAdapter(var boards: List<Pair<String, Board>>, val context: Context, val onPress: (id: String) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount(): Int {
        return (boards.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTV.text = boards[position].second.title
        holder.listItemContainer.setOnClickListener {
            onPress(boards[position].first)
        }
        holder.titleTV.setOnClickListener {
            onPress(boards[position].first)
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
