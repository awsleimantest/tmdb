package aws.com.themoviedb.app.db.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class Language(
        @PrimaryKey @SerializedName("iso_639_1") var id: String = UUID.randomUUID().toString(),
        var name: String? = null)