package aws.com.tmdb.ui.movie

import androidx.lifecycle.LiveData
import aws.com.themoviedb.app.Server.APIInterface
import aws.com.themoviedb.app.db.dao.MovieDao
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.Observable
import javax.inject.Inject

class MovieModel @Inject constructor(var mApiInterface: APIInterface, var mMovieDao: MovieDao) {

    fun getDetailedMovie(id: Int): Observable<Movie> {
        return getMoviesFromAPI(id)
    }

    private fun getMoviesFromAPI(id: Int): Observable<Movie> {
        return mApiInterface.getMovieDetails(id).doOnNext { detailedMovie -> mMovieDao.insert(detailedMovie) }
    }

    fun getMoviesFomDB(id: Int): LiveData<Movie> {
        return mMovieDao.getMovie(id)
    }
}