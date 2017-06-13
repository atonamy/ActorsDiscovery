package engineertest.android.touche.com.paytouch.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializer
import java.lang.reflect.Type
import java.util.*


/**
 * Created by archie on 20/5/17.
 */
abstract class RetrofitApiBuilder<T> {

    protected var timeoutConnectionMinutes: Long = 1
    protected var timeoutReadMinutes: Long = 1
    protected var baseApiUrl: String = "http://test.gotouche.com/"

    fun setTimeout(minutes: Long): RetrofitApiBuilder<T> {
        timeoutConnectionMinutes = minutes
        timeoutReadMinutes = minutes
        return this
    }

    fun setConnectionTimeout(minutes: Long): RetrofitApiBuilder<T> {
        timeoutConnectionMinutes = minutes
        return this
    }

    fun setReadingTimeout(minutes: Long): RetrofitApiBuilder<T> {
        timeoutReadMinutes = minutes
        return this
    }

    fun setBaseUrl(url: String): RetrofitApiBuilder<T> {
        baseApiUrl = url
        return this
    }

    protected open val httpClient: OkHttpClient
        get() = OkHttpClient.Builder()
                .connectTimeout(timeoutConnectionMinutes, TimeUnit.MINUTES)
                .readTimeout(timeoutReadMinutes, TimeUnit.MINUTES)
                .build()

    protected open val jsonParser: Gson
        get() {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                @Throws(JsonParseException::class)
                override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
                    return Date(json.asJsonPrimitive.asLong)
                }
            })
            return builder.create()
        }

    protected open val retrofit: Retrofit
        get() = Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(jsonParser))
                .client(httpClient)
                .build()

    abstract fun build(): T
}