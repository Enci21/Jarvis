package com.example.jarvis

import java.util.*

const val PERMISSIONS_REQUEST_ALL_PERMISSIONS = 1
val UUID: UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

//user inputs
const val HI = "hi"
const val HELLO = "hello"
const val INTRODUCE_YOURSELF = "Jarvis please introduce yourself"
const val LOVE_YOU = "I love you"
const val SAY_HELLO = "Jarvis please say hello"
const val SHOW_DAD = "show your dad"
const val BLUETOOTH_TURN_ON = "turn on the Bluetooth"
const val BLUETOOTH_TURN_OFF = "turn off the Bluetooth"
val SEARCH_YT = Regex(pattern = """\b(\w*search on YouTube\w*)\b""")
val SEARCH_GOOGLE = Regex(pattern = """\b(\w*search on Google\w*)\b""")
val CALL = Regex(pattern = """\b(\w*call\w*)\b""")
val WRITE_A_MESSAGE = Regex(pattern = """\b(\w*write a message to\w*)\b""")


//answers
const val SORRY_I_CANT_UNDERSTAND = "Sorry, I can't understand"
const val LOVE_YOU_MORE = "i love you more"
const val OK = "ok"
const val IM_CALLING = "i'm calling"
const val TELL_MESSAGE = "what is the message?"
const val HERE_IS_WHAT_I_FOUND = "here is what i found"
const val BLUETOOTH_ON = "OK, the bluetooth is on"
const val BLUETOOTH_OFF = "OK, the bluetooth is off now"
const val BLUETOOTH_ALREADY_OFF = "The Bluetooth is already off"
const val BLUETOOTH_ALREADY_ON = "The Bluetooth is already on"


const val IM_JARVIS = "I'm Jarvis. " +
        "My goddess created me with very hard work based on Anthony Stark's invention." +
        " I am a lot." +
        " An Android application, your friend, everyone's favorite assistant."