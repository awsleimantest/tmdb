package aws.com.themoviedb.app.di

import aws.com.themoviedb.app.di.modules.AppModule
import aws.com.themoviedb.app.di.modules.DiscoverModule
import aws.com.themoviedb.app.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DiscoverModule::class])
interface AppComponent {
    fun inject(fragment: HomeFragment)
}