package engineertest.android.touche.com.paytouch.ui.adapters

import android.view.LayoutInflater
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import engineertest.android.touche.com.paytouch.R
import engineertest.android.touche.com.paytouch.data.model.Actor
import engineertest.android.touche.com.paytouch.data.view_model.ActorViewModel
import engineertest.android.touche.com.paytouch.databinding.ItemActorBinding
import io.realm.RealmList

/**
 * Created by archie on 22/5/17.
 */
class ActorsAdapter(private val context: Context,
                    private val actors: MutableList<Actor> = RealmList<Actor>(),
                    private val inflater: LayoutInflater = LayoutInflater.from(context)):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ActorViewHolder(val binding: ItemActorBinding,
                            var viewModel: ActorViewModel): RecyclerView.ViewHolder(binding.root)  {

        fun bindActorView() {
            binding.viewModel = viewModel
        }

    }
    class LoaderViewHolder(v: View): RecyclerView.ViewHolder(v){}


    private val typeItem: Int = 1
    private val typeFooter: Int = 2

    var footerView: Int? = null
    set(value) {
        val new_footer = if (field == null) true else false
        field = value
        val position = actors.size
        if (new_footer && field != null)
            notifyItemInserted(position)
        else if (field == null && !new_footer)
            notifyItemRemoved(position)
        else if (!new_footer && field != null)
            notifyItemChanged(position)
    }

    val isEmpty: Boolean
    get() = actors.size == 0

    var actorClickListener: (actor: Actor) -> Unit = { }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when {
            holder is ActorViewHolder -> {
                val actor = getActor(position)
                if(actor != null) {
                    bindActor(actor, holder.viewModel, position % 2 == 0)
                    holder.bindActorView()
                }
            }
        }
    }

    fun addActors(actors: List<Actor>) {
        if(actors.size == 0)
            return
        val position = this.actors.size
        this.actors.addAll(position, actors)
        notifyItemRangeInserted(position, actors.size)
    }

    fun clearActors() {
        footerView = null
        this.actors.clear()
        notifyDataSetChanged()
    }

    private fun bindActor(actor: Actor, viewModel: ActorViewModel,
                          even: Boolean) {

        viewModel.setModel(actor)
        viewModel.popularityBackground = if(even) ContextCompat.getDrawable(context, R.drawable.popularity)
            else ContextCompat.getDrawable(context, R.drawable.popularity_)
        viewModel.icon = if(even) ContextCompat.getDrawable(context, R.drawable.pin_white)
            else ContextCompat.getDrawable(context, R.drawable.pin_black)
        viewModel.viewBackground = if(even) ContextCompat.getColor(context, R.color.colorPrimaryDark)
            else ContextCompat.getColor(context, R.color.colorPrimary)
        viewModel.viewFontColor = if(even) ContextCompat.getColor(context, R.color.colorPrimary)
            else ContextCompat.getColor(context, R.color.colorPrimaryDark)
        viewModel.onClickListener =  {
            actorClickListener(actor)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        when(viewType) {
            typeItem -> return ActorViewHolder(ItemActorBinding.inflate(inflater, parent, false),
                    ActorViewModel(context))
            typeFooter -> {
                return LoaderViewHolder(inflater.inflate(footerView!!, parent, false))
            }
            else -> return null
        }
    }

    override fun getItemCount(): Int {
        return if (footerView == null) actors.size else actors.size + 1;
    }

    override fun getItemViewType(position: Int): Int {
        if (position == actors.size && footerView != null)
            return typeFooter;

        return typeItem;
    }

    fun getActor(position: Int): Actor? {
        if(position >= 0 && position < actors.size)
            return actors.get(position);
        return null;
    }
}