package aws.com.themoviedb.app.db.Type_converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object StringListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun toStringList(data: String?): List<String> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<String>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringList(strings: List<String>): String {
        return gson.toJson(strings)
    }
}