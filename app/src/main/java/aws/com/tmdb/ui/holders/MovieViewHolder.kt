package aws.com.themoviedb.app.ui.base.holders

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import aws.com.themoviedb.app.db.pojo.Movie
import aws.com.themoviedb.app.utils.getImagePath
import aws.com.themoviedb.app.utils.loadImage
import aws.com.tmdb.R
import aws.com.tmdb.ui.adapters.MainAdapter
import kotlinx.android.synthetic.main.movie_holder_layout.view.*

class MovieViewHolder(itemView: View, private var mCLickListener: MainAdapter.OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

    fun bind(movie: Movie) {
        itemView.setOnClickListener {
            mCLickListener?.onItemClick(movie)
        }

        if (!TextUtils.isEmpty(movie.posterPath)) {
            loadImage(itemView.iv_image, getImagePath(movie.posterPath!!), R.drawable.bg_light_grey)
        } else {
            loadImage(itemView.iv_image, null, R.drawable.bg_light_grey)
        }

        if (!TextUtils.isEmpty(movie.title)) {
            val releaseYear = getReleaseYear(movie.releaseDate)
            itemView.tv_title.visibility = View.VISIBLE
            itemView.tv_title.text = if (TextUtils.isEmpty(releaseYear)) {
                movie.title!!
            } else {
                movie.title!!.plus(" (").plus(releaseYear).plus(")")
            }
        } else {
            itemView.tv_title.visibility = View.GONE
        }

        if (movie.popularity > 0) {
            itemView.tv_popularity.visibility = View.VISIBLE
            itemView.tv_popularity.text = itemView.context.getString(R.string.Popularity, movie.popularity)
        } else {
            itemView.tv_popularity.visibility = View.GONE
        }
        /* No genres to display at this point so there no need to touch itemView.tv_genre cz it's GONE by default*/
    }

    private fun getReleaseYear(releaseDate: String?): String? {
        if (TextUtils.isEmpty(releaseDate)) {
            return null
        }
        return releaseDate!!.split("-")[0]
    }
}