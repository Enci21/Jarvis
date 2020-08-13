package com.example.jarvis

import android.speech.SpeechRecognizer
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {

    single { SpeechRecognizer.createSpeechRecognizer(get()) }
}