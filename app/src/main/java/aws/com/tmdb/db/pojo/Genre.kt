package aws.com.themoviedb.app.db.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Genre(
        @PrimaryKey var id: Int = 0,
        var name: String? = null)