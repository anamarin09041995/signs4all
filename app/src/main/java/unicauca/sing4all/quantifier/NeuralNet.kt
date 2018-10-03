package unicauca.sing4all.quantifier

import io.reactivex.Single
import unicauca.sing4all.data.net.NNApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NeuralNet @Inject constructor(private val api:NNApi):Quantifier{

    override fun calculateChar(hand: Hand): Single<List<String>> = api.getResponseNN(hand.me.toFloat(),hand.co.toFloat(),hand.ind.toFloat(),hand.pu.toFloat())
                .map { listOf(it.result!!) }

}