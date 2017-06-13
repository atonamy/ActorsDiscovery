package engineertest.android.touche.com.paytouch.data.view_model

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import engineertest.android.touche.com.paytouch.R
import com.facebook.drawee.view.SimpleDraweeView
import android.databinding.BindingAdapter
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import engineertest.android.touche.com.paytouch.BR
import engineertest.android.touche.com.paytouch.data.model.Actor
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.imagepipeline.request.ImageRequest
import engineertest.android.touche.com.paytouch.ui.adapters.FilmsAdapter


/**
 * Created by archie on 21/5/17.
 */
class ActorViewModel(private val context: Context): BaseObservable() {

    private var model: Actor = Actor()
    var onClickListener: () -> Unit = {}

    @get:Bindable
    var popularityBackground: Drawable = ContextCompat.getDrawable(context, R.drawable.popularity)
        set(value) {
            field = value
            notifyPropertyChanged(BR.popularityBackground)
        }

    @get:Bindable
    var actorPicUrl: String = ""
        get() = model.profilePath
        set(value) {
            field = value
            notifyPropertyChanged(BR.actorPicUrl)
        }

    @get:Bindable
    var firstName: String = ""
        get() = fullName.first
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
        }

    @get:Bindable
    var lastName: String = ""
        get() = fullName.second
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
        }

    @get:Bindable
    var location: String = ""
        get() = model.location
        set(value) {
            field = value
            notifyPropertyChanged(BR.location)
        }

    @get:Bindable
    var description: String = ""
        get() = model.description
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

    @get:Bindable
    var popularity: String = ""
        get() = String.format("%.2f", model.popularity)
        set(value) {
            field = value
            notifyPropertyChanged(BR.popularity)
        }

    @get:Bindable
    var viewBackground: Int = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        set(value) {
            field = value
            notifyPropertyChanged(BR.viewBackground)
        }

    @get:Bindable
    var viewFontColor: Int = ContextCompat.getColor(context, R.color.colorPrimary)
        set(value) {
            field = value
            notifyPropertyChanged(BR.viewFontColor)
        }

    @get:Bindable
    var icon: Drawable = ContextCompat.getDrawable(context, R.drawable.pin_white)
        set(value) {
            field = value
            notifyPropertyChanged(BR.icon)
        }

    @get:Bindable
    val onClick: View.OnClickListener
        get() = View.OnClickListener {
            onClickListener()
        }

    fun setModel(model: Actor) {
        this.model = model
    }

    private val fullName: Pair<String, String>
    get() {
        var first = ""
        var last = ""
        val names = model.name.split(" ")
        if(names.size > 1)
        {
            first = names[0]
            last = names[1]
            if(names.size > 2)
                for (i in 2..names.size-1)
                    last += " " + names[i]
        }
        else if(names.size > 0)
            first = names[0]

        return Pair(first, last)
    }

    @get:Bindable
    var adapter: FilmsAdapter? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.adapter)
        }

    @get:Bindable
    var layoutManager: RecyclerView.LayoutManager? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.layoutManager)
        }

}

@BindingAdapter("imageUrl")
fun loadImage(view: SimpleDraweeView, imageUrl: String) {
    view.setImageURI(imageUrl)
}

