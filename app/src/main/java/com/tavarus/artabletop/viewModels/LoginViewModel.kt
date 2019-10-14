package com.tavarus.artabletop.viewModels

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.tavarus.artabletop.models.NavState
import javax.inject.Inject

class LoginViewModel @Inject constructor(val navState: NavState) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    fun logIn(email: String, password: String ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navState.goBack()
                } else {
                    errorMessage.value = task.exception?.localizedMessage
                }
            }
    }

    fun signUp(email: String, confirmEmail: String, password: String, confirmPassword: String ) {
        if (email != confirmEmail) {
            errorMessage.value = "Emails don't match"
        } else if (password != confirmPassword) {
            errorMessage.value = "Passwords don't match"
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navState.goBack()
                    } else {
                        errorMessage.value = task.exception?.localizedMessage
                    }
                }
        }

    }

    private fun validateLogin() : Boolean {
        //Do some field level validation
        return true
    }
}
