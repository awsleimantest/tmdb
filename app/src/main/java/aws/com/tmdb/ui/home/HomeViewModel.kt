package aws.com.themoviedb.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class HomeViewModel @Inject constructor(var mDiscoverModel: HomeModel): ViewModel() {
    internal var mMutableError = MutableLiveData<String>()
    var mIsLoading: Boolean = false
    internal fun refreshData() {
        loadData()
    }

    fun loadData() {
        mIsLoading = true
        mDiscoverModel.getDiscoverMovies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<List<Movie>>() {
                    override fun onNext(baseMovies: List<Movie>) {
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

    fun getLiveData(): LiveData<List<Movie>> {
        return mDiscoverModel.getMoviesFomDB()
    }

    fun getMutableError(): MutableLiveData<String> {
        return mMutableError
    }
}