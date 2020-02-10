package ru.alexandrstal.textmemory.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.phrase_list_item.view.*
import ru.alexandrstal.textmemory.R
import ru.alexandrstal.textmemory.entity.Phrase
import ru.alexandrstal.textmemory.phrase2Text


class PhraseAdapter(val items: List<Phrase>, val context: Context, var clickListener: (i: Phrase) -> Unit, var longClickListener:  (i: Phrase) -> Unit) : RecyclerView.Adapter<PhraseAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.phrase_list_item, parent, false)
        )
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val phrase = items.get(position)

        holder.phrase_text.setOnClickListener({clickListener(phrase)})

        holder.phrase_text.setOnLongClickListener({longClickListener(phrase); true})

        phrase2Text(phrase, holder.phrase_text)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val phrase_text = view.phrase_text
    }

}