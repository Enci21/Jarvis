package com.example.jarvis

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
import com.example.jarvis.talk.TalkActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val intentRecognizer: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    private val TAG = "---------LOG-----------"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), PackageManager.PERMISSION_GRANTED)
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        createRecognizerListener()
    }

    fun stopSpeech(v: View) {
        speechRecognizer.stopListening()
    }

    fun startSpeech(v: View) {
        speechRecognizer.startListening(intentRecognizer)
    }

    private fun createRecognizerListener() {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(this@MainActivity, "Talk to me", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@MainActivity, "Speech stopped!", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: Int) {
                Log.e(
                        TAG,
                        error.toString()
                )
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val matches: ArrayList<String>? = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                var current = ""
                if (matches != null) {
                    current = matches.get(0)
                    textView.text = current
                }
            }

        })
    }

    fun goToTalk(view: View) {
        val intent = Intent(this, TalkActivity::class.java)
        startActivity(intent)
    }

}