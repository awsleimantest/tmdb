package aws.com.themoviedb.app.Server.response

import aws.com.themoviedb.app.db.pojo.Movie

class MoviesResponse(var results: List<Movie>? = null, var totalPages: Int = -1)
