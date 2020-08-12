package com.example.jarvis.brain

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jarvis.talk.TalkService

class BrainViewModel : ViewModel() {

    val talkService = TalkService()

    //private val listenService = ListenService()
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

    /*val listenServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            binder.postValue(null)
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val iBinder = service as ListenService.ListenBinder
            binder.postValue(iBinder)
        }
    }*/

    var userVoiceInput = MutableLiveData<String>("")
    var answer = MutableLiveData<String>("")


    fun startListen(): String {
        return ""//listenService.startSpeech
    }
}