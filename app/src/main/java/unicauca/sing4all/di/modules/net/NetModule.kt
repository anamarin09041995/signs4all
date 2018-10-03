package unicauca.sing4all.di.modules.net

import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import unicauca.sing4all.R
import unicauca.sing4all.data.net.NNApi
import javax.inject.Singleton

@Module
class NetModule{
    @Provides
    @Singleton
    fun provideRetrofit(context: Context): Retrofit = Retrofit.
            Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .baseUrl(context.getString(R.string.neural_url))
            .build()

    @Provides
    @Singleton
    fun provideMovieClient(retrofit: Retrofit): NNApi =
            retrofit.create(NNApi::class.java)


}