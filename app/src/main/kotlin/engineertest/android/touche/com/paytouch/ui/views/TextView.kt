package engineertest.android.touche.com.paytouch.ui.views

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View

/**
 * Created by archie on 21/5/17.
 */
class TextView @JvmOverloads constructor(context: Context,
                                         attrs: AttributeSet? = null,
                                         defStyleAttr: Int = 0): AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        if(attrs != null)
            setFont(attrs)
    }


}

