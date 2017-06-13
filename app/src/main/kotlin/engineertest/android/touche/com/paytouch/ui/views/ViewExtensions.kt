package engineertest.android.touche.com.paytouch.ui.views

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.util.AttributeSet
import android.view.View

/**
 * Created by archie on 24/5/17.
 */

val attributeKey = "http://schemas.android.com/apk/res/android"
val attributeValue = "textStyle"

val normal = "fonts/myriad_pro_regular.ttf"
val italic = "fonts/myriad-pro-it.ttf"
val bold = "fonts/myriad-pro-bold.ttf"

internal fun selectFont(context: Context, attrs: AttributeSet, typeface: (typeface: Typeface) -> Unit) {
    val textStyle = attrs.getAttributeIntValue(attributeKey, attributeValue, Typeface.NORMAL);
    when (textStyle) {
        Typeface.BOLD -> typeface(Typeface.createFromAsset(context.assets, bold))
        Typeface.ITALIC -> typeface(Typeface.createFromAsset(context.assets, italic))
        else -> typeface(Typeface.createFromAsset(context.assets, normal))
    }
}

fun TextView.setFont(attrs: AttributeSet) {
    selectFont(context, attrs) {
        typeface = it
    }
}

fun RadioButton.setFont(attrs: AttributeSet) {
    selectFont(context, attrs) {
        typeface = it
    }
}

fun Button.setFont(attrs: AttributeSet) {
    selectFont(context, attrs) {
        typeface = it
    }
}

fun AppCompatAutoCompleteTextView.setFont(attrs: AttributeSet) {
    selectFont(context, attrs) {
        typeface = it
    }
}
