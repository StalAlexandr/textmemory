package ru.alexandrstal.textmemory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.alexandrstal.textmemory.dao.AppDatabase
import ru.alexandrstal.textmemory.entity.Phrase
import ru.alexandrstal.textmemory.view.PhraseAdapter

class PhraseCreate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phrase_create)

        var currentPhrase:Phrase? = null

        val box =  findViewById<EditText>(R.id.phraseBox);
        val phrases =  findViewById<RecyclerView>(R.id.phrases)

        phrases.layoutManager = LinearLayoutManager(this)

        val rmListener = fun (p:Phrase) {
            Thread {
                val db = AppDatabase.invoke(this)
                db.PhraseDAO().deletePhrase(p)
            }.start()

        }

        val selectListener = fun (phrase:Phrase) {
            currentPhrase = phrase
            phrase2Text(phrase, box)
        }

        val onSaveButton = findViewById<Button>(R.id.onSave)

        onSaveButton.setOnClickListener {


            val text = text2phrase(box)

            Thread {
                val db = AppDatabase.invoke(this)
                val id = currentPhrase?.id()

                if (id!=null){
                    text.emb.id=id
                    db.PhraseDAO().updatePhrase(text)
                }
                else {
                    db.PhraseDAO().insertPhrase(text)
                }

                currentPhrase = null

            }.start()



            box.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onSaveButton.isEnabled = !((s == null)||(s.length<5))
                }
            })


            box.setText("")
        }

        findViewById<Button>(R.id.onPlay).setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

            val db = AppDatabase.invoke(this)
            db.PhraseDAO().loadAllPhrases().observe(this, Observer<List<Phrase>>{
                phrases.adapter = PhraseAdapter(
                    it,
                    this,
                    selectListener,
                    rmListener
                )
                phrases.adapter?.notifyDataSetChanged()
            })

    }

}


