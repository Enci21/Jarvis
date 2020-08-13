package com.example.jarvis.brain

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jarvis.R
import com.example.jarvis.listen.ListenService
import com.example.jarvis.talk.TalkService

class BrainActivity : AppCompatActivity() {

    private lateinit var viewModel: BrainViewModel
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brain)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), PackageManager.PERMISSION_GRANTED)
        viewModel = ViewModelProvider(this).get(BrainViewModel::class.java)
        viewModel.userVoiceInput.observe(this, Observer<String> { answer(it) })

        viewModel.listenBinder.observe(this, Observer<ListenService.ListenBinder> { t ->
            t.getService()
        })

        viewModel.talkBinder.observe(this, Observer<TalkService.TalkBinder> { t ->
            t.getService()
        })
        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { textToSpeech.voice = createVoices(textToSpeech) }, "com.google.android.tts")
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    }

    fun listen(view: View) {
        viewModel.startListen()
    }

    private fun answer(userInput: String) {
        speak(viewModel.talkService.findAnswer(userInput))
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
        startListenService()
        startTalkService()
    }

    private fun startTalkService() {
        val intent = Intent(this, TalkService::class.java)
        startService(intent)
        bindTalkService()
    }

    private fun bindTalkService() {
        val intent = Intent(this, TalkService::class.java)
        bindService(intent, viewModel.talkServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(viewModel.talkServiceConnection)
        unbindService(viewModel.listenServiceConnection)
    }

    /*override fun onStop() {
        super.onStop()
        unbindService(viewModel.listenServiceConnection)
        unbindService(viewModel.talkServiceConnection)
    }*/

    private fun startListenService() {
        val intent = Intent(this, ListenService::class.java)
        startService(intent)
        bindListenService()
    }

    private fun bindListenService() {
        val intent = Intent(this, ListenService::class.java)
        bindService(intent, viewModel.listenServiceConnection, Context.BIND_AUTO_CREATE)
    }

}