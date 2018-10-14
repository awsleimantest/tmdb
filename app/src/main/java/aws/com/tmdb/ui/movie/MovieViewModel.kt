package aws.com.tmdb.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieViewModel @Inject constructor(var mMovieDetailsDataProvider: MovieModel) : ViewModel() {

    fun loadData(id: Int) {
        mMovieDetailsDataProvider.getDetailedMovie(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun getMutableLiveData(id: Int): LiveData<Movie> {
        return mMovieDetailsDataProvider.getMoviesFomDB(id)
    }
}
