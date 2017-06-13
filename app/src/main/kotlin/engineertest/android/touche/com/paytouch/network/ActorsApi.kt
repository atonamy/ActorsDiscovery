package engineertest.android.touche.com.paytouch.network

import engineertest.android.touche.com.paytouch.data.api.ActorsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by archie on 19/5/17.
 */
interface ActorsApi {
    @GET("rest/actors")
    fun loadActors(@Query("page") page: Int): Call<ActorsResponse>
}