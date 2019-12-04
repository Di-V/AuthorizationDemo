package app.di_v.authdemo.data.model

data class AuthFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)