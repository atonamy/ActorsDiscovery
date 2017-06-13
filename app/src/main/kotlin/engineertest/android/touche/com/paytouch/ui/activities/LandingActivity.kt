package engineertest.android.touche.com.paytouch.ui.activities


import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.*
import engineertest.android.touche.com.paytouch.R
import engineertest.android.touche.com.paytouch.data.view_model.MenuViewModel
import engineertest.android.touche.com.paytouch.databinding.ActionSortBinding
import android.support.v7.widget.LinearLayoutManager
import engineertest.android.touche.com.paytouch.data.DataManager
import engineertest.android.touche.com.paytouch.data.model.Actor
import engineertest.android.touche.com.paytouch.data.realm.ActorsHelper
import engineertest.android.touche.com.paytouch.data.view_model.ActorsViewModel
import engineertest.android.touche.com.paytouch.databinding.ActivityLandingBinding
import engineertest.android.touche.com.paytouch.ui.adapters.ActorsAdapter
import engineertest.android.touche.com.paytouch.ui.dialogs.SearchDialog
import engineertest.android.touche.com.paytouch.ui.helpers.EndlessScrollListener
import engineertest.android.touche.com.paytouch.ui.views.Spinner
import engineertest.android.touche.com.paytouch.ui.views.showError
import engineertest.android.touche.com.paytouch.ui.views.showWarning
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout
import org.jetbrains.anko.startActivity


class LandingActivity : AppCompatActivity(), EndlessScrollListener.ScrollToBottomListener,
        WaveSwipeRefreshLayout.OnRefreshListener {


    lateinit var menuViewModel: MenuViewModel
    lateinit var viewModel: ActorsViewModel
    private lateinit var scrollListener: EndlessScrollListener
    var searchDialog: SearchDialog? = null
    private val dataManager = DataManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        initActionBar()
        initView()
    }

    fun initView() {
        initViewModel()
        initMenuModel()
        DataBindingUtil.setContentView<ActivityLandingBinding>(this, R.layout.activity_landing)
                .viewModel = viewModel
        dataManager.onError = {
                showError(getString(R.string.error_title), it)
                viewModel.loadingState = false
        }
        resetAndLoad(true)
    }

    fun initMenuModel() {
        menuViewModel = MenuViewModel(this)

        menuViewModel.sortTypeListener = {
            resetAndLoad(false)
        }

        menuViewModel.propertyChangedCallback = {
                (LandingActivity@this as AppCompatActivity).invalidateOptionsMenu()
        }

        menuViewModel.onSearchClick = {
            if(menuViewModel.searchMode)
                populateSearch()
            else
                resetAndLoad(false)
        }

        menuViewModel.onSortClick = {
            if (it != null && it is ViewGroup)
                for (i in 0..it.childCount-1) {
                    val spinner = it.getChildAt(i)
                    if (spinner is Spinner)
                        spinner.performClick()
                }
        }
    }

    fun initViewModel() {
        viewModel = ActorsViewModel()
        viewModel.adapter = ActorsAdapter(this)
        viewModel.layoutManager = LinearLayoutManager(this)
        scrollListener = EndlessScrollListener(viewModel.layoutManager as LinearLayoutManager,
                this)
        viewModel.scrollListener = scrollListener
        viewModel.onRefreshListener = this

        viewModel.adapter?.actorClickListener = {
            startActivity<ActorDetailsActivity>(Pair("actor_id", it.identifier))
        }
    }

    fun loadActors() {
            dataManager.loadNextActors(ActorsHelper.parseSortType(menuViewModel.sortType)) {
                addActors(it)
            }
    }

    fun initActionBar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.paytouch)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.sort_menu, menu)
        val menuBinding = ActionSortBinding.bind(menu.findItem(R.id.action_sort).actionView)
        menuBinding.viewModel = menuViewModel
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_search)?.setIcon(menuViewModel.searchIcon)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> menuViewModel.toggleSearch()
        }
        return super.onOptionsItemSelected(item)
    }

    fun addActors(actors: List<Actor>) {
        viewModel.adapter?.addActors(actors)
        viewModel.loadingState = false
    }

    fun reset(fully: Boolean) {
        menuViewModel.searchMode = false
        viewModel.adapter?.clearActors()
        dataManager.reset(fully)
        scrollListener.onRefresh()
        viewModel.loadingState = true
    }

    fun resetAndLoad(fully: Boolean) {
        reset(fully)
        loadActors()
    }

    fun populateSearch() {
        menuViewModel.searchMode = false
        if(searchDialog == null || !searchDialog!!.isOpen) {
            searchDialog = SearchDialog(this, dataManager)
            val dialog = searchDialog!!
            dialog.isOpen = true
            dialog.sortType = ActorsHelper.parseSortType(menuViewModel.sortType)

            dialog.onSearch = {
                reset(false)
                menuViewModel.searchMode = true
            }

            dialog.onSearchResult = {
                if (it != null && it.size > 0)
                    addActors(it)
                else {
                    resetAndLoad(false)
                    showWarning(getString(R.string.search_title), getString(R.string.search_not_found))
                }
            }
            dialog.onReady = {
                dialog.open()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dataManager.release()
    }

    override fun onScrollToBottom() {
        if(dataManager.hasNext && !menuViewModel.searchMode) {
            Handler().post {
                viewModel.adapter?.footerView = R.layout.item_footer
                loadActors()
            }
        }
    }

    override fun onRefresh() {
        resetAndLoad(true)
    }

}
