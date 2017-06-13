package engineertest.android.touche.com.paytouch.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by archie on 20/5/17.
 */
open class Film: RealmObject() {

    @SerializedName("id")
    @PrimaryKey
    var id: Int = 0

    @SerializedName("adult")
    var adult: Boolean = false

    @SerializedName("title")
    var title: String = ""

    @SerializedName("original_title")
    var originalTitle: String = ""

    @SerializedName("release_date")
    var releaseDate: Date? = null

    @SerializedName("popularity")
    var popularity: Double = 0.0

    @SerializedName("video")
    var video: Boolean = false

    @SerializedName("vote_average")
    var voteAverage: Double = 0.0

    @SerializedName("vote_count")
    var voteCount: Int = 0

    @SerializedName("media_type")
    var mediaType: String = ""

    @SerializedName("backdrop_path")
    var backdropPath: String = ""

    @SerializedName("poster_path")
    var posterPath: String = ""

    @SerializedName("description")
    var description: String = ""
}