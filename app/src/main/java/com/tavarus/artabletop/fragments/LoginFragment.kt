package com.tavarus.artabletop.fragments

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tavarus.artabletop.App
import com.tavarus.artabletop.R
import com.tavarus.artabletop.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.*
import javax.inject.Inject


class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModel: LoginViewModel

    private var signUp = false

    private var measuredHeight = 0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val coreComponent = (activity?.applicationContext as App).provideCoreComponent()
        coreComponent.componentManager().getOrCreateNavComponent(activity?.applicationContext as Context, coreComponent).inject(this)

        swapText.paintFlags = (swapText.paintFlags or Paint.UNDERLINE_TEXT_FLAG)

        val errorObserver = Observer<String> { message ->
            showError(message)
        }

        viewModel.errorMessage.observe(this, errorObserver)

        // TODO: Add loading on button
        actionButton.setOnClickListener {
            hideError()
            if (!signUp) {
                    viewModel.logIn(emailInput.text.toString(), passwordInput.text.toString())
            } else  {
                viewModel.signUp(
                    emailInput.text.toString(),
                    confirmEmailInput.text.toString(),
                    passwordInput.text.toString(),
                    confirmPasswordInput.text.toString()
                )
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

    // TODO: Animate error visibility
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
