package aws.com.tmdb.ui.base

import aws.com.themoviedb.app.db.pojo.Movie

interface OnItemClickListener {
    fun onItemClick(movie: Movie)
}