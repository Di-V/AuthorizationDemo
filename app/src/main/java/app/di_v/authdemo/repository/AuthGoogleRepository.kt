package app.di_v.authdemo.repository

import app.di_v.authdemo.R
import app.di_v.authdemo.data.model.AuthResult
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthGoogleRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun firebaseAuthWithGoogle(acct: GoogleSignInAccount): AuthResult = withContext(Dispatchers.IO){
        val data = try {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            AuthResult.Success(result.toString())
        } catch (e: GoogleAuthException) {
            AuthResult.Error(R.string.google_auth_exception)
        } catch (e: Exception) {
            AuthResult.Error(R.string.exception)
        }
        data
    }
}