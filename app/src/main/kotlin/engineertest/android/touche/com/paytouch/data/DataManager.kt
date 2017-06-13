package engineertest.android.touche.com.paytouch.data

import android.content.Context
import android.util.Log
import engineertest.android.touche.com.paytouch.R
import engineertest.android.touche.com.paytouch.data.api.ActorsResponse
import engineertest.android.touche.com.paytouch.data.model.Actor
import engineertest.android.touche.com.paytouch.data.realm.ActorsHelper
import engineertest.android.touche.com.paytouch.injection.components.DaggerDataManagerComponent
import engineertest.android.touche.com.paytouch.injection.modules.DataManagerModule
import engineertest.android.touche.com.paytouch.network.ActorsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by archie on 22/5/17.
 */
class DataManager(val context: Context?) {

    @Inject
    lateinit var actorsApi: ActorsApi
    private val actorsDatabase: ActorsHelper = ActorsHelper()
    private var loadingActors = false
    private var totalPages = 1
    private var currentPage: Int = 1

    var onError: (message: String) -> Unit = { }

    init {
        DaggerDataManagerComponent.builder().dataManagerModule(DataManagerModule()).build().inject(this)
    }

    val hasNext
    get() = currentPage <= totalPages


    private inline fun populateActorsResult(actors: List<Actor>, result: (actors: List<Actor>) -> Unit) {
        currentPage++
        result(actors)
        loadingActors = false
    }

    fun loadNextActors(sort: ActorsHelper.SortType, result: (actors: List<Actor>) -> Unit) {

        Log.d(DataManager::class.java.simpleName, "loadNextActors")
        if(!loadingActors && currentPage <= totalPages) {
            loadingActors = true
            actorsDatabase.getActorsAsync(currentPage, sort, {
                if(it == null || it.count() == 0)
                    loadActorsFromInternet { actors ->
                            actors.forEach {
                                it.filmography.addAll(it.knownFor)
                            }
                            actorsDatabase.addOrUpdateActorsAsync(actors) {
                                populateActorsResult(actors, result)
                            }
                    }
                else{
                    populateActorsResult(it, result)
                }
            })
        }
    }



    fun searchActors(sort: ActorsHelper.SortType,
                     name: String? = null,
                     location: String? = null,
                     top: Boolean? = null,
                     popularity_from: Double? = null,
                     popularity_to: Double? = null) : List<Actor> {

        return actorsDatabase.searchActors(sort, name, location, top, popularity_from, popularity_to)
    }

    fun searchActorsAsync(sort: ActorsHelper.SortType,
                     name: String? = null,
                     location: String? = null,
                     top: Boolean? = null,
                     popularity_from: Double? = null,
                     popularity_to: Double? = null,
                          result: (List<Actor>?) -> Unit) {

        return actorsDatabase.searchActorsAsync(sort, name, location, top, popularity_from,
                popularity_to, result)
    }

    fun getPopularityRange(result: (Pair<Float, Float>?) -> Unit) {
        actorsDatabase.getPopularityRangeAsync (result)
    }

    fun getLocations(result: (List<String>?) -> Unit) {
        actorsDatabase.getLocationsAsync (result)
    }

    fun getActor(actorId: Int, result: (actor: Actor?) -> Unit) {
        actorsDatabase.getActorAsync(actorId, result)
    }

    private fun loadActorsFromInternet(result: (actors: List<Actor>) -> Unit) {
        actorsApi.loadActors(currentPage).enqueue(object : Callback<ActorsResponse> {
            override fun onFailure(call: Call<ActorsResponse>?, t: Throwable?) {
                if (context != null)
                    showError(t?.message ?: context.getString(R.string.error_message))
                loadingActors = false
                Log.e(DataManager::class.java.simpleName, t?.toString())
            }

            override fun onResponse(call: Call<ActorsResponse>?, response: Response<ActorsResponse>?) {
                if (response?.body()?.actors != null) {
                    if(currentPage == 1)
                        evaluateTotalPages(response.body()!!.totalElements,
                            response.body()!!.actors.size)
                    result(response.body()?.actors!!)
                }
                else {
                    totalPages = currentPage
                    result(ArrayList<Actor>())
                }
            }
        })
    }

    private inline fun evaluateTotalPages(totalElements: Int, pageSize: Int) {
        totalPages = (totalElements + pageSize - 1) / pageSize
    }

    fun reset(fully: Boolean = false) {
        if(fully)
            actorsDatabase.clearAsync()
        currentPage = 1;
    }

    private fun showError(message: String) {
        onError(message)
    }

    fun release() {
        actorsDatabase.release()
    }


}