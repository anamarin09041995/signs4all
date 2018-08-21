package unicauca.sing4all.quantifier

import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BothQuantifier @Inject constructor(private val step:StepQuantifier,
                                         private val vector:VectorQuantifier):Quantifier{


    override fun calculateChar(hand: Hand): Single<List<String>> = step.calculateChar(hand)
                .flatMap { if(it.size > 1) vector.calculateChar(hand) else Single.just(it) }



}