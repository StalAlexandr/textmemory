package ru.alexandrstal.textmemory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.children
import androidx.room.Room
import ru.alexandrstal.textmemory.dao.AppDatabase
import ru.alexandrstal.textmemory.entity.Phrase

import ru.alexandrstal.textmemory.entity.PhraseStorage
import ru.alexandrstal.textmemory.entity.TextSlice
import kotlin.random.Random

class MainActivity : AppCompatActivity() {




    private var currentPhrase: Phrase = Phrase(arrayListOf());

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Thread {

            val db = AppDatabase.invoke(this)

            /*
              val db = Room.databaseBuilder(
                  applicationContext,
                  AppDatabase::class.java, "phrases.db"
              ).build()

  */


            val list = db.PhraseDAO().selectAllPhrases()

            list.forEach({db.PhraseDAO().deletePhrase(it)})


            val p = Phrase(arrayListOf(TextSlice(0, "hello", false)))

            db.PhraseDAO().insertPhrase(p)

        }.start( )

        findViewById<Button>(R.id.onCheck).setOnClickListener{
            onCheckPhrase()
        }

        findViewById<Button>(R.id.onNext).setOnClickListener{
            onNextPhrase()
        }


        findViewById<Button>(R.id.onEdit).setOnClickListener{
            onEditPhrase()
        }



        if (!PhraseStorage.phrases.isEmpty()){
            onNextPhrase()
        }
        else{
            onEditPhrase()
        }

    }

    private fun onEditPhrase() {
        startActivity(Intent(this, PhraseCreate::class.java))
    }

    private fun onCheckPhrase() {

        val flex = findViewById<ViewGroup>(R.id.flexboxlayout)
        val myAnswer = ( flex.children.firstOrNull({it is EditText}) as EditText?)?.text.toString()
        val answer = currentPhrase.slices.firstOrNull({it.hidden})?.text.toString()


        Log.i("ANSWER", "$myAnswer $answer")
        Log.i("ANSWER", "${myAnswer.trim().contentEquals(answer.trim())}")


    }

    private fun onNextPhrase() {
        val flex = findViewById<ViewGroup>(R.id.flexboxlayout)
        flex.removeAllViews()

        val rnd = Random(5).nextInt(0, PhraseStorage.phrases.size);

        currentPhrase = PhraseStorage.phrases[rnd]


        phraseToView(this, currentPhrase).forEach { flex.addView(it) }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }





}
