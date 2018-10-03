package unicauca.sing4all.data.net


import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NNApi {

    @GET("/")
    fun getResponseNN(@Query("menique") menique: Float,@Query("medio") medio: Float,
                      @Query("indice") indice: Float,@Query("pulgar") pulgar: Float ): Single<ResponseNN>
}

    data class ResponseNN(var result:String?, var probability:String?)