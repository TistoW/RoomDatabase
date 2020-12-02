package com.inyongtisto.roomdatabase.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.inyongtisto.roomdatabase.R
import com.inyongtisto.roomdatabase.model.NoteModel

class AdapterNote(private var mData: List<NoteModel>, var listener: Listeners) : RecyclerView.Adapter<AdapterNote.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val view = layoutInflater.inflate(R.layout.item_note, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val a = mData[i]

        holder.tvTitle.text = a.title
        holder.tvDesc.text = a.title
        holder.layout.setOnClickListener {
            listener.onClicked(a)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    interface Listeners {
        fun onClicked(note: NoteModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        var tvDesc: TextView = itemView.findViewById(R.id.tv_desc)
        var layout: CardView = itemView.findViewById(R.id.layout)
    }
}