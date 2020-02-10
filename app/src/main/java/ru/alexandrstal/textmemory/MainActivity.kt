package ru.alexandrstal.textmemory

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.alexandrstal.textmemory.dao.AppDatabase
import ru.alexandrstal.textmemory.entity.Phrase
import kotlin.random.Random


enum class PlayState{
    NOTREADY, NOANSWER, INCORRECT, CORRECT
}

class MainActivity : AppCompatActivity() {

    private var currentPhrase: Phrase = Phrase(arrayListOf());

    var list =  listOf<Phrase>()

    val state = MutableLiveData<PlayState>()


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {

        state.postValue(PlayState.NOTREADY)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        state.postValue(PlayState.NOTREADY)
        Observable.just {
            AppDatabase.invoke(this)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                AppDatabase
                    .invoke(this)
                    .PhraseDAO()
                    .loadAllPhrases()
                    .observe(this, Observer { list = it; postInit()  })
                state.postValue(PlayState.NOANSWER)
            }

        val onCheckButton = findViewById<Button>(R.id.onCheck)

        val onNextButton = findViewById<Button>(R.id.onNext)

        val onEditButton = findViewById<Button>(R.id.onEdit)



        onCheckButton.setOnClickListener{
            onCheckPhrase()
        }

        onNextButton.setOnClickListener{
            onNextPhrase()
        }


        onEditButton.setOnClickListener{
            onEditPhrase()
        }


        val observer = Observer<PlayState> { state ->

            when (state){
                PlayState.NOTREADY -> {
                    onCheckButton.isEnabled = false
                    onEditButton.isEnabled = false
                    onNextButton.isEnabled = false
                }
                PlayState.NOANSWER -> {
                    onCheckButton.isEnabled = true
                    onEditButton.isEnabled = true
                    onNextButton.isEnabled = false
                }
                PlayState.INCORRECT -> {
                    onCheckButton.isEnabled = false
                    onEditButton.isEnabled = true
                    onNextButton.isEnabled = true
                }
                PlayState.CORRECT -> {
                    onCheckButton.isEnabled = false
                    onEditButton.isEnabled = true
                    onNextButton.isEnabled = true
                }
            }
        }

        state.observe(this, observer)
    }

    private fun postInit() {
       if (!list.isEmpty()){
           onNextPhrase()
           state.postValue(PlayState.NOANSWER)
       }
        else {
           onEditPhrase()
       }

    }

    private fun onEditPhrase() {
        startActivity(Intent(this, PhraseCreate::class.java))
    }

    @SuppressLint("SetTextI18n")
    private fun onCheckPhrase() {

        val flex = findViewById<ViewGroup>(R.id.flexboxlayout)

        val textBox = flex.children.firstOrNull({it is EditText}) as EditText


        val myAnswer =  textBox.text.toString()
        val answer = currentPhrase.slices.firstOrNull({it.hidden})?.text.toString()


        if (myAnswer.trim().contentEquals(answer.trim())){
            textBox.setTextColor(Color.GREEN)
            state.postValue(PlayState.CORRECT)
        }
        else {
            textBox.setTextColor(Color.RED)
            textBox.setText("$myAnswer -> $answer")
            state.postValue(PlayState.INCORRECT)
        }
    }

    private fun onNextPhrase() {
        val flex = findViewById<ViewGroup>(R.id.flexboxlayout)
        flex.removeAllViews()

        val rnd = Random.nextInt(0,list.size);

        currentPhrase = list[rnd]

        phraseToView(this, currentPhrase).forEach { flex.addView(it) }
        state.postValue(PlayState.NOANSWER)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }



fun rmAll(){
    Thread {
        AppDatabase
            .invoke(this).PhraseDAO().selectAllPhrases().forEach {
                AppDatabase
                    .invoke(this).PhraseDAO().deletePhrase(it)
            }
    }.start()
}

}


