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
        val firestore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val doc = firestore.collection("users").document(user!!.uid)
        doc.get().addOnSuccessListener { document ->
            if (document != null) {
                BoardController.INSTANCE.initialize(document.toObject(BoardListPOJO::class.java)!!, this)
                onSuccess()
            } else {
                Log.d("KOG", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("KOG", "get failed with ", exception)
        }
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
