package aws.com.themoviedb.app.db.Type_converters

import androidx.room.TypeConverter
import aws.com.themoviedb.app.db.pojo.Company
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CompanyListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun toCompanyList(data: String?): List<Company> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Company>>() {

        }.type

        return gson.fromJson<List<Company>>(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromCompanyList(companies: List<Company>): String {
        return gson.toJson(companies)
    }
}