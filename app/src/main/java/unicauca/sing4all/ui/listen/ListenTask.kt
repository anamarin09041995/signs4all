package unicauca.sing4all.ui.listen

import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import unicauca.sing4all.di.ActivityScope
import unicauca.sing4all.quantifier.Hand
import unicauca.sing4all.util.applySchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScope
class ListenTask @Inject constructor(){

    private val words:List<Hand> = listOf(
            Hand(9519,65475,44703,38519,44197 ), //C
            Hand(14611, 69602, 51380, 47565, 48558), //o
            Hand(14851, 87039, 30920, 25256, 44780), //n
            Hand(16193,87972, 54899, 53682, 63834) //e
    )

    fun mockHand(): Observable<Hand> = words.toObservable()
            .concatMap{i-> Observable.just(i).delay(4, TimeUnit.SECONDS)}
            .applySchedulers()


}