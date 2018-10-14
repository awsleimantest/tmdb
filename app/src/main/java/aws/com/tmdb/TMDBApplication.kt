package aws.com.themoviedb.app

import android.app.Application
import aws.com.themoviedb.app.di.AppComponent
import aws.com.themoviedb.app.di.DaggerAppComponent
import aws.com.themoviedb.app.di.modules.AppModule
import aws.com.themoviedb.app.di.modules.DiscoverModule
import com.facebook.drawee.backends.pipeline.Fresco

class TMDBApplication: Application() {


    private lateinit var mAppComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        mAppComponent = DaggerAppComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .discoverModule(DiscoverModule("https://api.themoviedb.org/3/"))
                .build()
    }

    fun getAppComponent(): AppComponent{
        return mAppComponent
    }
}