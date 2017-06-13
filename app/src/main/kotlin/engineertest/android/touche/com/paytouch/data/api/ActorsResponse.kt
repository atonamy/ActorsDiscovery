package engineertest.android.touche.com.paytouch.data.api

import com.google.gson.annotations.SerializedName
import engineertest.android.touche.com.paytouch.data.model.Actor

/**
 * Created by archie on 20/5/17.
 */
class ActorsResponse (
        @SerializedName("data") val actors: List<Actor>,
        @SerializedName("totalElements") val totalElements: Int
)