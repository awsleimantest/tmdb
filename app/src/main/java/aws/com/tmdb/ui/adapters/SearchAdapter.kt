package aws.com.tmdb.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aws.com.themoviedb.app.db.pojo.Movie
import aws.com.themoviedb.app.ui.base.holders.MovieViewHolder
import aws.com.tmdb.R
import aws.com.tmdb.ui.base.OnItemClickListener

class SearchAdapter(private val mCLickListener: OnItemClickListener): RecyclerView.Adapter<MovieViewHolder>() {
    private var mData: List<Movie> = emptyList()

    fun setData(data: List<Movie>) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_movie_holder_layout, parent, false), mCLickListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(mData[position], true)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return mData[position].getType()
    }
}