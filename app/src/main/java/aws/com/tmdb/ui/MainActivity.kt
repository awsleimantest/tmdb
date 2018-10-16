package aws.com.tmdb.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import aws.com.tmdb.R
import aws.com.tmdb.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment.newInstance())
                    .addToBackStack("root_fragment")
                    .commit()
        }
    }

    fun push(fragment: Fragment, animate: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        if(animate){
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        }
        transaction.replace(R.id.container, fragment,"last_push")
                .addToBackStack(null)
                .commit()
    }
    private val TAG = "MainActivity.kt: "

    fun replace(fragment: Fragment){
        val oldFragment = supportFragmentManager.findFragmentByTag("last_push")
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        Log.v("awslog", "$TAG replace() called oldFragment : $oldFragment")
        if(oldFragment != null) {
            ft.remove(oldFragment!!)
        }
        ft.add(R.id.container, fragment, "last_push")
                .addToBackStack(null)
        ft.commit()
    }

    fun pop() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        pop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
