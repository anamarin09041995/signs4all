package unicauca.sing4all.di.components

import dagger.Module
import dagger.android.ContributesAndroidInjector
import unicauca.sing4all.di.ActivityScope
import unicauca.sing4all.ui.listen.ListenActivity
import unicauca.sing4all.ui.main.MainActivity
import unicauca.sing4all.ui.setup.SetupActivity
import unicauca.sing4all.ui.test.TestActivity

@Module
abstract class ActivityComponents{

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindListenActivity(): ListenActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindTestActivity(): TestActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindSetUpActivity(): SetupActivity



}