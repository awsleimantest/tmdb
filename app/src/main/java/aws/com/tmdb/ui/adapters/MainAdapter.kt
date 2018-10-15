package aws.com.tmdb.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aws.com.themoviedb.app.ui.base.holders.MovieViewHolder
import aws.com.tmdb.R
import aws.com.tmdb.ui.base.BaseViewHolder
import aws.com.tmdb.ui.base.Displayable
import aws.com.tmdb.ui.base.Loading
import aws.com.tmdb.ui.base.OnItemClickListener
import aws.com.tmdb.ui.holders.LoadingViewHolder
import aws.com.tmdb.utils.TYPE_MOVIE
import java.util.*

class MainAdapter(private val mCLickListener: OnItemClickListener): RecyclerView.Adapter<BaseViewHolder>() {
    private var mData: List<Displayable> = emptyList()

    fun setData(data: List<Displayable>) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        return if(viewType == TYPE_MOVIE) {
            MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_holder_layout, parent, false), mCLickListener)
        }
        else{
            LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.loading_holder_layout, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return mData[position].getType()
    }

    fun addLoading(){
        if(mData.isNotEmpty() && !hasLoading()) {
            (mData as ArrayList).add(Loading())
            notifyItemInserted(mData.size - 1)
        }
    }
    fun removLoading(){
        if(mData.isNotEmpty() && hasLoading()) {
            (mData as ArrayList).remove(mData[mData.size - 1])
            notifyItemRemoved(mData.size)
        }
    }

    private fun hasLoading(): Boolean{
        if(mData[mData.size-1] is Loading){
            return true
        }
        return false
    }
}