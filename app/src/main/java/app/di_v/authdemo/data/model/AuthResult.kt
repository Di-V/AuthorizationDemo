package app.di_v.authdemo.data.model

sealed class AuthResult {
    data class Success(val data: String) : AuthResult()
    data class Error(val exception: Int) : AuthResult()
}

