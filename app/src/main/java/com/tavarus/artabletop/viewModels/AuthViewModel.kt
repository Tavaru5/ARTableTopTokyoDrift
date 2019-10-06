package com.tavarus.artabletop.viewModels

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.tavarus.artabletop.models.NavState
import com.tavarus.artabletop.models.NavStateEnum
import javax.inject.Inject

class AuthViewModel @Inject constructor(val navState: NavState) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    fun logIn(email: String, password: String ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navState.replaceWithView(NavStateEnum.HOME)
                } else {
                    errorMessage.value = task.exception?.localizedMessage
                }
            }
    }

    fun signUp(email: String, password: String ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navState.replaceWithView(NavStateEnum.HOME)
                } else {
                    errorMessage.value = task.exception?.localizedMessage
                }
            }
    }
}
