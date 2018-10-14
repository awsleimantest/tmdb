package aws.com.themoviedb.app.db.pojo

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import aws.com.themoviedb.app.db.Type_converters.*
import aws.com.tmdb.utils.getReleaseYear

@Entity
data class Movie(
        @PrimaryKey
        var id: Int = 0,
        var title: String? = null,
        var releaseDate: String? = null,
        var posterPath: String? = null,
        var backdropPath: String? = null,
        var voteCount: Int = 0,
        var video: Boolean = false,
        var voteAverage: Float = 0.toFloat(),
        var popularity: Float = 0.toFloat(),
        var originalLanguage: String? = null,
        var originalTitle: String? = null,
        @TypeConverters(StringListTypeConverter::class) var genreIds: List<String?>? = emptyList(),
        var adult: Boolean = false,
        var overview: String? = null,
        var budget: Long = 0.toLong(),
        @TypeConverters(GenreListTypeConverter::class) var genres: List<Genre?>? = emptyList(),
        var homepage: String? = null,
        var imdbId: String? = null,
        @TypeConverters(CompanyListTypeConverter::class) var productionCompanies: List<Company?>? = emptyList(),
        @TypeConverters(CountryListTypeConverter::class) var productionCountries: List<Country?>? = emptyList(),
        var revenue: Long = 0.toLong(),
        var runtime: Int = 0,
        @TypeConverters(LanguageListTypeConverter::class) var spokenLanguages: List<Language?>? = emptyList(),
        var status: String? = null,
        var tagline: String? = null) {

    fun getDisplayTitle(): String {
        var result = ""
        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(releaseDate)) {
            val releaseYear = getReleaseYear(releaseDate)
            result = if (TextUtils.isEmpty(releaseYear)) {
                getTitleOrEmpty()
            } else {
                getTitleOrEmpty().plus("(").plus(releaseYear).plus(")")
            }
        }
        return result
    }

    private fun getTitleOrEmpty(): String {
        return if (title != null) title!! else ""
    }

    fun getGenresAsString(): String {
        var result = ""
        val builder = StringBuilder()
        if (genres != null) {
            genres!!.forEach {
                if(it != null){
                    if(!builder.isEmpty()){
                        builder.append(", ")
                    }
                    builder.append(it.name)
                }
            }
            result = builder.toString()
        }
        return result
    }
}