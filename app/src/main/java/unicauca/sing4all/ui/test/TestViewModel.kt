package unicauca.sing4all.ui.test

import android.arch.lifecycle.ViewModel
import com.opencsv.CSVReader
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.rxkotlin.toObservable
import unicauca.sing4all.data.models.ReportChar
import unicauca.sing4all.data.models.ReportGlobal
import unicauca.sing4all.data.models.ReportTest
import unicauca.sing4all.data.preferences.Algorithm
import unicauca.sing4all.data.preferences.UserSession
import unicauca.sing4all.quantifier.*
import unicauca.sing4all.util.applySchedulers
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit

import javax.inject.Inject

class TestViewModel @Inject constructor(private val step: StepQuantifier,
                                        private val vector: VectorQuantifier,
                                        private val both: BothQuantifier,
                                        private val neural: NeuralNet,
                                        private val session: UserSession) : ViewModel() {
    private lateinit var start: Date
    private lateinit var end: Date


    fun prepareCsv(file: File, input: InputStream): Single<List<Array<String>>> = Single.create {

        if (!file.exists()) {
            val out = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var read = input.read(buffer)
            while (read != -1) {
                out.write(buffer, 0, read)
                read = input.read(buffer)
            }
            out.close()
        }
        input.close()

        val reader = FileReader(file)
        it.onSuccess(CSVReader(reader).readAll())
    }

    fun test(data: List<Array<String>>): Single<Pair<List<ReportChar>, ReportGlobal>> = when (session.algorithm) {
        Algorithm.STAGES -> testAlg(data, step)
        Algorithm.VECTORIAL -> testAlg(data, vector)
        Algorithm.BOTH -> testAlg(data, both)
        else -> testAlg(data, neural)
    }.applySchedulers()


    private fun testAlg(data: List<Array<String>>, alg: Quantifier): Single<Pair<List<ReportChar>, ReportGlobal>> = data.toFlowable()
                    .concatMap { Flowable.just(it).delay(50, TimeUnit.MILLISECONDS) }
                    .filter { !it.contains("") }
                    .map { arrayOf(it[0].toFloat().toInt(), it[1].toFloat().toInt(), it[2].toFloat().toInt(), it[3].toFloat().toInt(), it[4].toFloat().toInt()) to indexToChar(it[5]) }
                    .map { Hand(it.first[0], it.first[1], it.first[2], it.first[3], it.first[4]) to it.second }
                    .doOnNext { start = Date() }
                    .flatMapSingle { Singles.zip(Single.just(it.second), alg.calculateChar(it.first)) }
                    .doOnNext { end = Date() }
                    .map { ReportTest(start, end, end.time - start.time, it.second.isNotEmpty() && it.first == it.second[0], it.first, it.second) }
                    .groupBy { it.charExpected }
                    .flatMapSingle {
                        it.reduce(ReportChar(it.key!!)) { a, v ->
                            a.count += 1
                            if (v.success) a.success += 1
                            else a.fail += 1
                            a.time += v.duration
                            a.timeAverage = a.time.toDouble() / a.count
                            a.successProbability = a.success.toFloat() / (a.success + a.fail)
                            a
                        }
                    }

                    .toList()
                    .flatMap {
                        Singles.zip(Single.just(it),
                                it.toObservable()
                                        .reduce(ReportGlobal()) { a, v ->
                                            a.count += 1
                                            a.success += v.success
                                            a.fail += v.fail
                                            a.time += v.time
                                            a.timeAverage = a.time.toDouble() / a.count
                                            a.successProbability = a.success.toFloat() / (a.success + a.fail)
                                            a
                                        }
                        )
                    }


    private fun indexToChar(index: String): String = when (index) {
        "0" -> "a"
        "1" -> "b"
        "2" -> "c"
        "3" -> "d"
        "4" -> "e"
        "5" -> "f"
        "6" -> "i"
        "7" -> "k"
        "8" -> "l"
        "9" -> "m"
        "10" -> "n"
        "11" -> "o"
        "12" -> "p"
        "13" -> "q"
        "14" -> "r"
        "15" -> "t"
        "16" -> "u"
        "17" -> "v"
        "18" -> "w"
        "19" -> "x"
        "20" -> "y"
        else -> "es"
    }

}