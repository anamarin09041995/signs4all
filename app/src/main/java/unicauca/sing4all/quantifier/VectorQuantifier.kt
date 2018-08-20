package unicauca.sing4all.quantifier

import io.reactivex.Observable
import io.reactivex.Single
import org.apache.commons.lang3.tuple.MutablePair
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow
import kotlin.math.sqrt

@Singleton
class VectorQuantifier @Inject constructor(): Quantifier {

    private val cA :List<Double> = listOf(21287.55714286, 92956.51428571, 56994.12857143, 62624.17142857, 30740.28571429)
    private val cB :List<Double> = listOf(9582.13285714, 27756.35714286, 25219.45714286, 21161.34285714, 62846.61428571)
    private val cC :List<Double> = listOf(10387.95, 68955.68571429, 43645.75714286, 40174.28571429, 42625.38571429)
    private val cD :List<Double> = listOf(13992.92857143, 69764.11428571, 45177.68571429, 22911.55714286, 43041.98571429)
    private val cE :List<Double> = listOf(17027.37142857, 83986.04285714, 53230.55714286, 54569.47142857, 62997.84285714)
    private val cF :List<Double> = listOf(17836.6, 83636.1, 55475.88571429, 19706.84285714, 34062.84285714)
    private val cI :List<Double> = listOf(9254.47428571, 83063.84285714, 45094.54285714, 59117.01428571, 56402.37142857)
    private val cK :List<Double> = listOf(18465.65714286, 74312.94285714, 24308.01428571, 25025.55714286, 30531.94285714)
    private val cL :List<Double> = listOf(17264.55714286, 87263.51428571, 56057.62857143, 19666.8, 22117.25714286)
    private val cM :List<Double> = listOf(17594.6, 35304.98571429, 33393.5, 29576.2, 53205.94285714)
    private val cN :List<Double> = listOf(18485.47142857, 78863.45714286, 32663.28571429, 29875.57142857, 50391.31428571)
    private val cO :List<Double> = listOf(13883.38571429, 74786.71428571, 50514.87142857, 52474.67142857, 47306.37142857)
    private val cP :List<Double> = listOf(18030.5, 79846.71428571, 49310.8, 36650.6, 48329.74285714)
    private val cQ :List<Double> = listOf(11514.61428571, 58630.32857143, 35774.17142857, 38538.14285714, 41632.17142857)
    private val cR :List<Double> = listOf(19672.75714286, 84129.5, 24773.82857143, 25874.1, 53644.62857143)
    private val cT :List<Double> = listOf(9256.56142857, 27531.91428571, 23138.61428571, 38409.57142857, 29808.6)
    private val cU :List<Double> = listOf(9050.81571429, 86978.01428571, 59937.75714286, 24077.67142857, 46400.52857143)
    private val cV :List<Double> = listOf(16389.8, 84671.34285714, 22457.42857143, 22868.31428571, 48882.67142857)
    private val cW :List<Double> = listOf(20033.38571429, 26164.9, 22504.21428571, 20791.14285714, 51486.41428571)
    private val cX :List<Double> = listOf(18458.95714286, 85863.58571429, 49680.01428571, 41380.05714286, 53511.54285714)
    private val cY :List<Double> = listOf(9470.66857143, 81627.98571429, 48615.35714286, 57266.58571429, 32105.25714286)
    private val cEsp :List<Double> = listOf(8443.69, 25933.84285714, 20505.7, 21034.74285714, 45474.92857143)

    override fun calculateChar(hand: Hand): Single<List<String>> = Observable.fromArray(cA to "a", cB to "b", cC to "c", cD to "d", cE to "e",
            cF to "f", cI to "i", cK to "k", cL to "l", cM to "m", cN to "n", cO to "o", cP to "p",
            cQ to "q", cR to "r", cT to "t", cU to "u", cV to "v", cW to "w", cX to "x", cY to "y",
            cEsp to "_")
            .map { listOf(it.first[0] - hand.me, it.first[1] - hand.an, it.first[2] - hand.co, it.first[3] - hand.ind, it.first[4] - hand.pu) to it.second }
            .map { sqrt(it.first[0].pow(2) + it.first[1].pow(2) + it.first[2].pow(2)) + it.first[3].pow(2) + it.first[4].pow(2) to it.second }
            .reduce(MutablePair<Double, String>(0.0, "")) { a, v ->
                if (a.left == 0.0 || a.left >= v.first) {
                    a.left = v.first
                    a.right = v.second
                }
                a
            }
            .map { listOf(it.right) }


}