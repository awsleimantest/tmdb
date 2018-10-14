package aws.com.themoviedb.app.db.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Collection (
        @PrimaryKey var id: String = UUID.randomUUID().toString(),
        var name: String? = null,
        var posterPath : String? = null,
        var backdropPath: String? = null)