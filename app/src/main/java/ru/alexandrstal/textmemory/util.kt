package ru.alexandrstal.textmemory

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import ru.alexandrstal.textmemory.entity.Phrase
import ru.alexandrstal.textmemory.entity.TextSlice

fun phraseToView(context: Context, phrase: Phrase):List<View>{

    return phrase.slices.map{

        if (it.hidden){
          val editText = EditText(context)
            editText.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            editText.setPaddingRelative(0,0,0,0)
            editText.setEms(it.text.length)
            editText.setTextIsSelectable(true)
            return@map editText
        }
        else{
            val textView =  TextView(context);
            textView.layoutParams =   ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            textView.setText(it.text)
            textView.setTextIsSelectable(true)
            return@map textView
        }
    }


}

fun text2phrase(box:EditText):Phrase{

    val slises = arrayListOf<TextSlice>()

    val prefix = box.text.subSequence(0, box.selectionStart).toString()

    if (prefix.isNotEmpty()) {
        slises.add(TextSlice(prefix, false))
    }

    val hidden = box.text.subSequence(box.selectionStart, box.selectionEnd).toString()

    if (hidden.isNotEmpty()) {
        slises.add(TextSlice(hidden, true))
    }


    val postfix = box.text.subSequence(box.selectionEnd, box.text.length).toString()

    if (postfix.isNotEmpty()) {
        slises.add(TextSlice(postfix, false))
    }

    return Phrase(slises)

}

fun phrase2Text(phrase: Phrase, box: TextView):Unit{
    var text =""
    var startSelect = 0;
    var endSelect = 0;

    phrase.slices.forEach {
        if (it.hidden){startSelect = text.length; endSelect = startSelect + it.text.length}
        text+=it.text


    }
    box.setText(text)
    if (box is EditText) {
        box.setSelection(startSelect, endSelect)
    }
}
