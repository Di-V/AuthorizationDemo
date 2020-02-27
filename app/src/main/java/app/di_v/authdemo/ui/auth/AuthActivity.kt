package app.di_v.authdemo.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.di_v.authdemo.R
import app.di_v.authdemo.data.model.AuthResult.Success
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        viewModel.authResult.observe(this, Observer { result ->
            if(result is Success) finish()
        })

        auth_btn_sign_in_email.setOnClickListener {
            signInWithEmail()
        }

        auth_btn_sign_in_google.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithEmail() {
        EmailAuthFragment.newInstance(supportFragmentManager)

    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            RC_SIGN_IN -> viewModel.signInWithGoogle(data)
        }
    }
}
