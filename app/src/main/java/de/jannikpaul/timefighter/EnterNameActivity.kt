package de.jannikpaul.timefighter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class EnterNameActivity : AppCompatActivity() {


    fun speichereName(Name : String) {

        val meinePreferences: SharedPreferences = getSharedPreferences("Default", Context.MODE_PRIVATE)
        meinePreferences.edit {
            putString("Name_key", Name)
            commit()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("Eingabe", "OnResume EnterName")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_name)

        val eingabeFeld = findViewById<EditText>(R.id.editTextTextPersonName)
        eingabeFeld.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
                if (keyEvent?.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.d("Eingabe", "Enter")
                    val Inhalt = eingabeFeld.text.toString()
                    speichereName(Inhalt)
                    Log.d("Eingabe", "$Inhalt")
                    return true
                }
                return false
            }

        })

    }
}