package unicauca.sing4all.data.couch

import com.couchbase.lite.*
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import unicauca.sing4all.util.andEx
import unicauca.sing4all.util.equalEx
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class CouchRx @Inject constructor(private val db: Database,
                                        @Named("dbName") private val dbName: String,
                                        private val folder: File,
                                        private val mapper: ObjectMapper) {



    fun <T : Any> listByExp(expression: Expression, kClass: KClass<T>): Single<List<T>> = Single.create<ResultSet> {
        val query = QueryBuilder
                .select(SelectResult.all(), SelectResult.expression(Meta.id), SelectResult.expression(Meta.sequence))
                .from(DataSource.database(db))
                .where(expression andEx ("type" equalEx kClass.simpleName.toString()))

        it.onSuccess(query.execute())
    }
            .flatMapObservable { it.toObservable() }
            .map {
                val id = it.getString("id")
                val sequence = it.getLong("sequence")
                val content = it.getDictionary(dbName)
                Triple(id, sequence, content)
            }
            .map { dictionaryToObject(it.first, it.second, it.third, kClass) }
            .toList()

    fun ListObsByQuery(query: Query): Observable<ResultSet> = Observable.create { emitter ->
        query.addChangeListener {
            if (it.error == null) emitter.onNext(it.results)
            else emitter.onError(it.error)
        }
    }

    fun <T : Any> listObsByExp(expression: Expression?, kClass: KClass<T>): Observable<List<T>> = Observable.create<ResultSet> { emitter ->
        val exp = if(expression == null ) ("type" equalEx kClass.simpleName.toString()) else expression andEx ("type" equalEx kClass.simpleName.toString())
        val query = QueryBuilder
                .select(SelectResult.all(), SelectResult.expression(Meta.id), SelectResult.expression(Meta.sequence))
                .from(DataSource.database(db))
                .where(exp)
        query.addChangeListener {
            if (it.error == null) emitter.onNext(it.results)
            else emitter.onError(it.error)
        }
    }
            .flatMap { result ->
                result.toObservable()
                        .map {
                            val id = it.getString("id")
                            val sequence = it.getLong("sequence")
                            val content = it.getDictionary(dbName)
                            Triple(id, sequence, content)
                        }
                        .map { dictionaryToObject(it.first, it.second, it.third, kClass) }
                        .toList().toObservable()

            }

    fun getBlob(id:String, name:String):Pair<Dictionary?,ByteArray?>{
        val doc = db.getDocument(id)
        val files:Dictionary? = doc.getDictionary("files")
        val blob:Blob? = files?.getBlob(name)
        return  files to blob?.content
    }



    fun getBlogFromDictionary(dictionary: Dictionary?, name:String):ByteArray? = dictionary?.
            getBlob(name)?.content

    fun getFile(id: String, name: String): Maybe<File> {
        val file = File(folder, name)
        return if (file.exists()) {
            Maybe.just(file)
        } else {
            Maybe.create<InputStream> {
                try {
                    val doc = db.getDocument(id)
                    val files:Dictionary? = doc.getDictionary("files")
                    val blob:Blob? = files?.getBlob(name)
                    val input = blob?.contentStream
                    if (input != null && blob != null) it.onSuccess(input)
                    else it.onComplete()
                } catch (e: Exception) {
                    it.onError(e)
                }
            }
                    .flatMap {
                        val outStream = FileOutputStream(file)
                        val buffer = ByteArray(8 * 1024)
                        var bytesRead: Int = it.read(buffer)
                        while (bytesRead != -1) {
                            outStream.write(buffer, 0, bytesRead)
                            bytesRead = it.read(buffer)
                        }
                        outStream.close()
                        it.close()
                        Maybe.just(file)
                    }

        }
    }


    private fun <T : Any> dictionaryToObject(id: String, sequence: Long, dictionary: Dictionary, kClass: KClass<T>): T {
        val map = dictionary.toMap()
        map["_id"] = id
        map["_sequence"] = sequence
        return mapper.convertValue(map, kClass.java)
    }





}