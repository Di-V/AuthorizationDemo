package app.di_v.authdemo.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import app.di_v.authdemo.R
import app.di_v.authdemo.ui.auth.AuthActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            textView.text = auth.currentUser!!.email
            btn_auth_action.text = getString(R.string.sign_out)
            btn_auth_action.setOnClickListener {
                auth.signOut()

                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(this, gso)

                googleSignInClient.signOut().addOnCompleteListener(this) {
                    textView.text = "sign out success!"
                    this.recreate()
                }
            }
        } else {
            textView.text = "need to login"
            btn_auth_action.text = "authorization"
            btn_auth_action.setOnClickListener {
                startActivityForResult(Intent(this, AuthActivity::class.java), 7)
            }
        }
    }
}