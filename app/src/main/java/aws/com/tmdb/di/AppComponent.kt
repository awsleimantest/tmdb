package aws.com.themoviedb.app.di

import aws.com.themoviedb.app.di.modules.AppModule
import aws.com.themoviedb.app.di.modules.NetworkModule
import aws.com.themoviedb.app.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(fragment: HomeFragment)
}