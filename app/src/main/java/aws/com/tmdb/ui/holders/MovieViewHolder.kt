package aws.com.themoviedb.app.ui.base.holders

import android.text.TextUtils
import android.view.View
import aws.com.themoviedb.app.db.pojo.Movie
import aws.com.themoviedb.app.utils.getImagePath
import aws.com.themoviedb.app.utils.loadImage
import aws.com.tmdb.R
import aws.com.tmdb.ui.base.BaseViewHolder
import aws.com.tmdb.ui.base.Displayable
import aws.com.tmdb.ui.base.OnItemClickListener
import kotlinx.android.synthetic.main.movie_holder_layout.view.*

class MovieViewHolder(itemView: View, private var mCLickListener: OnItemClickListener?) : BaseViewHolder(itemView) {

    var isRow = false
    fun bind(movie: Displayable, isRow: Boolean) {
        this.isRow = isRow
        bind(movie)
    }

    override fun bind(item: Displayable) {
        if(item is Movie){
            itemView.setOnClickListener {
                mCLickListener?.onItemClick(item)
            }

            var imageSize = if(isRow) "w300" else "w500"
            var image = if(this.isRow) item.backdropPath?: item.posterPath else item.posterPath
            if(image == null && this.isRow) {//Some of the movies has null backdrops
                image = item.posterPath
                imageSize = "w342"
            }
            if (!TextUtils.isEmpty(image)) {
                loadImage(itemView.iv_image, getImagePath(image, imageSize), R.drawable.bg_light_grey)
            } else {
                loadImage(itemView.iv_image, null, R.drawable.bg_light_grey)
            }

            val displayTitle = item.getDisplayTitle()
            if (!TextUtils.isEmpty(displayTitle)) {
                itemView.tv_title.visibility = View.VISIBLE
                itemView.tv_title.text = displayTitle
            } else {
                itemView.tv_title.visibility = View.GONE
            }

            if (item.popularity > 0) {
                itemView.tv_popularity.visibility = View.VISIBLE
                itemView.tv_popularity.text = itemView.context.getString(R.string.Popularity_s, item.popularity.toInt().toString())
            } else {
                itemView.tv_popularity.visibility = View.GONE
            }
            // No genres to display at this point so there no need to touch itemView.tv_genre cz it's GONE by default
        }
    }
}