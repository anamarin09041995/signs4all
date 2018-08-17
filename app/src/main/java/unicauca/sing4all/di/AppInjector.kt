package unicauca.sing4all.di


import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import unicauca.sing4all.App
import unicauca.sing4all.di.components.DaggerAppComponent

class AppInjector {

    companion object {
        fun init(app: App){
            DaggerAppComponent.builder()
                    .application(app)
                    .build()
                    .inject(app)
            app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
                override fun onActivityPaused(activity: Activity?) {   }

                override fun onActivityResumed(activity: Activity?) {}

                override fun onActivityStarted(activity: Activity?) {}

                override fun onActivityDestroyed(activity: Activity?) {}

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

                override fun onActivityStopped(activity: Activity?) {}

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    handlerActivity(activity)
                }

            })
        }
        private fun handlerActivity(activity: Activity){
            if (activity is Injectable || activity is HasSupportFragmentInjector){
                AndroidInjection.inject(activity)
            }
            (activity as AppCompatActivity).supportFragmentManager
                    .registerFragmentLifecycleCallbacks(object: FragmentManager.FragmentLifecycleCallbacks(){

                        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                            if(f is Injectable){
                                AndroidSupportInjection.inject(f)
                            }
                        }

                    }, true)
        }
    }

}