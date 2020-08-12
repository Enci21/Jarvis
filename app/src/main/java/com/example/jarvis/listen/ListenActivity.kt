package com.example.jarvis.listen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.*

class ListenActivity : AppCompatActivity() {

    private val intentRecognizer: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    private val TAG = "---------LOG-----------"
    var result: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_listen)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), PackageManager.PERMISSION_GRANTED)

        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        createRecognizerListener()
    }

    fun startSpeech(v: View) {
        speechRecognizer.startListening(intentRecognizer)
    }

    /*fun sendResultInput() {
        val intent = Intent(this, TalkActivity::class.java)
        intent.putExtra("RESULT", result)
        startActivity(intent)
    }*/

    private fun createRecognizerListener() {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(this@ListenActivity, "Talk to me", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@ListenActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val matches: ArrayList<String>? = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                var current = ""
                if (matches != null) {
                    current = matches.get(0)
                    result = current
                    //sendResultInput()

                }
            }

        })
    }
}