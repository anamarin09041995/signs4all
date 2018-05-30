package unicauca.sing4all.di.components

import dagger.Module
import dagger.android.ContributesAndroidInjector
import unicauca.sing4all.di.ActivityScope
import unicauca.sing4all.ui.listen.ListenActivity
import unicauca.sing4all.ui.main.MainActivity

@Module
abstract class ActivityComponents{

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindListenActivity(): ListenActivity


}