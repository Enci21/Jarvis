package com.example.jarvis.brain

import android.app.SearchManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Canvas
import android.graphics.Picture
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jarvis.*
import com.example.jarvis.listen.ListenService
import com.example.jarvis.talk.TalkService
import kotlinx.android.synthetic.main.activity_brain.*
import java.io.IOException

class BrainActivity : AppCompatActivity() {

    private lateinit var viewModel: BrainViewModel
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var adapter: BluetoothAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brain)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            PackageManager.PERMISSION_GRANTED
        )
        viewModel = ViewModelProvider(this).get(BrainViewModel::class.java)
        viewModel.userVoiceInput.observe(this, Observer<String> { answer(it) })

        viewModel.listenBinder.observe(this, Observer<ListenService.ListenBinder> { t ->
            t.getService()
        })

        viewModel.talkBinder.observe(this, Observer<TalkService.TalkBinder> { t ->
            t.getService()
        })
        textToSpeech = TextToSpeech(
            this,
            TextToSpeech.OnInitListener { textToSpeech.voice = createVoices(textToSpeech) },
            "com.google.android.tts"
        )
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        adapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun listen(view: View) {
        viewModel.startListen()
    }

    private fun answer(userInput: String) {
        var input = userInput

        when (userInput) {
            SHOW_DAD -> imageView.setImageResource(R.drawable.tony)
            LOVE_YOU -> imageView.visibility = View.INVISIBLE
            BLUETOOTH_TURN_ON -> input = turnBluetooth(true)
            BLUETOOTH_TURN_OFF -> input = turnBluetooth(false)
        }
        speak(viewModel.talkService.findAnswer(input))

        when {
            userInput.contains(SEARCH_GOOGLE) -> {
                goToChrome(userInput.split(SEARCH_GOOGLE, 0))
                speak(HERE_IS_WHAT_I_FOUND)
            }
            userInput.contains(SEARCH_YT) -> {
                goToYoutube(userInput.split(SEARCH_YT, 0))
                speak(HERE_IS_WHAT_I_FOUND)
            }
            userInput.contains(CALL) -> {
                callSomeone(userInput.split(CALL, 0)[1])
                speak(IM_CALLING + userInput.split(CALL, 0)[1])
            }
        }
    }

    private fun callSomeone(name: String) {
        val searchName = name.split(" ")[1]
        var number = ""
        val cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val contactName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (contactName == searchName) {
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        val pCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.Contacts.DISPLAY_NAME + " = ?",
                            arrayOf(searchName),
                            null
                        )
                        if (pCursor != null && pCursor.count > 0) {
                            while (pCursor.moveToNext()) {
                                number =
                                    pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)) //throws exception
                            }
                        }
                        pCursor?.close()
                    }
                }
            }
        }
        cursor?.close()
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
    }


    private fun goToChrome(search: List<String>) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra(SearchManager.QUERY, search[1])
        val activities: List<ResolveInfo> = packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        val isIntentSafe: Boolean = activities.isNotEmpty()
        if (isIntentSafe) startActivity(intent)

    }

    private fun goToYoutube(search: List<String>) {

        val intent = Intent(Intent.ACTION_SEARCH)
        intent.setPackage("com.google.android.youtube")
        intent.putExtra(SearchManager.QUERY, search[1])
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val activities: List<ResolveInfo> = packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        val isIntentSafe: Boolean = activities.isNotEmpty()
        if (isIntentSafe) startActivity(intent)
    }

    private fun draw(picture: Picture) {
        val canvas = Canvas()
        canvas.drawPicture(picture)
    }

    private fun turnBluetooth(boolean: Boolean): String {
        return when (boolean) {
            true -> {
                if (!adapter.isEnabled) {
                    adapter.enable()
                    BLUETOOTH_TURN_ON
                } else BLUETOOTH_ALREADY_ON
            }
            false -> {
                if (adapter.isEnabled) {
                    adapter.disable()
                    BLUETOOTH_TURN_OFF
                } else BLUETOOTH_ALREADY_OFF
            }
        }
    }

    private fun connectToDeviceWithBluetooth(deviceName: String) {
        val pairedDevices: Set<BluetoothDevice>? = adapter.bondedDevices
        val name = deviceName.replace(" ", "")
        pairedDevices?.forEach {
            if (it.name.equals(name, ignoreCase = true)) {
                ConnectThread(device = it).run()
            } else Log.d("TAG", "nem jó")
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

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        private val socket: BluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(UUID)

        override fun run() {
            adapter.cancelDiscovery()

            socket.use { socket ->
                socket.connect()
            }
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: IOException) {
                Log.e("TAG", "Could not close the connect socket", e)
            }
        }
    }

}