package app.di_v.authdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.di_v.authdemo.ui.auth.AuthFragment
import app.di_v.authdemo.ui.auth.EmailAuthFragment

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EmailAuthFragment.newInstance())
                .commitNow()
        }
    }
}
