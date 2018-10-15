package aws.com.tmdb.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aws.com.themoviedb.app.Server.response.MoviesResponse
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(var mSearchModel: SearchModel) : ViewModel() {

    var mMutableError = MutableLiveData<String>()
    var mMutableLiveData = MutableLiveData<List<Movie>>()

    var mIsLoading: Boolean = false

    fun loadData(query: String){
        mSearchModel.search(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<MoviesResponse>() {
                    override fun onNext(response: MoviesResponse) {
                        mMutableLiveData.postValue(response.results)
                        mIsLoading = false
                    }

                    override fun onError(e: Throwable) {
                        mIsLoading = false
                        mMutableError.postValue(e.message)
                    }

                    override fun onComplete() {
                        mIsLoading = false
                    }
                })
    }

    fun getMutableLiveData(): LiveData<List<Movie>> {
        return mMutableLiveData
    }

    fun getMutableError(): MutableLiveData<String> {
        return mMutableError
    }
}
