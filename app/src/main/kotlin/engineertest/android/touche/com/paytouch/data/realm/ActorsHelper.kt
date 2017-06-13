package engineertest.android.touche.com.paytouch.data.realm

import engineertest.android.touche.com.paytouch.data.model.Actor
import io.realm.*

/**
 * Created by archie on 22/5/17.
 */
class ActorsHelper {

    enum class SortType (val type: Int){
        ByName(0),
        ByPopularity(1)
    }

    companion object {
        fun parseSortType(value: Int): SortType {
            val values = SortType.values()
            if (value < 0 || value >= values.size)
                return SortType.ByName
            return values[value]
        }
    }

    private val dbManager: DatabaseManager = DatabaseManager()

    private fun getSorted(query: RealmQuery<Actor>, sort: SortType): RealmResults<Actor> {
        when (sort) {
            SortType.ByName -> return query.findAllSorted("name")
            SortType.ByPopularity -> return query.findAllSorted("popularity", Sort.DESCENDING)
        }
    }

    fun getActors(page: Int, sort: SortType): List<Actor> {

        return dbManager.executeDbQuery {
            val itemsPerPage = 20
            val result = getSorted(it.where(Actor::class.java), sort)
            it.copyFromRealm(getPaginatingList(page-1, itemsPerPage, result))
        }
    }

    fun getPopularityRange(): Pair<Float, Float>? {
        return dbManager.executeDbQuery {
            val min = it.where(Actor::class.java).min("popularity").toFloat()
            val max = it.where(Actor::class.java).max("popularity").toFloat()
            Pair(min, max)

        }
    }

    fun getPopularityRangeAsync(result: (Pair<Float, Float>?) -> Unit) {
        dbManager.executeDbQueryAsync({
            getPopularityRange()
        }, result)
    }

    fun getLocations() : List<String> {
        return dbManager.executeDbQuery {
            val result = it.where(Actor::class.java).distinct("location").sort("location")
            val i = result.iterator()
            val locations = ArrayList<String>()
            while(i.hasNext())
                locations.add(i.next().location)
            locations

        }
    }

    fun getLocationsAsync(result: (List<String>?) -> Unit) {
        dbManager.executeDbQueryAsync({
            getLocations()
        }, result)
    }

    fun getActorsAsync(page: Int, sort: SortType, result: (actors: List<Actor>?) -> Unit) {
        dbManager.executeDbQueryAsync({
            getActors(page, sort)
        }, result)
    }

    fun addOrUpdateActors(actors: List<Actor>){
        dbManager.executeDbQuery {
            it.beginTransaction()
            it.copyToRealmOrUpdate(actors)
            it.commitTransaction()
            null
        }
    }

    fun addOrUpdateActorsAsync(actors: List<Actor>, done: (Nothing?) -> Unit) {
        dbManager.executeDbQueryAsync({
            addOrUpdateActors(actors)
            null
        }, done)
    }

    fun searchActors(sort: SortType,
                     name: String? = null,
                     location: String? = null,
                     top: Boolean? = null,
                     popularity_from: Double? = null,
                     popularity_to: Double? = null): List<Actor> {
        return dbManager.executeDbQuery {
            var query = it.where(Actor::class.java)
            if(name != null)
                query = query.contains("name", name, Case.INSENSITIVE)
            if(location != null)
                query = query.contains("location", location, Case.INSENSITIVE)
            if(top != null)
                query = query.equalTo("top", top)
            if(popularity_from != null && popularity_to != null) {
                query = query.greaterThanOrEqualTo("popularity", popularity_from)
                        .lessThanOrEqualTo("popularity", popularity_to)
            }
            val result = getSorted(query, sort)
            if(result.count() > 60)
                it.copyFromRealm(result.subList(0, 60))
            else
                it.copyFromRealm(result.toList())
        }

    }

    fun searchActorsAsync(sort: SortType,
                          name: String? = null,
                          location: String? = null,
                          top: Boolean? = null,
                          popularity_from: Double? = null,
                          popularity_to: Double? = null, result: (actors: List<Actor>?) -> Unit) {
        dbManager.executeDbQueryAsync({
            searchActors(sort, name, location, top, popularity_from, popularity_to)
        }, result)
    }

    fun getActor(actorId: Int): Actor? {
        return dbManager.executeDbQuery {
            it.copyFromRealm(it.where(Actor::class.java).equalTo("identifier", actorId).findFirst())
        }
    }

    fun getActorAsync(actorId: Int, result: (actors: Actor?) -> Unit) {
        dbManager.executeDbQueryAsync({
            getActor(actorId)
        }, result)
    }

    fun clear() {
        return dbManager.executeDbQuery {
            it.beginTransaction()
            it.delete(Actor::class.java)
            it.commitTransaction()
        }
    }

    fun clearAsync() {
        dbManager.executeDbQueryAsync({
            clear()
            null
        }, {

        })
    }

    fun getPaginatingList(page: Int, pageSize: Int, result: RealmResults<Actor>): List<Actor> {
        val size = result.size
        val from = page * pageSize
        if (from >= size)
            return RealmList<Actor>()
        var to = from + pageSize
        if (size < to)
            to -= to - size
        val result_list = result.subList(from, to)
        return result_list
    }

    fun release() {
        dbManager.release()
    }

}