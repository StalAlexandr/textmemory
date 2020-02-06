package ru.alexandrstal.textmemory.entity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.phrase_list_item.view.*
import ru.alexandrstal.textmemory.R
import ru.alexandrstal.textmemory.phrase2Text


class PhraseAdapter(val items: ArrayList<Phrase>, val context: Context, var clickListener: (i:Int) -> Unit, var longClickListener:  (i:Int) -> Unit) : RecyclerView.Adapter<ViewHolder>() {



    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.phrase_list_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.phrase_text.setOnClickListener({clickListener(position)})

        holder.phrase_text.setOnLongClickListener({longClickListener(position); true})

        phrase2Text(items.get(position), holder.phrase_text)
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    val phrase_text = view.phrase_text
}