package com.example.jarvis.talk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jarvis.R
import kotlinx.android.synthetic.main.activity_talk.*

class TalkActivity : AppCompatActivity() {

    private lateinit var viewModel: TalkServiceViewModel
    private lateinit var service: TalkService
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk)
        viewModel = ViewModelProvider(this).get(TalkServiceViewModel::class.java)
        viewModel.binder.observe(this, Observer<TalkService.TalkBinder> { t ->
            service = t.getService()
        })

        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { textToSpeech.voice = createVoices(textToSpeech) }, "com.google.android.tts")

        val input = intent.getStringExtra("RESULT")

        talkButton.setOnClickListener {
            speak(service.findAnswer(input))
        }
    }

    private fun speak(talk: String) {
        textToSpeech.speak(talk, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun createVoices(textToSpeech: TextToSpeech): Voice {
        for (voice in textToSpeech.voices) {
            if (voice.name == "en-gb-x-gbb-network") {
                return voice
            }
        }
        return textToSpeech.voice
    }

    override fun onResume() {
        super.onResume()
        startService()
    }

    /*override fun onStop() {
        super.onStop()
        unbindService(viewModel.serviceConnection)
    }*/

    private fun startService() {
        val intent = Intent(this, TalkService::class.java)
        startService(intent)
        bindService()
    }

    private fun bindService() {
        val intent = Intent(this, TalkService::class.java)
        bindService(intent, viewModel.serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(viewModel.serviceConnection)
    }

}