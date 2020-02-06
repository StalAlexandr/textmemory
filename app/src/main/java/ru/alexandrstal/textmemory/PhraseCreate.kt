package ru.alexandrstal.textmemory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alexandrstal.textmemory.entity.PhraseAdapter
import ru.alexandrstal.textmemory.entity.PhraseStorage

class PhraseCreate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phrase_create)

        var currentItemPosition = -1;

        val box =  findViewById<EditText>(R.id.phraseBox);
        val phrases =  findViewById<RecyclerView>(R.id.phrases)

        phrases.layoutManager = LinearLayoutManager(this)

        val rmListener = fun (i:Int) { PhraseStorage.phrases.removeAt(i);
            phrases.adapter?.notifyDataSetChanged()
            currentItemPosition=-1
        }

        val selectListener = fun (i:Int) { currentItemPosition=i
            val phrase = PhraseStorage.phrases[currentItemPosition]
            phrase2Text(phrase, box)
        }

        phrases.adapter = PhraseAdapter(PhraseStorage.phrases, this,selectListener,rmListener)

        findViewById<Button>(R.id.onSave).setOnClickListener {
            val text = text2phrase(box)
            if (currentItemPosition == -1) {
                PhraseStorage.phrases.add(text)
            } else{
                PhraseStorage.phrases[currentItemPosition]=text
            }
            box.setText("")
            currentItemPosition=-1
            phrases.adapter?.notifyDataSetChanged()

        }

        findViewById<Button>(R.id.onPlay).setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}


