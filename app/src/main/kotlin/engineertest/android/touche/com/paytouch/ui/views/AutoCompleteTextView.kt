package engineertest.android.touche.com.paytouch.ui.views

import android.content.Context
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.util.AttributeSet
import android.graphics.Typeface



/**
 * Created by archie on 21/5/17.
 */

class AutoCompleteTextView @JvmOverloads constructor(context: Context,
                                       attrs: AttributeSet? = null,
                                       defStyleAttr: Int = 0) :
        AppCompatAutoCompleteTextView(context, attrs, defStyleAttr) {

    init{
        if(attrs != null)
            setFont(attrs)
    }

}


