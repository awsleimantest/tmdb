package aws.com.tmdb.ui.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aws.com.themoviedb.app.Server.response.MoviesResponse
import aws.com.themoviedb.app.db.pojo.Movie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(var mSearchModel: SearchModel) : ViewModel() {

    private val TAG = "SearchViewModel.kt: "
    var mMutableError = MutableLiveData<String>()
    var mMutableLiveData = MutableLiveData<List<Movie>>()

    var mIsLoading: Boolean = false

    var dic: HashMap<String,List<Movie>?> = HashMap()
    var query = ""

    @SuppressLint("CheckResult")
    fun loadData(query: String){
        if(query.isNotEmpty()){
            val existingVal = dic.get(key = query)
            if(existingVal != null){
                Log.v("awslog", "$TAG loadData() called woooohooooo I'm saving energy, Green earth ")
                Observable.fromCallable {
                    mMutableLiveData.postValue(existingVal)
                    Any()
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {  }
                return
            }
        }

        this.query = query
        mSearchModel.search(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<MoviesResponse>() {
                    override fun onNext(response: MoviesResponse) {
                        dic[query] = response.results
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
