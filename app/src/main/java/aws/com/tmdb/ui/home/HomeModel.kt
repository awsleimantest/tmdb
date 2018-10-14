package aws.com.themoviedb.app.ui.home

import androidx.lifecycle.LiveData
import aws.com.themoviedb.app.Server.APIInterface
import aws.com.themoviedb.app.db.dao.MovieDao
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class HomeModel @Inject constructor(var mAPIInterface: APIInterface, var mMovieDao: MovieDao) {

    internal fun getDiscoverMovies(): Observable<List<Movie>> {
        return getMoviesFromAPI()
    }
    private fun getMoviesFromAPI(): Observable<List<Movie>> {
        val observable =  mAPIInterface.getDiscover().doOnNext { discoverMoviesResponse ->
            if(discoverMoviesResponse.results != null) {
                mMovieDao.insertAll(discoverMoviesResponse.results!!)
            }
        }
        return observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap<List<Movie>> { discoverMoviesResponse -> Observable.just(discoverMoviesResponse.results) }
    }
    fun getMoviesFomDB(): LiveData<List<Movie>> {
        return mMovieDao.movies
    }
}