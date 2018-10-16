package aws.com.tmdb.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aws.com.themoviedb.app.TMDBApplication
import aws.com.themoviedb.app.db.pojo.Movie
import aws.com.themoviedb.app.ui.home.HomeViewModel
import aws.com.tmdb.R
import aws.com.tmdb.ui.MainActivity
import aws.com.tmdb.ui.adapters.MainAdapter
import aws.com.tmdb.ui.adapters.decorations.HomeItemDecoration
import aws.com.tmdb.ui.base.Displayable
import aws.com.tmdb.ui.base.Loading
import aws.com.tmdb.ui.base.OnItemClickListener
import aws.com.tmdb.ui.movie.MovieFragment
import aws.com.tmdb.ui.search.SearchFragment
import aws.com.tmdb.utils.OPEN_MOVIE_SOURCE_HOME
import aws.com.tmdb.utils.TYPE_MOVIE
import aws.com.tmdb.utils.isConnected
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject


class HomeFragment : Fragment() {

    companion object {
        const val SCROLL_POSITION_KEY = "scroll_position_key"
        fun newInstance() = HomeFragment()
    }

    @Inject
    lateinit var mViewModel: HomeViewModel

    private lateinit var mAdapter: MainAdapter

    private val handler = Handler()
    private val runnable = java.lang.Runnable {
        checkIfEnteredThreshold()
    }

    private var mRecyclerViewOnScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        @SuppressLint("CheckResult")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 300)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity?.application as TMDBApplication).getAppComponent().inject(this)
        var initialPosition: Int? = savedInstanceState?.getInt(SCROLL_POSITION_KEY, -1)
        mViewModel.getLiveData().observe(this, Observer<List<Movie>> { movies ->
            showData(ArrayList(movies))
            if(initialPosition != null && initialPosition!! > 0){
                recycler_view.scrollToPosition(initialPosition!!)
                initialPosition = -1
            }
        })
        mViewModel.getMutableError().observe(this, Observer<String> { s ->
            showLoading(false)
            mAdapter.removLoading()
            if (mAdapter.itemCount == 0) {
                showError(getString(R.string.server_and_connection_error_message))
            }
        })
        mAdapter = MainAdapter(object : OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                (activity as MainActivity).push(MovieFragment.newInstance(movie.id, OPEN_MOVIE_SOURCE_HOME))
            }
        })
        mViewModel.loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolbar)
        val columns = resources.getInteger(R.integer.span_count)
        val layoutManager = GridLayoutManager(context, columns, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(pos: Int): Int {
                    return if (mAdapter.getItemViewType(pos) == TYPE_MOVIE) 1 else columns
                }
            }
        }
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(HomeItemDecoration(resources.getDimension(R.dimen.small_margin).toInt()))
        recycler_view.addOnScrollListener(mRecyclerViewOnScrollListener)
        val supportActionBar = (activity as MainActivity).supportActionBar
        supportActionBar?.title = getString(R.string.app_name)
        sw_refresh.setOnRefreshListener { mViewModel.refreshData() }
        showLoading(mAdapter.itemCount == 0 && mViewModel.mIsLoading)
    }

    override fun onDestroyView() {
        recycler_view?.removeOnScrollListener(mRecyclerViewOnScrollListener)
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(recycler_view != null && recycler_view.layoutManager != null){
            outState.putInt(SCROLL_POSITION_KEY, (recycler_view.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null && item.itemId == R.id.action_search){
            if(activity!= null) {
                if (isConnected(activity!!)) {
                    (activity as MainActivity).push(SearchFragment.newInstance(), false)
                }
                else{
                    Toast.makeText(activity!!, R.string.this_feature_needs_connection,Toast.LENGTH_LONG).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkIfEnteredThreshold() {

        if(recycler_view != null && recycler_view.layoutManager != null) {
            val visibleThreshold = 5
            val visibleItemCount = recycler_view.layoutManager!!.childCount
            val totalItemCount = recycler_view.layoutManager!!.itemCount
            val firstVisibleItemPosition = (recycler_view.layoutManager!! as GridLayoutManager).findFirstVisibleItemPosition()

            if (visibleItemCount + firstVisibleItemPosition + visibleThreshold >= totalItemCount && firstVisibleItemPosition > 0) {
                if(mViewModel.loadMoreDataIfPossible()){
                    mAdapter.addLoading()
                }
            }
        }
    }

    private fun showData(data: ArrayList<Displayable>) {
        if(mViewModel.canLoadMore()){
            data.add(Loading())
        }
        mAdapter.setData(data)
        showLoading(false)
        showError(null)
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