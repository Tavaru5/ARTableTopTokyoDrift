package com.tavarus.artabletop

import android.os.Bundle
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
                    moveTo(BoardFragment())
                }
                NavStateEnum.HOME -> {
                    moveTo(HomeFragment())
                }
                else -> {
                    // oh no. This should never happen. Do no navigation
                }
            }
        }


        navState.currentScreen.observe(this, navObserver)

        if (FirebaseAuth.getInstance().currentUser == null) {
            addFragment(LoginFragment())
        } else {
            addFragment(HomeFragment())
        }
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

    fun moveTo(fragment: Fragment) {
        when (navState.action.value) {
            NavActionEnum.REPLACE -> replaceFragment(fragment)
            NavActionEnum.PUSH -> addFragment(fragment)
        }
    }
}
