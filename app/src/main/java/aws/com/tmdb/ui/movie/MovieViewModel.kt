package aws.com.tmdb.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieViewModel @Inject constructor(var mMovieDetailsDataProvider: MovieModel) : ViewModel() {

    fun loadData(id: Int, dispose: Boolean) {
        mMovieDetailsDataProvider.getDetailedMovie(id, dispose)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Movie>() {
                    override fun onNext(movie: Movie) {
                        //Do nothing here
                    }

                    override fun onError(e: Throwable) {
                        //Do nothing here
                    }

                    override fun onComplete() {
                        //Do nothing here
                    }
                })
    }

    fun getMutableLiveData(id: Int): LiveData<Movie?> {
        return mMovieDetailsDataProvider.getMoviesFomDB(id)
    }

    fun dispose(){
        mMovieDetailsDataProvider.dispose()
    }
}
