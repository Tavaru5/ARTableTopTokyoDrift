package com.tavarus.artabletop

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tavarus.artabletop.Controllers.AuthController
import com.tavarus.artabletop.Controllers.BoardController
import com.tavarus.artabletop.Fragments.BoardFragment
import com.tavarus.artabletop.Fragments.LoginFragment
import com.tavarus.artabletop.POJO.BoardListPOJO


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!AuthController.INSTANCE.isLoggedIn()) {
            addFragment(LoginFragment())
        } else {
            setupBoard { addFragment(BoardFragment()) }
        }
    }

    fun onLogin() {
       setupBoard { replaceFragment(BoardFragment()) }
    }

    fun setupBoard(onSuccess: () -> Unit) {
        BoardController.INSTANCE.initialize(AuthController.INSTANCE.getUser()!!.uid, this, onSuccess, {})
    }

    fun onLogout() {
        BoardController.INSTANCE.clear()
        replaceFragment(LoginFragment())
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun AppCompatActivity.addFragment(fragment: Fragment){
        supportFragmentManager.inTransaction { add(R.id.fragment_placeholder, fragment) }
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{ replace(R.id.fragment_placeholder, fragment) }
    }
}
