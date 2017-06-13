package engineertest.android.touche.com.paytouch.ui.views

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatRadioButton
import android.util.AttributeSet

/**
 * Created by archie on 21/5/17.
 */
class RadioButton: AppCompatRadioButton {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setFont(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setFont(attrs)
    }

    constructor(context: Context) : super(context) {
    }
}



