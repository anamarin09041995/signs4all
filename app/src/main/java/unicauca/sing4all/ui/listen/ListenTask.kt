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
            Hand(13157,58845,49239,40441,45076),//c
            Hand(14611, 69602, 51380, 47565, 48558), //o
            Hand(14851, 87039, 30920, 25256, 44780), //n
            Hand(16193,87972, 54899, 53682, 63834)//e

            //e

    )



    /*
    *     private val words:List<Hand> = listOf(
            Hand(108477,99560,60269,39461,48557 ), //a
            Hand(8938,61252,1321,17991,61252),//b
            Hand(13157,58845,49239,40441,45076),//c
            Hand(11678,49935,37836,21159,49935),//d
            Hand(16193,87972, 54899, 53682, 63834),//e
            Hand(18303,25134,1135,20234,25134),//f
            Hand(96321,66037,3113,45375,66037),//i
            Hand(12442,37612,24894,20056,37612),//k
            Hand(16059,38751,44197,21255,31085),//l
            Hand(11451,79377,31251,33358,68378),//m
            Hand(14611, 69602, 51380, 47565, 48558), //o
            Hand(14851, 87039, 30920, 25256, 44780), //n
            Hand(19792,60269,51012,21064,60269),//p
            Hand(11541,43626,34494,29646,42521),//q
            Hand(16813,64372,37613,24775,66610),//r
            Hand(9861,35284,39946,44780,31251),//t

    )
*/


    fun mockHand(): Observable<Hand> = words.toObservable()
            .concatMap{i-> Observable.just(i).delay(4, TimeUnit.SECONDS)}
            .applySchedulers()


}