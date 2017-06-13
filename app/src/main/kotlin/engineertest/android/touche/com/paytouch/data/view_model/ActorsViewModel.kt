package engineertest.android.touche.com.paytouch.data.view_model

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import engineertest.android.touche.com.paytouch.BR
import engineertest.android.touche.com.paytouch.ui.adapters.ActorsAdapter
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout

/**
 * Created by archie on 23/5/17.
 */

class ActorsViewModel(): BaseObservable() {

    var loadingState: Boolean = true
    set(value) {
        field = value

        if(value) {
            if(!refreshing)
                loadingVisibility = View.VISIBLE
            actorsVisibility = View.GONE
        }
        else {
            loadingVisibility = View.GONE
            actorsVisibility = View.VISIBLE
            refreshing = false
            adapter?.footerView = null
        }
    }

    var onRefreshListener: WaveSwipeRefreshLayout.OnRefreshListener =
            WaveSwipeRefreshLayout.OnRefreshListener { }

    @get:Bindable
    var refreshing: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.refreshing)
        }

    val refreshListener: WaveSwipeRefreshLayout.OnRefreshListener
            = WaveSwipeRefreshLayout.OnRefreshListener {
                refreshing = true
                onRefreshListener.onRefresh()
            }

    @get:Bindable
    var loadingVisibility: Int = View.VISIBLE
        set(value) {
            field = value
            notifyPropertyChanged(BR.loadingVisibility)
        }

    @get:Bindable
    var actorsVisibility: Int = View.GONE
        set(value) {
            field = value
            notifyPropertyChanged(BR.actorsVisibility)
        }

    @get:Bindable
    var scrollListener: RecyclerView.OnScrollListener? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.scrollListener)
        }

    @get:Bindable
    var adapter: ActorsAdapter? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.adapter)
        }

    @get:Bindable
    var layoutManager: RecyclerView.LayoutManager? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.layoutManager)
        }

}

