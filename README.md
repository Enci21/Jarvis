<h1>J.A.R.V.I.S.</h1> (Just A Rather Very Intelligent System) was Tony Stark's invention.I like the idea so I tried to create something similar. He is an assistant like Siri or Bixby. 
We can tell him commands and he does his best to meet the needs of the user. 

I hope one day he comes to life like Vision. :) 
<br>
<br>

<h2>Functionalities</h2>

<h3>Call</h3>
If you tell him “Call X” Jarvis finds the contact named X among the contacts and calls it.

<h3>Introduce himself</h3>
With the “Jarvis please introduce yourself” command he tells the text of the introduction.

<h3>Bluetooth</h3>
When you need the Bluetooth just tell him the “Turn on the Bluetooth” command. Similary you can turn it off.

<h3>Google search</h3>
He can search on Google with the “Search on Google *something*” command.

<h3>YouTube search</h3>
Do you need some music? Tell him the title of the song or the name of the artist and he finds it on YouTube immediately. 

<h3>Kinda error message</h3>
If, for some reason, Jarvis can’t recognize what you told him, he’ll give you feedback about it, so it never happens that you’re waiting for him and nothing happens.
<h3>Funny functions</h3>
Jarvis loves you and if you tell him your feelings ("I love you") you will not be disappointed.  Or if you miss Tony Stark’s face just tell him "Show your dad" command and he will do it with pleasure.
 
<br>
<br>

<h2>Technologies</h2>
The whole app was written in <strong>Kotlin</strong>. I chose the <strong>MVVM</strong> architecture pattern.
The dependency injection was made with <strong>Koin</strong>. 
<br>
<strong>LiveData</strong> is a very important component in the project, I used <strong>Observables</strong> and <strong>Observers</strong> to the communication part.
Furthermore <strong>SpeechRecognizer</strong> - this class provides access to the speech recognition service -  
for the speech perception and the <strong>TextToSpeech</strong> engine for speaking.
