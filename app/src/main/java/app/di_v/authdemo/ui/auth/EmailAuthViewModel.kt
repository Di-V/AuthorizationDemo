package app.di_v.authdemo.ui.auth

import androidx.lifecycle.*
import app.di_v.authdemo.R
import app.di_v.authdemo.repository.AuthEmailRepository
import app.di_v.authdemo.data.model.AuthFormState
import app.di_v.authdemo.data.model.FormType
import app.di_v.authdemo.data.model.AuthResult
import kotlinx.coroutines.*

class EmailAuthViewModel : ViewModel() {
    private val authRepository = AuthEmailRepository()

    private val _formState = MutableLiveData<AuthFormState>(AuthFormState(type = FormType.NEXT))
    val formState: LiveData<AuthFormState>
            get() = _formState

    fun action(email: String, password: String?) {
        val type = _formState.value!!.type
        _formState.value = AuthFormState(loading = true, type = type)

        viewModelScope.async(Dispatchers.IO) {
            when(_formState.value!!.type) {
                FormType.NEXT -> {
                    if (isEmailValid(email = email)) getSignInList(email = email)
                    else _formState.postValue(
                        AuthFormState(
                            emailError = R.string.invalid_email,
                            type = FormType.NEXT))
                }
                FormType.LOGIN -> {
                    if(isPasswordValid(password)) signInWithEmail(email, password!!)
                    else _formState.postValue(
                        AuthFormState(
                            passwordError = R.string.invalid_password,
                            type = FormType.LOGIN))
                }
                FormType.REGISTER -> {
                    if(isPasswordValid(password)) createUserWithEmail(email, password!!)
                    else _formState.postValue(
                        AuthFormState(
                            passwordError = R.string.invalid_password,
                            type = FormType.REGISTER))
                }
                FormType.SUCCESS -> ""
            }
        }
    }

    private fun getSignInList(email: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = authRepository.getSignInEmailList(email)
        when (result) {
            is AuthResult.Success -> {
                if (result.data.contains("password")) {
                    _formState.postValue(AuthFormState(emailError = null, type = FormType.LOGIN))
                } else {
                    _formState.postValue(AuthFormState(emailError = null, type = FormType.REGISTER))
                }
            }
            is AuthResult.Error -> {
                _formState.postValue(AuthFormState(emailError = result.exception, type = FormType.NEXT))
            }
        }
    }

    private fun signInWithEmail(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.firebaseSignInWithEmail(email, password)
            when (result) {
                is AuthResult.Success -> {
                    _formState.postValue(AuthFormState(passwordError = null, type = FormType.SUCCESS))
                }
                is AuthResult.Error -> {
                    _formState.postValue(AuthFormState(passwordError = result.exception, type = FormType.LOGIN))
                }
            }
    }

    private fun createUserWithEmail(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = authRepository.firebaseCreateUserWithEmail(email, password)
        when (result) {
            is AuthResult.Success -> {
                _formState.postValue(AuthFormState(passwordError = null, type = FormType.SUCCESS))
            }
            is AuthResult.Error -> {
                _formState.postValue(AuthFormState(passwordError = result.exception, type = FormType.REGISTER))
            }
        }
    }

    private fun isEmailValid(email: String?): Boolean = email!=null && email.contains('@')

    private fun isPasswordValid(password: String?) = password!=null && password.length > 5
}
