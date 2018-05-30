package unicauca.sing4all.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import unicauca.sing4all.di.ViewModelKey
import unicauca.sing4all.ui.listen.ListenViewModel
import unicauca.sing4all.ui.main.MainViewModel
import unicauca.sing4all.util.AppViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListenViewModel::class)
    abstract fun bindListenViewModel(listenViewModel: ListenViewModel): ViewModel


}