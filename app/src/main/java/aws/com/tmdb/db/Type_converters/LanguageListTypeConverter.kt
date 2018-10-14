package aws.com.themoviedb.app.db.Type_converters

import androidx.room.TypeConverter
import aws.com.themoviedb.app.db.pojo.Language
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object LanguageListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun toLanguageList(data: String?): List<Language> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Language>>() {

        }.type

        return gson.fromJson<List<Language>>(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromLanguageList(languages: List<Language>): String {
        return gson.toJson(languages)
    }
}