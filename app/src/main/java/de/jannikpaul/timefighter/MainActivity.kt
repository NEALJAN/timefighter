package de.jannikpaul.timefighter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    var sekunden = 5

    var punktestand = 0

    var highscore = -1

    var Name = ""

    fun highscoreSpeichern() {
        highscoresign()
        setSignVisibility(true)
        saveNewHighscore(punktestand)
        highscore = punktestand
        aktualisiereHighscore()
        val intent = Intent(this, EnterNameActivity::class.java)
        intent.putExtra("score", highscore)
        startActivity(intent)
    }

    fun setSignVisibility(visible: Boolean) {
        var  highscoreMessage= findViewById<TextView>(R.id.highscoreMessage)
        if (visible == true) {
            highscoreMessage.visibility = View.VISIBLE
        }
        else {
            highscoreMessage.visibility = View.INVISIBLE
        }
    }

    fun highscoresign() {
        val highscoreMessage= findViewById<TextView>(R.id.highscoreMessage)
        highscoreMessage.text ="Neuer Highscore!"
    }

    fun aktualisiereHighscore() {
        loadName()
        val highscorefield = findViewById<TextView>(R.id.highscore)
        highscorefield.text = "HighScore: $Name $highscore"
    }

    fun loadHighscore() {
        val pref: SharedPreferences = getSharedPreferences("Default", Context.MODE_PRIVATE)
        highscore = pref.getInt("highscore_key", -1)

        aktualisiereHighscore()
    }

    fun loadName() {
        val pref: SharedPreferences = getSharedPreferences("Default", Context.MODE_PRIVATE)
        Name = pref.getString("Name_key", "")!!
    }

    fun saveNewHighscore(score: Int) {
        val meinePreferences: SharedPreferences = getSharedPreferences("Default", Context.MODE_PRIVATE)
        meinePreferences.edit {
            putInt("highscore_key", score)
            commit()
        }
    }

    fun resetHighscore() {
        saveNewHighscore(0)
        highscore = 0
        aktualisiereHighscore()
    }


    var spielgestartet = false

    var resetTimer = object: CountDownTimer(2000, 1000){
        override fun onTick(p0: Long) {}

        override fun onFinish() {
            var gamebutton = findViewById<Button>(R.id.button)
            gamebutton.isEnabled = true
            gamebutton.text = "Hier drücken!"
        }

    }

    var countDownTimer = object : CountDownTimer(5000, 1000){
        override fun onTick(p0: Long) {
            sekunden = sekunden - 1
            var timer = findViewById<TextView>(R.id.timer)
            timer.text = "$sekunden s."
        }

        override fun onFinish() {
            var timer = findViewById<TextView>(R.id.timer)
            timer.text = "Ende"
            spielgestartet = false
            sekunden = 5
            var timerbutton = findViewById<Button>(R.id.button)
            timerbutton.isEnabled = false
            resetTimer.start()
            timerbutton.text = "Game Over"

            if (punktestand > highscore){

                highscoreSpeichern()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("Eingabe", "OnResume Main")
        aktualisiereHighscore()
    }

//anfang code ausführung
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Eingabe", "OnCreate")

        loadHighscore()

        aktualisierePunktestand()

        val button = findViewById<Button>(R.id.button)
        button.text = "hier drücken"
        button.setOnClickListener {
            if (!spielgestartet) //"!" kehrt boolean um
            {
                setSignVisibility(false)
                spielgestartet = true
                punktestand = 0
                aktualisierePunktestand()
                countDownTimer.start()
            }
            buttonWurdeGedrueckt()
        }
    }

    fun buttonWurdeGedrueckt(){
        punktestand = punktestand + 1
        val punkteanimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        val punkteBg = findViewById<TextView>(R.id.punkteBg)
        punkteBg.visibility = View.VISIBLE
        punkteBg.startAnimation(punkteanimation)
        aktualisierePunktestand()

        val ersteAnimation = AnimationUtils.loadAnimation(this,R.anim.bounce)
        val button = findViewById<Button>(R.id.button)
        button.startAnimation(ersteAnimation)
    }

    fun aktualisierePunktestand(){
        val punkte = findViewById<TextView>(R.id.punkte)
        punkte.text = "Punkte: $punktestand"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item.itemId == R.id.list) {
            //TODO: in activity springen
            Log.d("unser_Menu", "in activity springen")
            val intent = Intent(this, HighscoreActivity::class.java)
            startActivity(intent)
        }
        else if (item.itemId == R.id.reset) {
            resetHighscore()
        }
        else if (item.itemId == R.id.inputName) {
            val intent = Intent(this, EnterNameActivity::class.java)
            startActivity(intent)
        }

        return true
    }
}

