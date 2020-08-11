package com.example.jarvis.talk

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.jarvis.R
import kotlinx.android.synthetic.main.activity_talk.*

class TalkActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech
    private lateinit var viewModel: TalkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk)

        viewModel = ViewModelProvider(this).get(TalkViewModel::class.java)
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            textToSpeech.voice = createVoices()
        }, "com.google.android.tts")

        talkButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                speak("Hello, I'm Jarvis")
            }
        })
    }

    fun speak(talk: String) {
        textToSpeech.speak(talk, TextToSpeech.QUEUE_FLUSH, null, null)
}

    private fun createVoices(): Voice {
        for (voice in textToSpeech.voices) {
            if (voice.name == "en-gb-x-gbb-network") {
                return voice
            }
        }
        return textToSpeech.voice
    }
}