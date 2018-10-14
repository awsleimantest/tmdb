package aws.com.themoviedb.app.di.modules

import android.app.Application
import androidx.room.Room
import aws.com.themoviedb.app.db.DataBase
import aws.com.themoviedb.app.db.dao.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var mApplication: Application) {

    @Provides
    @Singleton
    internal fun providesApplication(): Application {
        return mApplication
    }

    @Provides
    @Singleton
    internal fun provideTMDBDatabase(application: Application): DataBase {
        return Room.databaseBuilder(application,
                DataBase::class.java, "tmdb").build()
    }

    @Provides
    @Singleton
    internal fun provideMovieDao(database: DataBase): MovieDao {
        return database.movieDao()
    }

}