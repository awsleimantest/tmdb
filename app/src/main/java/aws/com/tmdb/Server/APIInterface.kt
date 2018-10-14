package aws.com.themoviedb.app.Server

import aws.com.themoviedb.app.Server.response.DiscoverMoviesResponse
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface APIInterface {

    @GET("discover/movie")
    fun getDiscover(): Observable<DiscoverMoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") movieId: Int): Observable<Movie>
}
