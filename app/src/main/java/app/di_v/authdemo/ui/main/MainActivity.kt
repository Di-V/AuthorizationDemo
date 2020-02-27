package app.di_v.authdemo.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.di_v.authdemo.R
import app.di_v.authdemo.ui.auth.AuthActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val authRequestCode = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            auth.currentUser!!.email?.let { updateUi(it,  getString(R.string.sign_out)) }
        } else {
            updateUi("need to login","authorization")
        }

        btn_auth_action.setOnClickListener {
            if (auth.currentUser != null) {
                auth.signOut()

                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(this, gso)

                googleSignInClient.signOut().addOnCompleteListener(this) {
                    updateUi("sign out success!", "authorization")
                }
            } else {
                startActivityForResult(Intent(this, AuthActivity::class.java), authRequestCode)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            authRequestCode -> auth.currentUser!!.email?.let { updateUi(it, getString(R.string.sign_out)) }
        }
    }

    private fun updateUi(text: String, textBtn: String) {
        textView.text = text
        btn_auth_action.text = textBtn
    }
}