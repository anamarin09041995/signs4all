package unicauca.sing4all.ui.listen

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import unicauca.sing4all.data.couch.CouchRx
import unicauca.sing4all.data.models.Word
import unicauca.sing4all.util.applySchedulers
import unicauca.sing4all.util.likeEx
import javax.inject.Inject

class ListenViewModel @Inject constructor(private val db: CouchRx) : ViewModel() {

    fun queryWords(word: String): Single<List<Word>> {
        val query = if (word == "") "" else "$word%"
        return db.listByExp("text" likeEx query, Word::class)
                .applySchedulers()
    }


}