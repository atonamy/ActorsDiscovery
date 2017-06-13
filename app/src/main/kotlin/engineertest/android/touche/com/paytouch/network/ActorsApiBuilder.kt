package engineertest.android.touche.com.paytouch.network

/**
 * Created by archie on 21/5/17.
 */
class ActorsApiBuilder : RetrofitApiBuilder<ActorsApi>() {
    override fun build(): ActorsApi {
        return retrofit.create(ActorsApi::class.java)
    }
}