package aws.com.tmdb.ui.movie

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import aws.com.themoviedb.app.Server.APIInterface
import aws.com.themoviedb.app.db.dao.MovieDao
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieModel @Inject constructor(var mApiInterface: APIInterface, var mMovieDao: MovieDao) {

    fun getDetailedMovie(id: Int, dispose: Boolean): Observable<Movie> {
        return getMoviesFromAPI(id, dispose)
    }

    private fun getMoviesFromAPI(id: Int, dispose: Boolean): Observable<Movie> {
        return mApiInterface.getMovieDetails(id).doOnNext { movie ->
            val previousId = mMovieDao.loadMovieId(movie.id)
            if (previousId == null) {
                movie.dispose = dispose
                mMovieDao.insert(movie)
            } else {
                mMovieDao.update(movie)
            }
        }
    }

    fun getMoviesFomDB(id: Int): LiveData<Movie?> {

        return mMovieDao.getMovie(id)
    }

    @SuppressLint("CheckResult")
    fun dispose(){
        Observable.fromCallable(mMovieDao::removeDisposables)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {  }
    }
}