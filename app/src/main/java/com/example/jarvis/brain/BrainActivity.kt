package com.example.jarvis.brain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jarvis.R
import com.example.jarvis.talk.TalkService

class BrainActivity : AppCompatActivity() {

    private val viewModel: BrainViewModel = ViewModelProvider(this).get(BrainViewModel::class.java)
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brain)
        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { textToSpeech.voice = createVoices(textToSpeech) }, "com.google.android.tts")
        viewModel.talkBinder.observe(this, Observer<TalkService.TalkBinder> { t ->
            t.getService()
        })
        val answerObserver = Observer<String> { a ->
            speak(a)
        }
        val userVoiceInputObserver = Observer<String> { userVoiceInput ->
            viewModel.talkService.findAnswer(userVoiceInput)
        }
        viewModel.userVoiceInput.observe(this, userVoiceInputObserver)
        viewModel.answer.observe(this, answerObserver)
    }

    fun listen(view: View) {
        viewModel.userVoiceInput.value = viewModel.startListen()
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
        startTalkService()
    }

    /*override fun onStop() {
        super.onStop()
        unbindService(viewModel.serviceConnection)
    }*/

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
    }

    /*override fun onResume() {
        super.onResume()
        startListenService()
    }

    /*override fun onStop() {
        super.onStop()
        unbindService(viewModel.listenServiceConnection)
    }*/

    private fun startListenService() {
        val intent = Intent(this, ListenService::class.java)
        startService(intent)
        bindListenService()
    }

    private fun bindListenService() {
        val intent = Intent(this, Listen::class.java)
        bindService(intent, viewModel.listenServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(viewModel.listenServiceConnection)
    }*/


}