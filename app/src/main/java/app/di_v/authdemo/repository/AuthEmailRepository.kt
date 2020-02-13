package app.di_v.authdemo.repository

import android.util.Log
import app.di_v.authdemo.R
import app.di_v.authdemo.data.model.AuthResult
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthEmailRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun getSignInEmailList(email: String): AuthResult = withContext(Dispatchers.IO) {
        Log.d("Rep", " start getSignInList")
        val data = try {
            val result = auth.fetchSignInMethodsForEmail(email).await()
            Log.d("Rep", " getSignInList fun1 aaa = ${result.signInMethods.toString()}")
            AuthResult.Success(result.signInMethods.toString())
        } catch(e: FirebaseAuthEmailException) {
            AuthResult.Error(R.string.email_exception)
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(R.string.firebase_exception)
        } catch (e: Exception) {
            AuthResult.Error(R.string.exception)
        }
        data
    }

    suspend fun firebaseSignInWithEmail(email: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        val data = try {
            val result =  auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(result.toString())
        } catch(e: FirebaseAuthEmailException) {
            AuthResult.Error(R.string.email_exception)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error(R.string.invalid_credentials_exception)
        } catch (e: FirebaseTooManyRequestsException) {
            AuthResult.Error(R.string.too_many_requests_exception)
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(R.string.firebase_exception)
        } catch (e: Exception) {
            AuthResult.Error(R.string.exception)
        }
        data
    }

    suspend fun firebaseCreateUserWithEmail(email: String, password: String): AuthResult = withContext(Dispatchers.IO){
        val data = try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success(result.toString())
        } catch(e: FirebaseAuthEmailException) {
            AuthResult.Error(R.string.email_exception)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error(R.string.invalid_credentials_exception)
        } catch (e: FirebaseTooManyRequestsException) {
            AuthResult.Error(R.string.too_many_requests_exception)
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(R.string.firebase_exception)
        } catch (e: Exception) {
            AuthResult.Error(R.string.exception)
        }
        data
    }
}
