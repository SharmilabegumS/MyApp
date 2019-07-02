package com.example.Calendar.adapter

import android.content.Context;
import android.util.SparseBooleanArray
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView
import com.example.Calendar.R

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewHolders> {

    private var itemList = ArrayList<String>()
    private var context: Context
    var selectedItem: SparseBooleanArray? = null

    constructor(context: Context, itemList: ArrayList<String>) {
        this.itemList = itemList;
        this.context = context;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolders {
        var layoutView: View = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list, null);
        var rcv = RecyclerViewHolders(layoutView);
        selectedItem = rcv.selectedItems
        return rcv;
    }

    override fun onBindViewHolder(holder: RecyclerViewHolders, position: Int) {
        holder.songTitle!!.setText(itemList.get(position))
    }

    override fun getItemCount(): Int {
        return this.itemList.size;
    }

    fun setChecked(pos: Int) {

    }
}

