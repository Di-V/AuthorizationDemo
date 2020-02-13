package app.di_v.authdemo.data.model

data class AuthFormState(
    var emailError: Int? = null,
    var passwordError: Int? = null,
    var loading: Boolean = false,
    var type: FormType
)

enum class FormType {
    NEXT, LOGIN, REGISTER, SUCCESS
}