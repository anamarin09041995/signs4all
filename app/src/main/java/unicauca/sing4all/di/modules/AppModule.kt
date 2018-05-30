package unicauca.sing4all.di.modules

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app

    @Singleton
    @Provides
    fun provideText2Speech(context: Context): TextToSpeech{
        val txt2Speech = TextToSpeech(context) {}
        txt2Speech.language = Locale.getDefault()
        return txt2Speech
    }



}
