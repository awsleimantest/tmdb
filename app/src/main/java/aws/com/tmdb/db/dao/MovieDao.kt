package aws.com.themoviedb.app.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import aws.com.themoviedb.app.db.pojo.Movie

@Dao
interface MovieDao {

    @get:Query("SELECT * FROM Movie ORDER BY popularity DESC")
    val movies: LiveData<List<Movie>>

    @Query("SELECT * FROM Movie WHERE id IS :id")
    fun getMovie(id: Int): LiveData<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Update
    fun update(movie: Movie)

    @Delete
    fun delete(movie: Movie)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>)

    @Delete
    fun delete(movie: List<Movie>)

    @Query("DELETE FROM Movie")
    fun deleteAll()

    @Query("SELECT id FROM Movie WHERE id IS :id")
    fun loadMovieId(id: Int): Int?
}