package com.example.book_v2.utilities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

object TextToSpeechSetUp {

    fun newInstanceTTS(context: Context): TextToSpeech {
        var textToSpeech: TextToSpeech? = null
        textToSpeech = TextToSpeech(
            context
        ) {
            if (it == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("ar_EG"))
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                    Log.e("error", "onViewCreated: arabic missing data ")
                   // installVoiceData(context)
                } else {
                    Log.e("error", "onViewCreated: something happened ")
                }
            } else {
                Log.e("error", "onViewCreated:not initialized ")
            }
        }

        val attrs = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
            .setLegacyStreamType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
        textToSpeech.setAudioAttributes(attrs)
        textToSpeech.setSpeechRate(.8f)
        textToSpeech.setPitch(1.5f)

        return textToSpeech
    }

    private fun installVoiceData(context: Context) {
        val intent = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.google.android.tts"/*replace with the package name of the target TTS engine*/)
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Log.e("TAG", "Failed to install TTS data, no Activity found for $intent)")
        }
    }
}
