package unicauca.sing4all.ui.test

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_test.*
import unicauca.sing4all.R
import unicauca.sing4all.data.preferences.Algorithm
import unicauca.sing4all.data.preferences.UserSession
import unicauca.sing4all.databinding.ActivityTestBinding
import unicauca.sing4all.di.Injectable
import unicauca.sing4all.ui.adapter.ReportAdapter
import unicauca.sing4all.util.LifeDisposable
import unicauca.sing4all.util.buildViewModel
import java.io.File
import javax.inject.Inject

class TestActivity : AppCompatActivity(), Injectable {

    val dis: LifeDisposable = LifeDisposable(this)
    val adapter: ReportAdapter = ReportAdapter()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var session:UserSession

    val viewModel: TestViewModel by lazy { buildViewModel<TestViewModel>(factory) }

    lateinit var binding: ActivityTestBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = when(session.algorithm){
            Algorithm.STAGES -> "Escalar"
            Algorithm.NEURONAL -> "Neuronal"
            Algorithm.VECTORIAL -> "Vectorial"
            else -> "Combinado"
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_test)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        list.adapter = adapter

        val csv = File(ContextCompat.getDataDir(this), "testb.csv")

        dis add viewModel.prepareCsv(csv,assets.open("datasets/testPrePro.csv") )
                .flatMap(viewModel::test)
                .subscribe { res ->
                    binding.global = res.second
                    adapter.data = res.first
                }

    }


}
