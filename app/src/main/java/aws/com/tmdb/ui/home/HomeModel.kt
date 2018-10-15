package aws.com.themoviedb.app.ui.home

import androidx.lifecycle.LiveData
import aws.com.themoviedb.app.Server.APIInterface
import aws.com.themoviedb.app.Server.response.MoviesResponse
import aws.com.themoviedb.app.db.dao.MovieDao
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeModel @Inject constructor(var mAPIInterface: APIInterface, var mMovieDao: MovieDao) {

    internal fun getDiscoverMovies(page: Int): Observable<MoviesResponse> {
        return getMoviesFromAPI(page)
    }
    private fun getMoviesFromAPI(page: Int): Observable<MoviesResponse> {
        val observable =  mAPIInterface.getDiscover(page).doOnNext { discoverMoviesResponse ->
            if(discoverMoviesResponse.results != null && discoverMoviesResponse.results!!.isNotEmpty()) {
                if(page == 1){
                    mMovieDao.deleteAll()
                    mMovieDao.insertAll(discoverMoviesResponse.results!!)
                }
                else{
                    insertOrUpdateList(discoverMoviesResponse.results!!)
                }
            }
        }
        return observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }

    fun getMoviesFomDB(): LiveData<List<Movie>> {
        return mMovieDao.movies
    }

    fun insertOrUpdateList(movies: List<Movie>){
        movies.forEach {movie ->
            if(mMovieDao.loadMovieId(movie.id) == null){
                mMovieDao.insert(movie)
            }
            else{
                mMovieDao.update(movie)
            }
        }
    }

}