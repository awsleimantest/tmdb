package aws.com.themoviedb.app.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aws.com.themoviedb.app.TMDBApplication
import aws.com.themoviedb.app.db.pojo.Movie
import aws.com.tmdb.MainActivity
import aws.com.tmdb.R
import aws.com.tmdb.ui.adapters.MainAdapter
import aws.com.tmdb.ui.adapters.decorations.HomeItemDecoration
import aws.com.tmdb.ui.movie.MovieFragment
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject

class HomeFragment : Fragment() {


    companion object {
        const val RECYCLERVIEW_STATE_KEY = "recyclerview_state_key"
        fun newInstance() = HomeFragment()
    }

    @Inject
    lateinit var mViewModel: HomeViewModel

    private lateinit var mAdapter: MainAdapter
    private lateinit var mLayoutManager : RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("awslog", "$TAG onCreate() called ")
        (activity!!.application as TMDBApplication).getAppComponent().inject(this)
        mViewModel.loadData()
        mAdapter = MainAdapter(object : MainAdapter.OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                (activity as MainActivity).push(MovieFragment.newInstance(movie.id))
            }
        })
        mLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.span_count), RecyclerView.VERTICAL, false)
    }
private val TAG = "HomeFragment.kt: "
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.v("awslog", "$TAG onCreateView() called ")
        val result = inflater.inflate(R.layout.home_fragment, container, false)
        if(savedInstanceState == null) {
            mViewModel.getMutableLiveData().observe(this, Observer<List<Movie>> { movies -> showData(movies) })
            mViewModel.getMutableError().observe(this, Observer<String> { s ->
                if (mAdapter.itemCount == 0) {
                    showError(s)
                }
            })
        }
        return result
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("awslog", "$TAG onViewCreated() called ")
        recycler_view.layoutManager =  mLayoutManager
        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(HomeItemDecoration(resources.getDimension(R.dimen.small_margin).toInt()))
        (activity as MainActivity).setSupportActionBar(toolbar)
        val supportActionBar = (activity as MainActivity).supportActionBar
        supportActionBar?.title = getString(R.string.app_name)
        sw_refresh.setOnRefreshListener { mViewModel.refreshData() }
        showLoading(true)
    }

    override fun onDestroyView() {
        recycler_view.layoutManager = null
        recycler_view.adapter = null
        super.onDestroyView()
    }

    private fun showData(movies: List<Movie>) {
        Log.v("awslog", "$TAG showData() called ")
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