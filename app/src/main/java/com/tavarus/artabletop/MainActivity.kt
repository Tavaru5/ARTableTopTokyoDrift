package com.tavarus.artabletop

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.tavarus.artabletop.board.BoardFragment
import com.tavarus.artabletop.editor.EditorFragment
import com.tavarus.artabletop.home.HomeFragment
import com.tavarus.artabletop.login.LoginFragment
import com.tavarus.artabletop.dataModels.NavActionEnum
import com.tavarus.artabletop.models.NavState
import com.tavarus.artabletop.dataModels.NavStateEnum
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navState: NavState

    fun createFrag(): Fragment? {
        return when(navState.currentScreen) {
            NavStateEnum.LOGIN -> {
                LoginFragment()
            }
            NavStateEnum.HOME -> {
                HomeFragment()
            }
            NavStateEnum.BOARD -> {
                BoardFragment()
            }
            NavStateEnum.EDITOR -> {
                EditorFragment()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val coreComponent = (applicationContext as App).provideCoreComponent()
        coreComponent.componentManager().getOrCreateNavComponent(applicationContext as Context, coreComponent).inject(this)

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
        if (supportFragmentManager.backStackEntryCount > 1 && navState.allowBackNav) {
            supportFragmentManager.popBackStack()
        } else {
            // TODO: Potentially add a pop up dialog
            finishAndRemoveTask()
        }
    }
}
