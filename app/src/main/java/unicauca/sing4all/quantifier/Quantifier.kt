package unicauca.sing4all.quantifier

import io.reactivex.Single

interface Quantifier{

    fun calculateChar(hand:Hand):Single<List<String>>

}