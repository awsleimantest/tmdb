package aws.com.themoviedb.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import aws.com.themoviedb.app.db.Type_converters.*
import aws.com.themoviedb.app.db.dao.MovieDao
import aws.com.themoviedb.app.db.pojo.*

@Database(entities = [Movie::class, Company::class, Country::class, Genre::class, Language::class, aws.com.themoviedb.app.db.pojo.Collection::class], version = 1, exportSchema = false)
@TypeConverters(CollectionTypeConverter::class, CompanyListTypeConverter::class, CountryListTypeConverter::class, GenreListTypeConverter::class, LanguageListTypeConverter::class,StringListTypeConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
