package aws.com.tmdb.ui.search

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aws.com.themoviedb.app.TMDBApplication
import aws.com.themoviedb.app.db.pojo.Movie
import aws.com.tmdb.R
import aws.com.tmdb.ui.MainActivity
import aws.com.tmdb.ui.adapters.SearchAdapter
import aws.com.tmdb.ui.base.OnItemClickListener
import aws.com.tmdb.ui.movie.MovieFragment
import aws.com.tmdb.utils.OPEN_MOVIE_SOURCE_SEARCH
import aws.com.tmdb.utils.TYPE_MOVIE
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject



class SearchFragment : Fragment() {

    private val TAG = "SearchFragment.kt: "
    companion object {
        const val SCROLL_POSITION_KEY = "scroll_position_key"
        fun newInstance() = SearchFragment()
    }

    @Inject
    lateinit var mViewModel: SearchViewModel

    private lateinit var mAdapter: SearchAdapter

    private var query = ""

    private val handler = Handler()
    private val runnable = java.lang.Runnable {
        Log.v("awslog", "$TAG runnable() called query : $query")
        if(query.isEmpty()){
            clearData()
        }
        else {
            mViewModel.loadData(query)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity?.application as TMDBApplication).getAppComponent().inject(this)
        var initialPosition: Int? = savedInstanceState?.getInt(SCROLL_POSITION_KEY, -1)
        mViewModel.getMutableLiveData().observe(this, Observer<List<Movie>>{ movies ->
            showData(ArrayList(movies))
            if(initialPosition != null && initialPosition!! > 0){
                recycler_view.scrollToPosition(initialPosition!!)
                initialPosition = -1
            }
        })
        mViewModel.getMutableError().observe(this, Observer<String> { s ->
            showLoading(false)
            if (mAdapter.itemCount == 0) {
                showError(s)
            }
        })
        mAdapter = SearchAdapter(object : OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                (activity as MainActivity).push(MovieFragment.newInstance(movie.id, OPEN_MOVIE_SOURCE_SEARCH))
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolbar)

        val columns = resources.getInteger(R.integer.row_span_count)
        val layoutManager = GridLayoutManager(context, columns, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(pos: Int): Int {
                    return if (mAdapter.getItemViewType(pos) == TYPE_MOVIE) 1 else columns
                }
            }
        }

        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = mAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(recycler_view != null && recycler_view.layoutManager != null){
            outState.putInt(SCROLL_POSITION_KEY, (recycler_view.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition())
        }
        super.onSaveInstanceState(outState)
    }

    private fun showData(data: ArrayList<Movie>) {
        mAdapter.setData(data)
        showLoading(false)
        showError(null)
    }

    private fun clearData() {
        mAdapter.setData(emptyList())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        searchMenuItem.expandActionView()
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                // do nothing here
                return false
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                (activity as MainActivity).onBackPressed()
                return true
            }
        })
        val searchView = searchMenuItem.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                query = newText?: ""
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 500)
                return true
            }
        })
        super.onCreateOptionsMenu(menu,inflater)
    }

    private fun showError(errorMessage: String?) {
        if (!TextUtils.isEmpty(errorMessage)) {
            tv_error.text = errorMessage
            tv_error.visibility = View.VISIBLE
        } else {
            tv_error.visibility = View.GONE
        }
    }

    private fun showLoading(visible: Boolean) {
        if (visible) {
            pb_loading?.visibility = View.VISIBLE
        } else {
            pb_loading?.visibility = View.GONE
            sw_refresh?.isRefreshing = false
        }
    }

}
