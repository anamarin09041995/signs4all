package unicauca.sing4all.ui.listen

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_listen.*
import unicauca.sing4all.R
import unicauca.sing4all.di.Injectable
import unicauca.sing4all.ui.adapter.WordListenAdapter
import unicauca.sing4all.util.LifeDisposable
import unicauca.sing4all.util.buildViewModel
import javax.inject.Inject

class ListenActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val viewModel: ListenViewModel by lazy { buildViewModel<ListenViewModel>(factory)}

    @Inject
    lateinit var adapter: WordListenAdapter

    @Inject
    lateinit var textToSpeech: TextToSpeech

    val dis: LifeDisposable = LifeDisposable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listen)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()

        dis add adapter.onClick
                .subscribe {
                    textToSpeech.speak(it.text, TextToSpeech.QUEUE_FLUSH, null, null)
                }

        /*dis add sign.textChanges()
                .flatMapSingle {
                    viewModel.queryWords(it.toString())}
                .subscribe {
                    adapter.data = it
                }*/

        dis add btnStop.clicks()
                .subscribe {

                }

    }
}
