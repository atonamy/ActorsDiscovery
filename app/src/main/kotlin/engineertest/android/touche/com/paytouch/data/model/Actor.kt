package engineertest.android.touche.com.paytouch.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

/**
 * Created by archie on 19/5/17.
 */

open class Actor : RealmObject() {

    @SerializedName("identifier")
    @PrimaryKey
    var identifier: Int = 0

    @SerializedName("adult")
    var adult: Boolean = false

    @SerializedName("name")
    @Index
    var name: String = ""

    @SerializedName("popularity")
    var popularity: Double = 0.0

    @SerializedName("profile_path")
    var profilePath: String = ""

    @SerializedName("location")
    @Index
    var location: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("top")
    @Index
    var top: Boolean = false

    @Expose
    var filmography: RealmList<Film> = RealmList<Film>()

    @SerializedName("known_for")
    @Ignore
    var knownFor: List<Film> = filmography
}