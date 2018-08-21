package unicauca.sing4all.ui.setup

import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setup.*
import org.jetbrains.anko.startActivity
import unicauca.sing4all.R
import unicauca.sing4all.databinding.ActivitySetupBinding
import unicauca.sing4all.ui.test.TestActivity
import android.widget.RadioGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.checked
import unicauca.sing4all.data.preferences.Algorithm
import unicauca.sing4all.data.preferences.UserSession
import unicauca.sing4all.di.Injectable
import unicauca.sing4all.util.LifeDisposable
import javax.inject.Inject


class SetupActivity : AppCompatActivity(), Injectable {

    lateinit var binding: ActivitySetupBinding
    @Inject
    lateinit var session: UserSession
    val dis: LifeDisposable = LifeDisposable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup)

        binding.click = this
    }

    fun btnClick() {
        startActivity<TestActivity>()
    }

    override fun onResume() {
        super.onResume()

        when(session.algorithm){
            Algorithm.STAGES -> radioButton3
            Algorithm.VECTORIAL -> radioButton2
            else -> radioButton
        }.isChecked = true


        dis add radioButton.clicks()
                .subscribe { session.algorithm = Algorithm.NEURONAL }
        dis add radioButton2.clicks()
                .subscribe { session.algorithm = Algorithm.VECTORIAL }
        dis add radioButton3.clicks()
                .subscribe { session.algorithm = Algorithm.STAGES }
        dis add radioButton4.clicks()
                .subscribe { session.algorithm = Algorithm.BOTH }


    }
}
