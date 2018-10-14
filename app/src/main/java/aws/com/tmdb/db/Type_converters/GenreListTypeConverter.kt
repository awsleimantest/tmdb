package aws.com.themoviedb.app.db.Type_converters

import androidx.room.TypeConverter
import aws.com.themoviedb.app.db.pojo.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
object GenreListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun toGenreList(data: String?): List<Genre> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Genre>>() {

        }.type

        return gson.fromJson<List<Genre>>(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromGenreList(genres: List<Genre>): String {
        return gson.toJson(genres)
    }
}