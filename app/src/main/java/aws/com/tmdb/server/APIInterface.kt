package aws.com.themoviedb.app.Server

import aws.com.themoviedb.app.Server.response.MoviesResponse
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIInterface {

    @GET("discover/movie")
    fun getDiscover(@Query("page") page: Int): Observable<MoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") movieId: Int): Observable<Movie>

    @GET("search/movie")
    fun search(@Query("query") query: String): Observable<MoviesResponse>
}
