package unicauca.sing4all.ui.listen

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_listen.*
import org.jetbrains.anko.toast
import unicauca.sing4all.R
import unicauca.sing4all.databinding.ActivityListenBinding
import unicauca.sing4all.di.Injectable
import unicauca.sing4all.ui.adapter.WordListenAdapter
import unicauca.sing4all.util.LifeDisposable
import unicauca.sing4all.util.buildViewModel
import javax.inject.Inject

class ListenActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val viewModel: ListenViewModel by lazy { buildViewModel<ListenViewModel>(factory) }

    @Inject
    lateinit var adapter: WordListenAdapter

    @Inject
    lateinit var textToSpeech: TextToSpeech

    @Inject
    lateinit var uxTask: ListenTask

    val dis: LifeDisposable = LifeDisposable(this)
    var uxDis: Disposable? = null

    var testing = false

    lateinit var binding: ActivityListenBinding

    var word = ""
        set(value) {
            field = value
            sign?.setText(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_listen)

        title = "Traductor"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.letters = emptyList()
        binding.listen = false

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.listen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_task -> {
                testing = true
                toast("Test de Usuario Activado")
            }
            R.id.action_no_task -> {
                stopUxTask()
                testing = false
                toast("Test de Usuario Desactivado")
            }
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        dis add adapter.onClick
                .subscribe {
                    textToSpeech.speak(it.text, TextToSpeech.QUEUE_FLUSH, null, null)
                }

        dis add btnPlay.clicks()
                .subscribe {
                    binding.listen = true
                    if (testing) runUxTask()
                }

        dis add btnStop.clicks()
                .subscribe {
                    stopListen()
                    if (testing) stopUxTask()
                }

        dis add sign.textChanges()
                .filter { !binding.listen }
                .doOnNext { if (it == "") adapter.data = emptyList() }
                .filter { it != "" }
                .flatMapSingle {
                    viewModel.queryWords(it.toString())
                }
                .subscribe {
                    adapter.data = it
                }

        dis add Observable.merge(
                btnChar1.clicks().map { 0 },
                btnChar2.clicks().map { 1 },
                btnChar3.clicks().map { 2 }
        )
                .map {
                    word = word.replaceRange(word.lastIndex, word.length, binding.letters!![it])
                    sign.setText(word)
                    binding.letters = emptyList()
                    word
                }
                .flatMapSingle(viewModel::queryWords)
                .subscribe { adapter.data = it }

        dis add btnClear.clicks()
                .subscribe {
                    adapter.data = emptyList()
                    word = ""
                }

    }

    fun runUxTask() {
        uxDis = uxTask.mockHand()
                .flatMapSingle(viewModel::calculateLetter)
                .filter { it.isNotEmpty() }
                .doOnNext {
                    word += it[0]
                    binding.letters = it
                }
                .map { word }
                .flatMapSingle(viewModel::queryWords)
                .subscribe {
                    adapter.data = it
                }
    }

    fun stopUxTask() {
        uxDis?.dispose()
        uxDis = null
        stopListen()
    }

    fun stopListen() {
        binding.listen = false
        binding.letters = emptyList()

    }

}
