package com.tavarus.artabletop

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.tavarus.artabletop.fragments.BoardFragment
import com.tavarus.artabletop.fragments.HomeFragment
import com.tavarus.artabletop.fragments.LoginFragment
import com.tavarus.artabletop.models.NavActionEnum
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
                    Log.d("KOG", "moving to board")
                    moveTo(BoardFragment())
                }
                NavStateEnum.HOME -> {
                    Log.d("KOG", "Moving to home")
                    moveTo(HomeFragment())
                }
                NavStateEnum.LOGIN -> {
                    Log.d("KOG", "Moving to Login")
                    moveTo(LoginFragment())
                }
                else -> {
                    // oh no. This should never happen. Do no navigation
                }
            }
        }


        navState.currentScreen.observe(this, navObserver)

        // This should move to the home fragment. If, when you get there you aren't logged in, move to login
        //TODO: We need to observe the aciton
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun AppCompatActivity.addFragment(fragment: Fragment){
        supportFragmentManager.inTransaction { add(R.id.fragment_placeholder, fragment).addToBackStack(fragment.tag) }
    }


    fun AppCompatActivity.replaceFragment(fragment: Fragment) {
        supportFragmentManager.inTransaction{ replace(R.id.fragment_placeholder, fragment) }
    }

    fun moveTo(fragment: Fragment) {
        when (navState.action.value) {
            NavActionEnum.REPLACE -> replaceFragment(fragment)
            NavActionEnum.PUSH -> addFragment(fragment)
        }
    }

    override fun onBackPressed() {
        // We have like 4 fragments here when we should only have 1. What's going on?
        Log.d("KOG", "GOIN back")
        Log.d("KOG", supportFragmentManager.backStackEntryCount.toString())
        supportFragmentManager.popBackStack()
    }
}
