package unicauca.sing4all.quantifier

import io.reactivex.Observable
import io.reactivex.Single
import org.apache.commons.lang3.tuple.MutablePair
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow
import kotlin.math.sqrt

@Singleton
class VectorQuantifier @Inject constructor() : Quantifier {

    private val cA: List<Double> = listOf(20974.94059406, 93180.75247525, 55362.62376238, 63750.87128713, 32856.86138614)
    private val cB: List<Double> = listOf(9353.86262626, 28064.66666667, 24984.34343434, 21301.43434343, 64176.05050505)
    private val cC: List<Double> = listOf(10194.66470588, 65700.02941176, 44404.17647059, 40496.97058824, 42758.95588235)
    private val cD: List<Double> = listOf(13969.62318841, 69738.76811594, 38067.60869565, 22935.56521739, 42990.33333333)
    private val cE: List<Double> = listOf(15776.24712644, 81424.78735632, 48528.13793103, 54262.8008046, 60884.29310345)
    private val cF: List<Double> = listOf(16594.63963964, 82120.38738739, 47401.81981982, 19446.34234234, 35300.41441441)
    private val cI: List<Double> = listOf(9158.87931034, 83689.55172414, 38408.70114943, 60080.26436782, 57116.14942529)
    private val cK: List<Double> = listOf(18458.97260274, 74609.34246575, 24589.01428571, 24788.24657534, 30306.83561644)
    private val cL: List<Double> = listOf(17316.390625, 86611.6171875, 52189.828125, 19871.796875, 23075.125)
    private val cM: List<Double> = listOf(18600.46280992, 36724.11570248, 37325.2892562, 30477.66115702, 54649.36363636)
    private val cN: List<Double> = listOf(18357.21341463, 77684.14634146, 33151.27439024, 34645.98780488, 52741.57926829)
    private val cO: List<Double> = listOf(13777.94303797, 75670.41772152, 44815.27848101, 53802.36075949, 50016.12658228)
    private val cP: List<Double> = listOf(17877.20125786, 77236.35849057, 46897.28930818, 44134.28930818, 48399.83018868)
    private val cQ: List<Double> = listOf(11354.16883117, 55910.79220779, 38208.11688312, 36391.38311688, 41290.14285714)
    private val cR: List<Double> = listOf(19607.76, 83256.07, 29940.84, 24586.86, 52148.07)
    private val cT: List<Double> = listOf(8896.97933333, 27805.02, 22436.65333333, 34613.95333333, 30728.1)
    private val cU: List<Double> = listOf(8980.98380952, 86947.21904762, 52786.76190476, 23491.18095238, 49400.05714286)
    private val cV: List<Double> = listOf(17582.40198507, 80534.03731343, 22284.26119403, 22090.76119403, 50291.00746269)
    private val cW: List<Double> = listOf(19839.7037037, 26221.96296296, 33109.2962963, 21122.22222222, 50623.51851852)
    private val cX: List<Double> = listOf(18624.96815287, 86351.38853503, 50422.17197452, 45572.08917197, 55419.54140127)
    private val cY: List<Double> = listOf(9104.7192053, 80102.03311258, 48832.39072848, 56218.7615894, 27882.67549669)
    private val cEsp: List<Double> = listOf(8475.34029851, 25905.34328358, 19932.56716418, 21180.40298507, 45583.67164179)

    override fun calculateChar(hand: Hand): Single<List<String>> = Observable.fromArray(cA to "a", cB to "b", cC to "c", cD to "d", cE to "e",
            cF to "f", cI to "i", cK to "k", cL to "l", cM to "m", cN to "n", cO to "o", cP to "p",
            cQ to "q", cR to "r", cT to "t", cU to "u", cV to "v", cW to "w", cX to "x", cY to "y",
            cEsp to "es")
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