package unicauca.sing4all

import android.app.Activity
import android.app.Application
import com.couchbase.lite.Replicator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import unicauca.sing4all.di.AppInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector{

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var replicator:Replicator

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        replicator.start()
    }

    override fun activityInjector(): AndroidInjector<Activity> = injector

}