package com.tavarus.artabletop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.tavarus.artabletop.controllers.AuthController
import com.tavarus.artabletop.fragments.BoardFragment
import com.tavarus.artabletop.fragments.HomeFragment
import com.tavarus.artabletop.fragments.LoginFragment
import com.tavarus.artabletop.models.NavState
import com.tavarus.artabletop.models.NavStateEnum
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navState: NavState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (applicationContext as App).provideCoreComponent().inject(this)

        val navObserver = Observer<NavStateEnum> { navStateEnum ->
            // Handle navigation
            when (navStateEnum) {
                NavStateEnum.BOARD -> {
                    addFragment(BoardFragment())
                }
                NavStateEnum.HOME -> {
                    // I don't think we're going to actually want to add
                    // the home fragment on top; we might need to implement
                    // our own stack handling.
                    addFragment(HomeFragment())
                }
                else -> {
                    // oh no. This should never happen. Do no navigation
                }
            }
        }


        navState.currentScreen.observe(this, navObserver)

        if (!AuthController.INSTANCE.isLoggedIn()) {
            addFragment(LoginFragment())
        } else {
            //Make calls, then
            addFragment(HomeFragment())
        }
    }

    fun onLogin() {
        replaceFragment(HomeFragment())
    }

    fun onLogout() {
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
