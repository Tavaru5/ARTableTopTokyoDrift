package com.tavarus.artabletop

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
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

    fun createFrag(): Fragment? {
        return when(navState.currentScreen.value) {
            NavStateEnum.LOGIN -> {
                LoginFragment()
            }
            NavStateEnum.HOME -> {
                HomeFragment()
            }
            NavStateEnum.BOARD -> {
                BoardFragment()
            }
            else -> {
                null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (applicationContext as App).provideCoreComponent().inject(this)

        val navObserver = Observer<NavActionEnum> { navActionEnum ->
            when (navActionEnum) {
                NavActionEnum.PUSH -> {
                    addFragment(createFrag()!!)
                }
                NavActionEnum.REPLACE -> {
                    replaceFragment(createFrag()!!)
                }
                NavActionEnum.BACK -> {
                    onBackPressed()
                }
                else -> {
                    // oh no. This should never happen. Do no navigation
                }
            }
        }

        navState.action.observe(this, navObserver)
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            // In the future maybe pop an exit dialog?
            super.onBackPressed()
        }
    }
}
