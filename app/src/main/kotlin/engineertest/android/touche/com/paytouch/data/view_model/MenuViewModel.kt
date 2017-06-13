package engineertest.android.touche.com.paytouch.data.view_model

import android.app.Activity
import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.databinding.Observable
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import engineertest.android.touche.com.paytouch.BR
import engineertest.android.touche.com.paytouch.R
import engineertest.android.touche.com.paytouch.ui.views.Spinner

/**
 * Created by archie on 21/5/17.
 */
class MenuViewModel(private val context: Context): BaseObservable() {

    private var sortTypeInitialized: Boolean = false
    var searchMode: Boolean = false
        set(value) {
            field = value
            if(value)
                searchIcon = ContextCompat.getDrawable(context,
                    R.drawable.mglass_green)
            else
                searchIcon = ContextCompat.getDrawable(context,
                        R.drawable.mglass_big)
        }

    fun toggleSearch() {
        searchMode = !searchMode
        onSearchClick()
    }

    var sortTypeListener: (type: Int) -> Unit = {}
    var propertyChangedCallback: () -> Unit = {}
    var onSearchClick: () -> Unit = {}
    var onSortClick: (View) -> Unit = {}

    var searchIcon: Drawable = ContextCompat.getDrawable(context,
            R.drawable.mglass_big)
    set(value){
        field = value
        propertyChangedCallback()
    }

    @get:Bindable
    var background: Int = Color.TRANSPARENT
        set(value) {
            field = value
            notifyPropertyChanged(BR.background)
        }

    var sortType: Int = 0
        set(value) {
            field = value
            background = Color.TRANSPARENT;
            if(sortTypeInitialized)
                sortTypeListener(value)
            else
                sortTypeInitialized = true
        }

    val onClick: View.OnClickListener
        get() = View.OnClickListener {
            onSortClick(it)
        }

    val menuOrderEvents =
            object: Spinner.OnSpinnerEventsListener {
                override fun onSpinnerOpened() {
                    background = ContextCompat.getColor(context, R.color.colorMenu)
                }

                override fun onSpinnerClosed() {
                    background = Color.TRANSPARENT
                }
            }
}

@BindingAdapter("events")
fun setMenuOrderEvents(view: Spinner, value: Spinner.OnSpinnerEventsListener?) {
    if(value != null)
        view.onSpinnerEventsListener = value
}