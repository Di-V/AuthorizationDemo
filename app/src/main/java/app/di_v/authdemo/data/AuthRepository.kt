package app.di_v.authdemo.data

import app.di_v.authdemo.data.model.FormType
import app.di_v.authdemo.data.model.Result
import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    val firebaseAuth = FirebaseAuth.getInstance()

    fun firebaseSignInWithEmail(email: String, password: String): Result {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {

                }
            }
        return Result(success = true, type = FormType.LOGIN)
    }

    fun firebaseCreateUserWithEmail(email: String, password: String): Result {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {

                }
            }
        return Result(success = true, type = FormType.REGISTER)
    }

    fun sendEmailVerification(email: String): Result {
        //TODO: Verification email
        return if (true) {
            Result(success = false, type = FormType.LOGIN)
        } else Result(success = false, type = FormType.REGISTER)
    }
}
