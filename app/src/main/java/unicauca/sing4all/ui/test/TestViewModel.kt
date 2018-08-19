package unicauca.sing4all.ui.test

import android.arch.lifecycle.ViewModel
import com.opencsv.CSVReader
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.toObservable
import unicauca.sing4all.data.models.ReportChar
import unicauca.sing4all.data.models.ReportGlobal
import unicauca.sing4all.data.models.ReportTest
import unicauca.sing4all.data.preferences.Algorithm
import unicauca.sing4all.data.preferences.UserSession
import unicauca.sing4all.quantifier.Hand
import unicauca.sing4all.quantifier.StepQuantifier
import unicauca.sing4all.util.applySchedulers
import java.io.FileDescriptor
import java.io.FileReader
import java.io.InputStream
import java.util.*
import javax.inject.Inject

class TestViewModel @Inject constructor(private val step: StepQuantifier,
                                        private val session: UserSession) : ViewModel() {

    private lateinit var start: Date
    private lateinit var end: Date


    private fun openCsv(fd: FileDescriptor): Single<List<Array<String>>> = Single.create {
        val reader = FileReader(fd)
        it.onSuccess(CSVReader(reader).readAll())
    }

    fun test(fd: FileDescriptor): Single<Pair<List<ReportChar>, ReportGlobal>> = when(session.algorithm){
        Algorithm.STAGES -> testStage(fd)
        else -> testStage(fd)
    }

    private fun testStage(fd: FileDescriptor): Single<Pair<List<ReportChar>, ReportGlobal>> = openCsv(fd)
            .flatMapObservable { it.toObservable() }
            .map { Hand(it[0].toInt(), it[1].toInt(), it[2].toInt(), it[3].toInt(), it[4].toInt()) to indexToChar(it[5]) }
            .doOnNext { start = Date() }
            .flatMapSingle { Singles.zip(Single.just(it.second), step.calculateChar(it.first)) }
            .doOnNext { end = Date() }
            .map { ReportTest(start, end, end.time - start.time, it.first == it.second[0], it.first, it.second) }
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
            }.applySchedulers()


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
        else -> " "
    }


}