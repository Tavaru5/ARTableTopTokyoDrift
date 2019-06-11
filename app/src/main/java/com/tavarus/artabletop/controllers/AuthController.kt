package com.tavarus.artabletop.controllers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tavarus.artabletop.MainActivity
import java.lang.Exception

class AuthController() {
    companion object {
        var INSTANCE: AuthController = AuthController()
    }

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun logIn(email: String, password: String, activity: MainActivity, onSuccess: (FirebaseUser)->Unit, onFailure: (Exception?)->Unit ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser!!)
                    activity.onLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    onFailure(task.exception)
                }
            }
    }

    fun signUp(email: String, password: String, activity: MainActivity, onSuccess: (FirebaseUser)->Unit, onFailure: (Exception?)->Unit ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser!!)
                    activity.onLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    onFailure(task.exception)
                }
            }
    }

    fun isLoggedIn() : Boolean {
        return auth.currentUser != null
    }

    fun logOut(activity: MainActivity) {
        auth.signOut()
        activity.onLogout()
    }

    fun getUser() : FirebaseUser? {
        return auth.currentUser
    }

}
