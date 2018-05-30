package unicauca.sing4all.ui.main

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import unicauca.sing4all.data.couch.CouchRx
import unicauca.sing4all.data.models.Word
import unicauca.sing4all.util.applySchedulers
import unicauca.sing4all.util.likeEx
import javax.inject.Inject

class MainViewModel @Inject constructor(private val db: CouchRx):ViewModel(){

    fun listWords(): Observable<List<Word>> =
            db.listObsByExp(null, Word::class)
                    .applySchedulers()


}