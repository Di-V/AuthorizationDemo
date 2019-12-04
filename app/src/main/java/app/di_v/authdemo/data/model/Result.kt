package app.di_v.authdemo.data.model

data class Result(
    val success: Boolean,
    val type: FormType = FormType.NEXT
)

enum class FormType {
    NEXT, LOGIN, REGISTER
}