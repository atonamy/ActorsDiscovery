package engineertest.android.touche.com.paytouch.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import engineertest.android.touche.com.paytouch.R
import engineertest.android.touche.com.paytouch.data.DataManager
import engineertest.android.touche.com.paytouch.data.model.Actor
import engineertest.android.touche.com.paytouch.data.realm.ActorsHelper
import engineertest.android.touche.com.paytouch.data.view_model.SearchViewModel
import engineertest.android.touche.com.paytouch.databinding.DialogSearchBinding
import engineertest.android.touche.com.paytouch.ui.adapters.AutoCompleteAdapter
import android.view.View
import android.view.ViewGroup
import engineertest.android.touche.com.paytouch.ui.views.TextView
import engineertest.android.touche.com.paytouch.ui.views.showWarning


/**
 * Created by archie on 24/5/17.
 */
class SearchDialog(private val context: Context,
                   private val dataManager: DataManager,
                   private val inflater: LayoutInflater = LayoutInflater.from(context)) {

    val viewModel = SearchViewModel(context)
    private val binding = DialogSearchBinding.inflate(inflater)
    private var top: Boolean? = null
    private var locations: List<String>? = null
    private lateinit var dialog: AlertDialog
    var isInit: Boolean = false
    private lateinit var minMaxPopularity: Pair<Number, Number>
    var sortType: ActorsHelper.SortType = ActorsHelper.SortType.ByName
    var onSearch: () -> Unit = {}
    var onSearchResult: (List<Actor>?) -> Unit = {}
    var onReady: () -> Unit = {}
    var isOpen: Boolean = false
    val isNoSearchCriteria: Boolean
    get() = (viewModel.name.trim().isEmpty() &&
            !viewModel.hasLocation &&
            top == null &&
            viewModel.currentMinPopularityRange.contentEquals(minMaxPopularity.first.toInt().toString()) &&
            viewModel.currentMaxPopularityRange.contentEquals(minMaxPopularity.second.toInt().toString()))

    class LocationsAdapter(private val ctx: Context,
                           textViewResourceId: Int,
                           val items: List<String>)
        : ArrayAdapter<String>(ctx, textViewResourceId, items) {

        private var firstTime = true
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var row: TextView = super.getView(position, convertView, parent) as TextView
            if(firstTime){
                firstTime = false
                row = LayoutInflater.from(ctx)
                            .inflate( R.layout.item_spinner_row, parent, false) as
                    TextView
                row.setTextColor(ContextCompat.getColor(ctx, R.color.background))
                row.setText(R.string.select_location)
            }
            return row
        }
    }

    init {
        initNameAdapter()
        initLocationAdapter()
    }

    fun open() {
        if(!isInit && !isOpen)
            return
        isOpen = true
        dialog = AlertDialog.Builder(context)
                .setView(binding.root)
                .create()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        binding.viewModel = viewModel

        dialog.setOnDismissListener {
            isOpen = false
        }

        Handler().postDelayed({
            viewModel.currentMinPopularityRange = minMaxPopularity.first.toInt().toString()
            viewModel.currentMaxPopularityRange = minMaxPopularity.second.toInt().toString()
        }, 125)

    }

    fun close() {
        if(isOpen)
            dialog.dismiss()
    }


    private fun initLocationAdapter() {
        dataManager.getLocations {
            if(it != null) {
                viewModel.locationAdapter = LocationsAdapter(context,
                        R.layout.item_spinner_row, it)
                viewModel.onLocationFirstChanged = {
                    viewModel.locationAdapter = viewModel.locationAdapter
                }
            }
            locations = it
            initPopularityRange()
        }
    }

    private fun initPopularityRange() {
        dataManager.getPopularityRange {
            if(it != null) {
                viewModel.minPopularityRange = it.first.toInt().toFloat()
                viewModel.maxPopularityRange = (it.second.toInt() + 1).toFloat()
                minMaxPopularity = Pair(viewModel.minPopularityRange, viewModel.maxPopularityRange)
            }
            initListeners()
        }
    }

    private fun initListeners() {
        viewModel.onChecked = {
            top = it
        }

        viewModel.onClickListener = {
            when(it){
                SearchViewModel.ClickType.Close -> close()
                SearchViewModel.ClickType.Search -> performSearch()
            }
        }
        viewModel.onPopularityRangeChange = {min, max ->
            viewModel.currentMinPopularityRange = min.toString()
            viewModel.currentMaxPopularityRange = max.toString()
        }
        isInit = true
        onReady()
    }


    private fun performSearch() {
        if(isNoSearchCriteria){
            context.showWarning(context.getString(R.string.search_title),
                    context.getString(R.string.search_message))
            return
        }
        Handler().postDelayed({
            close()
        }, 500)

        onSearch()
        dataManager.searchActorsAsync(sortType,
                if (viewModel.name.trim().isEmpty()) null else viewModel.name,
                if (viewModel.hasLocation) locations!![viewModel.selectedLocation] else null,
                if (top != null) top else null,
                viewModel.currentMinPopularityRange.toDouble(),
                viewModel.currentMaxPopularityRange.toDouble()
        ) {
            onSearchResult(it)
        }
    }

    private fun initNameAdapter() {
        viewModel.nameAdapter.setAutoCompleteFeedback(object : AutoCompleteAdapter.AutoCompleteFeedback {
            override val minimumQueryLength: Int
                get() = 3

            override fun searchAutocomplete(query: String): List<AutoCompleteAdapter.AutoCompleteData> {
                val actors = dataManager.searchActors(sortType,
                        query)
                val names = ArrayList<AutoCompleteAdapter.AutoCompleteData>()
                actors.forEach {
                    names.add(AutoCompleteAdapter.AutoCompleteData(it.identifier, it.name))
                }
                return names;
            }
        })
    }

}