package aws.com.themoviedb.app.db.Type_converters

import androidx.room.TypeConverter
import aws.com.themoviedb.app.db.pojo.Country
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CountryListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun toCountryList(data: String?): List<Country> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Country>>() {

        }.type

        return gson.fromJson<List<Country>>(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromCountryList(countries: List<Country>): String {
        return gson.toJson(countries)
    }
}