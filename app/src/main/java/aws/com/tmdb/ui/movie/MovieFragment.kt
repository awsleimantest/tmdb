package aws.com.tmdb.ui.movie

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import aws.com.themoviedb.app.TMDBApplication
import aws.com.themoviedb.app.db.pojo.Movie
import aws.com.themoviedb.app.utils.getImagePath
import aws.com.themoviedb.app.utils.loadImage
import aws.com.tmdb.R
import aws.com.tmdb.ui.MainActivity
import aws.com.tmdb.utils.OPEN_MOVIE_SOURCE_SEARCH
import aws.com.tmdb.utils.formatNumber
import aws.com.tmdb.utils.getBoldSpannable
import kotlinx.android.synthetic.main.movie_fragment.*
import javax.inject.Inject

class MovieFragment : Fragment() {

    companion object {
        const val SOURCE_KEY = "source_key"
        const val MOVIE_ID_KEY = "movie_id_key"
        fun newInstance(movieId: Int, source: String): MovieFragment {
            val fragment = MovieFragment()
            val bundle = Bundle()
            bundle.putInt(MOVIE_ID_KEY, movieId)
            bundle.putString(SOURCE_KEY, source)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var mViewModel: MovieViewModel

    private var movieId: Int = 0
    private lateinit var mSource: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TMDBApplication).getAppComponent().inject(this)
        movieId = savedInstanceState?.getInt(MOVIE_ID_KEY) ?: arguments!!.getInt(MOVIE_ID_KEY)
        mSource = savedInstanceState?.getString(SOURCE_KEY) ?: arguments!!.getString(SOURCE_KEY)!!
        mViewModel.loadData(movieId, disposable())
    }

    private fun disposable(): Boolean{
        return listOf(
                OPEN_MOVIE_SOURCE_SEARCH
                // Here should be added all the sources that might affect our DB
        ).contains(mSource)
    }

    override fun onDestroy() {
        mViewModel.dispose()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MOVIE_ID_KEY, movieId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mViewModel.getMutableLiveData(movieId).observe(this, Observer{ movie ->
            if(movie != null){
                updateView(movie!!)
            }
            else{
                showLoading()
            }
        })
    }

    private fun updateView(movie: Movie) {
        showLoading(false)
        loadImage(iv_image, getImagePath(movie.backdropPath, "w780"), -1)
        val displayTitle = movie.getDisplayTitle()
        if (displayTitle.isEmpty()) {
            collapsing_toolbar_layout.isTitleEnabled = false
        } else {
            collapsing_toolbar_layout.title = displayTitle
            collapsing_toolbar_layout.isTitleEnabled = true
        }

        if (movie.popularity > 0) {
            val popularityValue = movie.popularity.toInt().toString()
            val textValue = getString(R.string.Popularity_s, popularityValue)
            popularity.visibility = View.VISIBLE
            popularity.text = getBoldSpannable(textValue, 0, textValue.length - popularityValue.length -1)
        } else {
            popularity.visibility = View.GONE
        }

        val genres = movie.getGenresAsString()
        if (genres.isNotEmpty()) {
            val textValue = getString(R.string.Genre_s, genres)
            genre.text = getBoldSpannable(textValue, 0, textValue.length - genres.length -1)
            genre.visibility = View.VISIBLE
        } else {
            genre.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(movie.overview)) {
            val textValue = getString(R.string.Overview_s, movie.overview)
            description.text = getBoldSpannable(textValue, 0, textValue.length - movie.overview!!.length -1)
            description.visibility = View.VISIBLE
        } else {
            description.visibility = View.GONE
        }

        if (movie.runtime > 0) {
            val textValue = getString(R.string.duration_d, movie.runtime)
            runtime.text = getBoldSpannable(textValue, 0, textValue.indexOf(':'))
            runtime.visibility = View.VISIBLE
        } else {
            runtime.visibility = View.GONE
        }

        if (movie.revenue > 0) {
            val revenueString = "$".plus(formatNumber(movie.revenue))
            val textValue = getString(R.string.Revenue_s, revenueString)
            revenue.text = getBoldSpannable(textValue, 0, textValue.length - revenueString.length -1)
            revenue.visibility = View.VISIBLE
        } else {
            revenue.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(movie.originalLanguage)) {
            val textValue = getString(R.string.Language_s, movie.originalLanguage)
            language.text = getBoldSpannable(textValue, 0, textValue.length - movie.originalLanguage!!.length -1)
            language.visibility = View.VISIBLE
        } else {
            language.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(movie.homepage)) {
            link.text = movie.homepage
            link.visibility = View.VISIBLE
        } else {
            link.visibility = View.GONE
        }
    }

    private fun showLoading(show: Boolean = false){
        if(show){
            progressBar.visibility = View.VISIBLE
        }
        else{
            progressBar.visibility = View.GONE
        }
    }

}
