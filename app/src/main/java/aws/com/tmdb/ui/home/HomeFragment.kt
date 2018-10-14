package aws.com.tmdb.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aws.com.themoviedb.app.TMDBApplication
import aws.com.themoviedb.app.db.pojo.Movie
import aws.com.themoviedb.app.ui.home.HomeViewModel
import aws.com.tmdb.MainActivity
import aws.com.tmdb.R
import aws.com.tmdb.ui.adapters.MainAdapter
import aws.com.tmdb.ui.adapters.decorations.HomeItemDecoration
import aws.com.tmdb.ui.movie.MovieFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as TMDBApplication).getAppComponent().inject(this)
        var initialPosition: Int? = savedInstanceState?.getInt(SCROLL_POSITION_KEY, -1)
        mViewModel.getLiveData().observe(this, Observer<List<Movie>> { movies ->
            showData(movies)
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
        mAdapter = MainAdapter(object : MainAdapter.OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                (activity as MainActivity).push(MovieFragment.newInstance(movie.id))
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
        val layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.span_count), RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(HomeItemDecoration(resources.getDimension(R.dimen.small_margin).toInt()))
        (activity as MainActivity).setSupportActionBar(toolbar)
        val supportActionBar = (activity as MainActivity).supportActionBar
        supportActionBar?.title = getString(R.string.app_name)
        sw_refresh.setOnRefreshListener { mViewModel.refreshData() }
        showLoading(mAdapter.itemCount == 0 && mViewModel.mIsLoading)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if(recycler_view != null && recycler_view.layoutManager != null){
            outState.putInt(SCROLL_POSITION_KEY, (recycler_view.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        recycler_view.adapter = null
        super.onDestroyView()
    }

    private fun showData(movies: List<Movie>) {
        mAdapter.setData(movies)
        showLoading(false)
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