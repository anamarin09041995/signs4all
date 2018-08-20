package unicauca.sing4all.ui.test

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test.*
import unicauca.sing4all.R
import unicauca.sing4all.databinding.ActivityTestBinding
import unicauca.sing4all.di.Injectable
import unicauca.sing4all.ui.adapter.ReportAdapter
import unicauca.sing4all.util.LifeDisposable
import unicauca.sing4all.util.buildViewModel
import javax.inject.Inject

class TestActivity : AppCompatActivity(), Injectable {

    val dis: LifeDisposable = LifeDisposable(this)
    val adapter: ReportAdapter = ReportAdapter()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel: TestViewModel by lazy { buildViewModel<TestViewModel>(factory) }

    lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test)
    }

    override fun onResume() {
        super.onResume()

        list.adapter = adapter

        dis add viewModel.test(assets.openFd("datasets/test.csv").fileDescriptor)
                .subscribe { res ->
                    binding.global = res.second
                    adapter.data = res.first
                }

    }


}
