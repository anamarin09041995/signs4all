package unicauca.sing4all.di.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import unicauca.sing4all.App
import unicauca.sing4all.di.modules.AppModule
import unicauca.sing4all.di.modules.CouchModule
import unicauca.sing4all.di.modules.SqlModule
import unicauca.sing4all.di.modules.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityComponents::class,
    AppModule::class,
    ViewModelModule::class,
    CouchModule::class,
    SqlModule::class
])
interface AppComponent {

    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }


}