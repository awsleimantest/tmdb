package aws.com.themoviedb.app.db.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Company(
        @PrimaryKey var id: Int = 0,
        var logoPath: String? = null,
        var originCountry: String? = null )