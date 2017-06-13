package engineertest.android.touche.com.paytouch.data.view_model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView
import engineertest.android.touche.com.paytouch.BR
import engineertest.android.touche.com.paytouch.data.model.Film
import java.text.SimpleDateFormat

/**
 * Created by archie on 22/5/17.
 */
class FilmViewModel: BaseObservable() {

    private  var model: Film = Film()
    private val dateFormatter =  SimpleDateFormat("MM/dd/yyyy")

    @get:Bindable
    var moviePicUrl: String = ""
        get() = model.posterPath
        set(value) {
            field = value
            notifyPropertyChanged(BR.moviePicUrl)
        }

    @get:Bindable
    var title: String = ""
        get() = model.title
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    var date: String = ""
        get() = if(model.releaseDate != null) dateFormatter.format(model.releaseDate)
                    else ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.date)
        }

    @get:Bindable
    var popularity: String = ""
        get() = String.format("%.2f", model.popularity)
        set(value) {
            field = value
            notifyPropertyChanged(BR.popularity)
        }

    @get:Bindable
    var votes: String = ""
        get() = model.voteCount.toString()
        set(value) {
            field = value
            notifyPropertyChanged(BR.votes)
        }

    fun setModel(model: Film){
        this.model = model
    }
}

@BindingAdapter("imageUrl")
fun setImage(view: SimpleDraweeView, imageUrl: String) {
    view.setImageURI(imageUrl)
}