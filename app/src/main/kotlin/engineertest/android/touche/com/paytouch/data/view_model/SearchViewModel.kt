package engineertest.android.touche.com.paytouch.data.view_model

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener
import engineertest.android.touche.com.paytouch.BR
import engineertest.android.touche.com.paytouch.R
import android.databinding.BindingAdapter
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import engineertest.android.touche.com.paytouch.ui.adapters.AutoCompleteAdapter
import engineertest.android.touche.com.paytouch.ui.views.AutoCompleteTextView

/**
 * Created by archie on 21/5/17.
 */
class SearchViewModel(private val context: Context): BaseObservable() {

    enum class ClickType {
        Close,
        Search
    }

    private var itemChangedCount = 0
    var onClickListener: (type: ClickType) -> Unit = {}
    var onTextChange: (text: String) -> Unit = {}
    var onChecked: (isTop: Boolean) -> Unit = {}
    var onLocationFirstChanged: () -> Unit = {}
    var onPopularityRangeChange: (min: Number, max: Number) -> Unit = {_, _ ->
    }

    val hasLocation: Boolean
    get() = (itemChangedCount > 1)

    var name: String = ""
    @get:Bindable
    var selectedLocation: Int = 0
        set(value){
            field = value
            if(itemChangedCount == 1)
                onLocationFirstChanged()
            if(itemChangedCount <= 1)
                itemChangedCount++
            notifyPropertyChanged(BR.selectedLocation)
        }

    val onCloseClick: View.OnClickListener
        get() = View.OnClickListener {
            onClickListener(ClickType.Close)
        }

    val onSearchClick: View.OnClickListener
        get() = View.OnClickListener {
            onClickListener(ClickType.Search)
        }

    val onCheckedChange: RadioGroup.OnCheckedChangeListener
    get() = object: RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checked: Int) {
            when(checked){
                R.id.radio_button_yes -> onChecked(true)
                R.id.radio_button_no -> onChecked(false)
            }
        }
    }

    var nameAdapter: AutoCompleteAdapter = AutoCompleteAdapter(context, R.layout.item_spinner_row)

    @get:Bindable
    var locationAdapter: ArrayAdapter<String> = ArrayAdapter(context, R.layout.item_spinner_row)
        set(value) {
            field = value
            notifyPropertyChanged(BR.locationAdapter)
        }

    val popularityListener: OnRangeSeekbarChangeListener
        get() = object: OnRangeSeekbarChangeListener {
            override fun valueChanged(minValue: Number?, maxValue: Number?) {
                if(minValue != null && maxValue != null) {
                    currentMinPopularityRange = minValue.toString()
                    currentMaxPopularityRange = maxValue.toString()
                    onPopularityRangeChange(minValue, maxValue)
                }

            }
        }

    var minPopularityRange: Float = 0f
        set(value) {
            field = value
            currentMinPopularityRange = value.toString()
        }
    var maxPopularityRange: Float = 100f
        set(value) {
            field = value
            currentMaxPopularityRange = value.toString()
        }

    @get:Bindable
    var currentMinPopularityRange: String = "0"
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentMinPopularityRange)
        }

    @get:Bindable
    var currentMaxPopularityRange: String = "100"
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentMaxPopularityRange)
        }

    fun onTextChanged(s: CharSequence, @Suppress("UNUSED_PARAMETER") start: Int,
                      @Suppress("UNUSED_PARAMETER")before: Int,
                      @Suppress("UNUSED_PARAMETER") count: Int) {
        onTextChange(s.toString())
    }
}

@BindingAdapter("minValue")
fun setMinValue(view: CrystalRangeSeekbar, value: Float?) {
    if(value != null)
        view.setMinValue (value)
}

@BindingAdapter("maxValue")
fun setMaxValue(view: CrystalRangeSeekbar, value: Float?) {
    if(value != null)
        view.setMaxValue (value)
}

@BindingAdapter("adapter")
fun setMaxValue(view: AutoCompleteTextView, adapter: AutoCompleteAdapter?) {
    view.setAdapter(adapter)
}