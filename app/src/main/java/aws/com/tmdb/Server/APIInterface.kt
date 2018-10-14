package aws.com.themoviedb.app.Server

import aws.com.themoviedb.app.Server.response.DiscoverMoviesResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface APIInterface {
    @GET("discover/movie")
    fun getDiscover(): Observable<DiscoverMoviesResponse>
}
