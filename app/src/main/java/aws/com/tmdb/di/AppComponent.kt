package aws.com.themoviedb.app.di

import aws.com.themoviedb.app.di.modules.AppModule
import aws.com.themoviedb.app.di.modules.NetworkModule
import aws.com.tmdb.ui.home.HomeFragment
import aws.com.tmdb.ui.movie.MovieFragment
import aws.com.tmdb.ui.search.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(fragment: HomeFragment)
    fun inject(fragment: MovieFragment)
    fun inject(fragment: SearchFragment)
}