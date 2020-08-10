package com.example.jarvis

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_talk.*
import java.util.*

class TalkActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk)

        talkButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                speak("Hello, I'm Jarvis")
            }
        })
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { textToSpeech.language = Locale.UK })
    }

    fun speak(talk: String) {
        textToSpeech.speak(talk, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}