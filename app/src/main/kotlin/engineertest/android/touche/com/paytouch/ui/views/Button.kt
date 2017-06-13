package engineertest.android.touche.com.paytouch.ui.views

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet

/**
 * Created by archie on 21/5/17.
 */
class Button @JvmOverloads constructor(context: Context,
                                      attrs: AttributeSet? = null,
                                      defStyleAttr: Int = 0): AppCompatButton(context, attrs, defStyleAttr) {
  init{
      if(attrs != null)
          setFont(attrs)
  }
}

