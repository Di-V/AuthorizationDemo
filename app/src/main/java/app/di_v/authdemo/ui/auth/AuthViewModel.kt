package app.di_v.authdemo.ui.auth

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.di_v.authdemo.data.model.AuthResult
import app.di_v.authdemo.repository.AuthGoogleRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthGoogleRepository()

    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult>
        get() = _authResult

    fun signInWithGoogle(data: Intent?) = viewModelScope.launch(Dispatchers.IO) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        try {
            val account = task.getResult(ApiException::class.java)
            _authResult.postValue(authRepository.firebaseAuthWithGoogle(account!!))
        } catch (e: ApiException) {

        }
    }
}