package com.example.Calendar.adapter

import android.util.SparseBooleanArray
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView
import com.example.Calendar.R

class RecyclerViewHolders : RecyclerView.ViewHolder, View.OnClickListener {

    var songTitle: TextView? = null


    var selectedItems: SparseBooleanArray = SparseBooleanArray();

    constructor(itemView: View) : super(itemView) {

        itemView.setOnClickListener(this);
        songTitle = itemView.findViewById(R.id.profile_name);

    }

    override fun onClick(view: View) {
        selectedItems.put(getAdapterPosition(), true);
        if (selectedItems.get(getAdapterPosition(), false)) {
            selectedItems.delete(getAdapterPosition());
            view.setSelected(false);
        } else {

            view.setSelected(true);
        }

    }

    fun setSelected(pos: Int) {
        selectedItems.put(pos, true)
    }
}