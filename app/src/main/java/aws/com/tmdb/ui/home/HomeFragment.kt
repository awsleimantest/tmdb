package aws.com.themoviedb.app.ui.home

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
import aws.com.tmdb.MainActivity
import aws.com.tmdb.R
import aws.com.tmdb.ui.adapters.MainAdapter
import aws.com.tmdb.ui.adapters.decorations.HomeItemDecoration
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject

class HomeFragment: Fragment() {


    companion object {
        fun newInstance() = HomeFragment()
    }

    @Inject
    lateinit var mViewModel: HomeViewModel

    private lateinit var mAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as TMDBApplication).getAppComponent().inject(this)
        mViewModel.loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = MainAdapter(object : MainAdapter.OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                //TODO go to movie fragment
            }
        })
        recycler_view.layoutManager = GridLayoutManager(context,resources.getInteger(R.integer.span_count), RecyclerView.VERTICAL, false)
        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(HomeItemDecoration(resources.getDimension(R.dimen.small_margin).toInt()))

        (activity as MainActivity).setSupportActionBar(toolbar)
        val supportActionBar = (activity as MainActivity).supportActionBar
        supportActionBar?.title = getString(R.string.app_name)
        sw_refresh.setOnRefreshListener { mViewModel.refreshData() }
        showLoading(true)
        mViewModel.getMutableLiveData().observe(this, Observer<List<Movie>> { movies -> showData(movies) })
        mViewModel.getMutableError().observe(this, Observer<String> { s ->
            if (mAdapter.itemCount == 0) {
                showError(s)
            }
        })
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