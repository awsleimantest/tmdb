package aws.com.themoviedb.app.db.Type_converters

import androidx.room.TypeConverter
import aws.com.themoviedb.app.db.pojo.Collection
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CollectionTypeConverter {
        @TypeConverter
        @JvmStatic
        fun toCollection(data: String?): Collection? {
            if (data == null) {
                return null
            }
            val type = object : TypeToken<Collection>() {}.type
            return try {
                Gson().fromJson<Collection>(data, type)
            } catch (e:Exception) {
                null
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromCollection(collection: Collection): String {
            return Gson().toJson(collection)
        }
}