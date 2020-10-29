package com.example.jarvis.brain

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jarvis.listen.ListenService
import com.example.jarvis.talk.TalkService

class BrainViewModel : ViewModel() {

    val listenService = ListenService()
    val listenBinder: MutableLiveData<ListenService.ListenBinder> = MutableLiveData()
    val listenServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            listenBinder.postValue(null)
            listenService.subject.onComplete()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val iBinder = service as ListenService.ListenBinder
            listenBinder.postValue(iBinder)
        }
    }

    val talkService = TalkService()
    val talkBinder: MutableLiveData<TalkService.TalkBinder> = MutableLiveData()
    val talkServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            talkBinder.postValue(null)
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val iBinder = service as TalkService.TalkBinder
            talkBinder.postValue(iBinder)
        }
    }

    var userVoiceInput = MutableLiveData<String>()
    private var connectionBoolean = true

    fun startListen() {
        listenService.startSpeech()
        if (connectionBoolean) {
            listenService.subject.subscribe { value ->
                userVoiceInput.value = value
            }
            connectionBoolean = false
        }
    }


}