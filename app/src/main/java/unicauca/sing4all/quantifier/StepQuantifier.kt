package unicauca.sing4all.quantifier

import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import unicauca.sing4all.data.sql.dao.CharConstraintDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StepQuantifier @Inject constructor(private val dao: CharConstraintDao) {

    private val rangeM: List<Pair<Int, Int>> = listOf(
            8065 to 11591,
            11591 to 11895,
            11865 to 17538,
            17538 to 22794,
            22794 to 23407
    )

    private val rangeA: List<Pair<Int, Int>> = listOf(
            22974 to 28890,
            28890 to 32104,
            32104 to 42521,
            42521 to 45558,
            45558 to 57033,
            57033 to 69602,
            69602 to 88923,
            88923 to 92970,
            92970 to 98394
    )

    private val rangeC: List<Pair<Int, Int>> = listOf(
            18147 to 37391,
            37391 to 57033,
            57033 to 68895

    )

    private val rangeI: List<Pair<Int, Int>> = listOf(
            15601 to 21643,
            21643 to 27878,
            27878 to 36314,
            36314 to 48223,
            48223 to 51012,
            51012 to 63303,
            63303 to 68378
    )

    private val rangeP: List<Pair<Int, Int>> = listOf(
            19195 to 32258,
            32258 to 42252,
            42252 to 43346,
            43346 to 54889,
            54889 to 67779
    )

    fun calculateChar(hand: Hand): Single<List<String>> = Single.create<String> { emitter ->
        val m = rangeM.indexOfFirst { hand.me in it.first until it.second } + 1
        val a = rangeA.indexOfFirst { hand.an in it.first until it.second } + 1
        val c = rangeC.indexOfFirst { hand.co in it.first until it.second } + 1
        val i = rangeI.indexOfFirst { hand.ind in it.first until it.second } + 1
        val p = rangeP.indexOfFirst { hand.pu in it.first until it.second } + 1
        emitter.onSuccess("$m$a$c$i$p")
    }
            .flatMap { dao.getChars(it) }
            .flatMapObservable { it.toObservable() }
            .map { it.char }
            .toList()


}
