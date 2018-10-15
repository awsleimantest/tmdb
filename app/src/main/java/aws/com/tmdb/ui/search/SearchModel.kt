package aws.com.tmdb.ui.search

import aws.com.themoviedb.app.Server.APIInterface
import aws.com.themoviedb.app.Server.response.MoviesResponse
import io.reactivex.Observable
import javax.inject.Inject

class SearchModel @Inject constructor(var mAPIInterface: APIInterface) {
    fun search(query: String): Observable<MoviesResponse> {
        return mAPIInterface.search(query)
    }
}