package aws.com.tmdb.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import aws.com.tmdb.ui.MainActivity
import java.util.*

class SplashActivity: AppCompatActivity() {

    private var mStartActivityHandler: Handler? = null
    private var mActivityStarterTimerTask: ActivityStarterTimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStartActivityHandler = Handler()
        mActivityStarterTimerTask = ActivityStarterTimerTask()
    }

    override fun onPause() {
        super.onPause()
        mStartActivityHandler!!.removeCallbacks(mActivityStarterTimerTask)
    }

    override fun onResume() {
        super.onResume()
        mStartActivityHandler!!.postDelayed(mActivityStarterTimerTask, 500)
    }

    private inner class ActivityStarterTimerTask : TimerTask() {
        override fun run() {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}