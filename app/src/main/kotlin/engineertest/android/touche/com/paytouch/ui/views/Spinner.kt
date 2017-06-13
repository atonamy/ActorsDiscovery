package engineertest.android.touche.com.paytouch.ui.views

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatSpinner
import android.util.AttributeSet



/**
 * Created by archie on 21/5/17.
 */
class Spinner @JvmOverloads constructor(context: Context,
                                        attrs: AttributeSet? = null,
                                        defStyleAttr: Int = 0): AppCompatSpinner(context, attrs, defStyleAttr) {


    var onSpinnerEventsListener: OnSpinnerEventsListener? = null
    private var mOpenInitiated = false

    interface OnSpinnerEventsListener {
        fun onSpinnerOpened()
        fun onSpinnerClosed()
    }

    override fun performClick(): Boolean {
        mOpenInitiated = true
        onSpinnerEventsListener?.onSpinnerOpened()
        return super.performClick()
    }

    private fun performClosedEvent() {
        mOpenInitiated = false
        onSpinnerEventsListener?.onSpinnerClosed()
    }


    val hasBeenOpened: Boolean get() = mOpenInitiated

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasBeenOpened && hasWindowFocus)
            performClosedEvent()
    }

    override fun setSelection(position: Int, animate: Boolean) {
        val sameSelected = position == selectedItemPosition
        super.setSelection(position, animate)
        if (sameSelected) {
            onItemSelectedListener!!.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }

    override fun setSelection(position: Int) {
        val sameSelected = position == selectedItemPosition
        super.setSelection(position)
        if (sameSelected) {
            onItemSelectedListener!!.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }
}

