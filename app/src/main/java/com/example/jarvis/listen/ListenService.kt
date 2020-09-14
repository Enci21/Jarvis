package com.example.jarvis.listen

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import io.reactivex.subjects.BehaviorSubject
import org.koin.android.ext.android.get
import java.util.*

class ListenService : Service() {

    private val TAG = "---------LOG-----------"
    private val intentRecognizer: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    private val listenBinder = ListenBinder()
    private val speechRecognizer = get<SpeechRecognizer>()
    var subject: BehaviorSubject<String> = BehaviorSubject.create()

    class ListenBinder : Binder() {

        fun getService(): ListenService {
            return ListenService()
        }
    }

    override fun onCreate() {
        super.onCreate()
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return listenBinder
    }

    fun startSpeech() {
        speechRecognizer.startListening(intentRecognizer)
        createRecognizerListener()
    }

    private fun createRecognizerListener() {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
            }

            override fun onRmsChanged(rmsdB: Float) {
                val quietMax = 25f
                val mediumMax = 65f

                if (rmsdB < quietMax) {
                    Log.e(
                            TAG,
                            "I'm quiet"
                    )
                } else if (rmsdB >= quietMax && rmsdB < mediumMax) {
                    Log.e(
                            TAG,
                            "I'm medium quiet"
                    )
                } else {
                    Log.e(
                            TAG,
                            "I'm loud"
                    )
                }
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                TODO("Not yet implemented")
            }

            override fun onPartialResults(partialResults: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onBeginningOfSpeech() {
                Log.e(
                        TAG,
                        "Beginning of speech "
                )
            }

            override fun onEndOfSpeech() {
                Log.e(
                        TAG,
                        "End of speech "
                )
            }

            override fun onError(error: Int) {
                Log.e(
                        TAG,
                        error.toString()
                )
            }

            override fun onResults(results: Bundle?) {
                val matches: ArrayList<String>? = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                var current = ""
                if (matches != null) {
                    current = matches.get(0)
                    Log.e(TAG, current)
                    subject.onNext(current)
                }
            }
        })
    }

}

