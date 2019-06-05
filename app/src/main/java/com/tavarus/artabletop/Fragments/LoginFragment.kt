package com.tavarus.artabletop.Fragments

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tavarus.artabletop.Controllers.AuthController
import com.tavarus.artabletop.MainActivity
import com.tavarus.artabletop.R
import kotlinx.android.synthetic.main.login_fragment.*
import android.view.animation.Animation
import android.view.animation.Transformation


class LoginFragment : Fragment() {

    private var signUp = false

    private var measuredHeight = 0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swapText.paintFlags = (swapText.paintFlags or Paint.UNDERLINE_TEXT_FLAG)
        actionButton.setOnClickListener {
            hideError()
            if (!signUp && validateLogin()) {
                    AuthController.INSTANCE.logIn(
                        emailInput.text.toString(),
                        passwordInput.text.toString(),
                        activity!! as MainActivity,
                        {},
                        { exception -> showError(exception?.localizedMessage ?: "") })
            } else if (validateSignUp()) {
                AuthController.INSTANCE.signUp(
                    emailInput.text.toString(),
                    passwordInput.text.toString(),
                    activity!! as MainActivity,
                    {},
                    { exception -> showError(exception?.localizedMessage ?: "") })
            }

        }
        swapText.setOnClickListener {
            hideError()
            if (!signUp) {
                confirmEmailInputLayout.visibility = View.VISIBLE
                confirmPasswordInputLayout.visibility = View.VISIBLE
                openAnim(marginSpacerEmail) {
                    confirmEmailInputLayout.hint = "Confirm Email"
                }
                openAnim(marginSpacerPassword) {
                    confirmPasswordInputLayout.hint = "Confirm Password"
                }
                actionButton.text = getString(R.string.sign_up)
                swapText.text = getString(R.string.login)
                signUp = true
            } else {
                confirmEmailInputLayout.hint = ""
                confirmPasswordInputLayout.hint = ""
                closeAnim(marginSpacerEmail) {
                    confirmEmailInputLayout.visibility = View.INVISIBLE
                }
                closeAnim(marginSpacerPassword) {
                    confirmPasswordInputLayout.visibility = View.INVISIBLE
                }
                actionButton.text = getString(R.string.login)
                swapText.text = getString(R.string.sign_up)
                signUp = false
            }
        }
    }

    private fun validateLogin() : Boolean {
        //Do some field level validation
        return true
    }

    private fun validateSignUp() : Boolean {
        if (emailInput.text != confirmEmailInput.text) {
            showError("Emails don't match")
            return false
        }
        if (passwordInput.text != confirmPasswordInput.text) {
            showError("Passwords don't match")
            return false
        }
        return true
    }

    private fun openAnim(view: View, onEnd: ()->Unit){
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val params = view.layoutParams as ViewGroup.MarginLayoutParams
                if (measuredHeight == 0) {
                    measuredHeight = params.bottomMargin
                }
                params.bottomMargin = (params.bottomMargin - (params.bottomMargin * interpolatedTime).toInt())
                view.layoutParams = params
            }
        }
        a.duration = 500
        a.setAnimationListener( object: Animation.AnimationListener{
            override fun onAnimationEnd(p0: Animation?) {
                onEnd()
            }
            override fun onAnimationRepeat(p0: Animation?) {
            }
            override fun onAnimationStart(p0: Animation?) {
            }
        })
        view.startAnimation(a)
        view.requestLayout()
    }

    private fun closeAnim(view: View, onEnd: ()->Unit){
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val params = view.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin = (measuredHeight * interpolatedTime).toInt()
                view.layoutParams = params
            }
        }
        a.duration = 500
        a.setAnimationListener( object: Animation.AnimationListener{
            override fun onAnimationEnd(p0: Animation?) {
                onEnd()
            }
            override fun onAnimationRepeat(p0: Animation?) {
            }
            override fun onAnimationStart(p0: Animation?) {
            }
        })
        view.startAnimation(a)
        view.requestLayout()
    }

    //These should be animated eventually? Also loading on the button should be
    private fun showError(message: String) {
        if (message.isNotEmpty()) {
            exceptionText.visibility = View.VISIBLE
            exceptionText.text = message
        }
    }

    private fun hideError() {
        exceptionText.visibility = View.GONE
        exceptionText.text = ""
    }
}
