package unicauca.sing4all.quantifier

import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import unicauca.sing4all.data.sql.dao.CharConstraintDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StepQuantifier @Inject constructor(private val dao: CharConstraintDao):Quantifier {

    private val rangeM: List<Pair<Int, Int>> = listOf(
            8065 to 11590,
            11591 to 11864,
            11865 to 17537,
            17538 to 22774,
            22775 to 23408
    )

    private val rangeA: List<Pair<Int, Int>> = listOf(
            22974 to 28889,
            28890 to 32104,
            32105 to 42520,
            42521 to 45558,
            45559 to 57032,
            57033 to 69602,
            69603 to 88922,
            88923 to 92969,
            92970 to 98395
    )

    private val rangeC: List<Pair<Int, Int>> = listOf(
            18147 to 37390,
            37391 to 57032,
            57033 to 68896

    )

    private val rangeI: List<Pair<Int, Int>> = listOf(
            15601 to 21642,
            21643 to 27878,
            27879 to 36313,
            36314 to 48223,
            48224 to 51011,
            51012 to 63303,
            63304 to 68379
    )

    private val rangeP: List<Pair<Int, Int>> = listOf(
            19195 to 32257,
            32258 to 42251,
            42252 to 43345,
            43346 to 54888,
            54889 to 67780
    )

    override fun calculateChar(hand: Hand): Single<List<String>> = Single.create<String> { emitter ->
        val m = rangeM.indexOfFirst { hand.me in it.first .. it.second } + 1
        val a = rangeA.indexOfFirst { hand.an in it.first .. it.second } + 1
        val c = rangeC.indexOfFirst { hand.co in it.first .. it.second } + 1
        val i = rangeI.indexOfFirst { hand.ind in it.first .. it.second } + 1
        val p = rangeP.indexOfFirst { hand.pu in it.first .. it.second } + 1
        emitter.onSuccess("$m$a$c$i$p")
    }
            .flatMap {dao.getChars(it) }
            .flatMapObservable { it.toObservable() }
            .map { it.letter }
            .toList()


}
