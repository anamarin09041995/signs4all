package unicauca.sing4all.ui.listen

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import unicauca.sing4all.data.couch.CouchRx
import unicauca.sing4all.data.models.BluetoothSession
import unicauca.sing4all.data.models.Word
import unicauca.sing4all.data.net.NNApi
import unicauca.sing4all.data.net.ResponseNN
import unicauca.sing4all.data.preferences.Algorithm
import unicauca.sing4all.data.preferences.UserSession
import unicauca.sing4all.quantifier.BothQuantifier
import unicauca.sing4all.quantifier.Hand
import unicauca.sing4all.quantifier.StepQuantifier
import unicauca.sing4all.quantifier.VectorQuantifier
import unicauca.sing4all.util.applySchedulers
import unicauca.sing4all.util.likeEx
import javax.inject.Inject

class ListenViewModel @Inject constructor(private val db: CouchRx,
                                          private  val client: NNApi,
                                          private val step: StepQuantifier,
                                          private val vector: VectorQuantifier,
                                          private val both: BothQuantifier,
                                          private val session: UserSession,
                                          private val bluetoothSession: BluetoothSession) : ViewModel() {

    private val addressBt = bluetoothSession.address
    fun getBtSession():String = addressBt

    private val nameBt = bluetoothSession.name
    fun  getBtName():String = nameBt

    var word: String = ""

    private fun queryWords(): Single<List<Word>> {
        val query = if (word == "") "" else "$word%"
        return db.listByExp("text" likeEx query, Word::class)
    }

    fun queryWords(w:String): Single<List<Word>> {
        val query = if (w== "") "" else "$w%"
        return db.listByExp("text" likeEx query, Word::class)
    }

    fun NeuralNNResult(menique:Float,medio:Float,indice:Float,pulgar:Float):Single<ResponseNN> =
        client.getResponseNN(menique,medio,indice,pulgar).map {
            it
        }.applySchedulers()




    fun calculateWord(hand: Hand): Single<Pair<String, List<Word>>> = when (session.algorithm) {
        Algorithm.VECTORIAL -> vector.calculateChar(hand)
        Algorithm.STAGES -> step.calculateChar(hand)
        Algorithm.BOTH -> both.calculateChar(hand)
        else -> step.calculateChar(hand)
    }
            .flatMap {
                word += if (it.isNotEmpty()) it[0] else ""
                queryWords()
            }
            .map { word to it  }
            .applySchedulers()

    fun calculateLetter(hand:Hand):Single<List<String>> = when (session.algorithm) {
        Algorithm.VECTORIAL -> vector.calculateChar(hand)
        Algorithm.STAGES -> step.calculateChar(hand)
        Algorithm.BOTH -> both.calculateChar(hand)
        else -> step.calculateChar(hand)
    }.applySchedulers()

    fun clearWord() {
        word = ""
    }
}




