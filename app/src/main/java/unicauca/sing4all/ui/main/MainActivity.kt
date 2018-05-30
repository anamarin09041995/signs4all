package unicauca.sing4all.ui.main

import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import unicauca.sing4all.R
import unicauca.sing4all.di.Injectable
import unicauca.sing4all.ui.adapter.WordAdapter
import unicauca.sing4all.ui.listen.ListenActivity
import unicauca.sing4all.util.LifeDisposable
import unicauca.sing4all.util.buildViewModel
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var factory:ViewModelProvider.Factory
    val viewModel:MainViewModel by lazy { buildViewModel<MainViewModel>(factory)}

    @Inject
    lateinit var adapter:WordAdapter

    @Inject
    lateinit var textToSpeech:TextToSpeech

    val dis:LifeDisposable = LifeDisposable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()

        dis add viewModel.listWords()
                .subscribe {
                    adapter.data = it
                }

        dis add adapter.onClick
                .subscribe {
                    textToSpeech.speak(it.text, TextToSpeech.QUEUE_FLUSH, null, null)
                }

        dis add btnPlay.clicks()
                .subscribe { startActivity<ListenActivity>() }
    }
}
