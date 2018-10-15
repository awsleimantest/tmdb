package aws.com.themoviedb.app.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aws.com.themoviedb.app.Server.response.DiscoverMoviesResponse
import aws.com.themoviedb.app.db.pojo.Movie
import aws.com.tmdb.utils.HOME_CURRENT_PAGE_INDEX
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class HomeViewModel @Inject constructor(var mDiscoverModel: HomeModel, var mPrefs: SharedPreferences): ViewModel() {
    internal var mMutableError = MutableLiveData<String>()
    var mIsLoading: Boolean = false
    private var mCurrentPage = 0
    private var mTotalPages = -1

    init {
        mCurrentPage = mPrefs.getInt(HOME_CURRENT_PAGE_INDEX, 0)
    }

    internal fun refreshData() {
        mCurrentPage = 0
        loadData(true)
    }

    fun loadMoreDataIfPossible(): Boolean{
        if (!mIsLoading && canLoadMore()) {
            mIsLoading = true
            loadData(true)
            return true
        }
        return false
    }

    fun loadData(forceLoad: Boolean = false) {
        if(!forceLoad && mCurrentPage > 0){
            // this here means that the call is originated from the view and there is no need to get data we already have
            return
        }
        mIsLoading = true
        mCurrentPage++
        mDiscoverModel.getDiscoverMovies(mCurrentPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<DiscoverMoviesResponse>() {
                    override fun onNext(response: DiscoverMoviesResponse) {
                        mPrefs.edit().putInt(HOME_CURRENT_PAGE_INDEX, mCurrentPage).apply()
                        mTotalPages = response.totalPages
                        mIsLoading = false
                    }

                    override fun onError(e: Throwable) {
                        mIsLoading = false
                        mMutableError.postValue(e.message)
                        mCurrentPage--
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

    fun canLoadMore():Boolean{
        return mCurrentPage < mTotalPages || mTotalPages == -1
    }

}