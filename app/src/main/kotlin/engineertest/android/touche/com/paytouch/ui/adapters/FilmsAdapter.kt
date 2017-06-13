package engineertest.android.touche.com.paytouch.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import engineertest.android.touche.com.paytouch.data.model.Film
import engineertest.android.touche.com.paytouch.data.view_model.FilmViewModel
import engineertest.android.touche.com.paytouch.databinding.ItemFilmBinding


/**
 * Created by archie on 23/5/17.
 */
class FilmsAdapter(private val context: Context,
                   private val movies: List<Film>,
                   private val inflater: LayoutInflater = LayoutInflater.from(context)):
        RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>(){

    class FilmViewHolder(val binding: ItemFilmBinding,
                          var viewModel: FilmViewModel): RecyclerView.ViewHolder(binding.root)  {

        fun bindFilmView() {
            binding.viewModel = viewModel
        }

    }

    override fun onBindViewHolder(holder: FilmViewHolder?, position: Int) {
        val film = getFilm(position)
        if(film != null) {
            holder?.viewModel?.setModel(film)
            holder?.bindFilmView()
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FilmViewHolder {
        return FilmViewHolder(ItemFilmBinding.inflate(inflater, parent, false), FilmViewModel())
    }

    fun getFilm(position: Int): Film? {
        if(position >= 0 && position < movies.size)
            return movies.get(position);
        return null;
    }
}