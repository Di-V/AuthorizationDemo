package app.di_v.authdemo.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.di_v.authdemo.R
import app.di_v.authdemo.data.AuthRepository
import app.di_v.authdemo.data.model.AuthFormState
import app.di_v.authdemo.data.model.Result

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private var authRepository: AuthRepository = AuthRepository()
    private val _authenticatedUserLiveData = MutableLiveData<Result>()
    val authenticatedResultLiveData: LiveData<Result> = _authenticatedUserLiveData
    private val _formState = MutableLiveData<AuthFormState>()
    val formState: LiveData<AuthFormState> = _formState

    fun verificationEmail(email: String) {
        _authenticatedUserLiveData.value = authRepository.sendEmailVerification(email)
    }

    fun signInWithEmail(email: String, password: String) {
        _authenticatedUserLiveData.value = authRepository.firebaseSignInWithEmail(email, password)
    }

    fun createUserWithEmail(email: String, password: String) {
        _authenticatedUserLiveData.value = authRepository.firebaseCreateUserWithEmail(email, password)
    }

    fun isEmailValid(email: String) {
        if (email.contains('@')) {
            _formState.value = AuthFormState(isDataValid = true)
        } else _formState.value = AuthFormState(emailError = R.string.invalid_email)
    }


    fun isPasswordValid(password: String) {
        if (password.length > 5) {
            _formState.value = AuthFormState(isDataValid = true)
        } else _formState.value = AuthFormState(passwordError = R.string.invalid_password)
    }
}
