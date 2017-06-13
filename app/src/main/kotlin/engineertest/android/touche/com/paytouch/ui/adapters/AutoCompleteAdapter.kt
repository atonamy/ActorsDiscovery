package engineertest.android.touche.com.paytouch.ui.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable


/**
 * Created by archie on 23/5/17.
 */
class AutoCompleteAdapter(context: Context, resource: Int) : ArrayAdapter<AutoCompleteAdapter.AutoCompleteData>(context, resource), Filterable {

    class AutoCompleteData(val id: Any, val value: String) {

        private val limitLength = 35

        override fun toString(): String {
            if (this.value.length > limitLength)
                return this.value.substring(0, limitLength) + "..."
            return this.value
        }
    }

    interface AutoCompleteFeedback {
        fun searchAutocomplete(query: String): List<AutoCompleteData>?
        val minimumQueryLength: Int
    }

    var autoSuggestions: List<AutoCompleteData>? = null
    private var autoCompleteFeedback: AutoCompleteFeedback? = null

    init {
        autoSuggestions = null
        autoCompleteFeedback = null
    }

    fun setAutoCompleteFeedback(feedback: AutoCompleteFeedback) {
        autoCompleteFeedback = feedback
    }

    override fun getCount(): Int {
        return if (autoSuggestions == null) 0 else autoSuggestions!!.size
    }

    override fun getItem(index: Int): AutoCompleteData? {
        return if (autoSuggestions == null || index >= autoSuggestions!!.size) null else autoSuggestions!![index]
    }

    override fun getFilter(): Filter {
        val myFilter = object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (autoCompleteFeedback != null && constraint != null &&
                        constraint.length >= autoCompleteFeedback!!.minimumQueryLength) {
                    val suggestions = autoCompleteFeedback?.searchAutocomplete(constraint.toString())
                    if (suggestions != null) {
                        filterResults.values = suggestions
                        filterResults.count = suggestions.size
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    val list = results.values
                    if(list is List<*>)
                        autoSuggestions = list.filterIsInstance<AutoCompleteData>()
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

        }
        return myFilter
    }
}