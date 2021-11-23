package com.devstree.annibee.utility

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator

import android.view.animation.Animation

import android.view.animation.TranslateAnimation


/**
 * Created by Dhaval Baldha on 21/1/21
 */

//https://stackoverflow.com/questions/14141039/view-animation-right-to-left-android
object AnimationHelper {
    fun inFromRightAnimation(): Animation {
        val inFromRight: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, +1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f)
        inFromRight.duration = 500
        inFromRight.interpolator = AccelerateInterpolator()
        return inFromRight
    }

    fun outToLeftAnimation(view: View, animationEndCallBack: () -> Unit) {
        val outtoLeft: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f)
        outtoLeft.duration = 300
        outtoLeft.interpolator = AccelerateInterpolator()
        view.startAnimation(outtoLeft)
        outtoLeft.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                animationEndCallBack.invoke()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }

    fun inFromLeftAnimation(view: View, animationEndCallBack: () -> Unit) {
        val inFromLeft: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f)
        inFromLeft.duration = 300
        inFromLeft.interpolator = AccelerateInterpolator()
        view.startAnimation(inFromLeft)
        inFromLeft.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                animationEndCallBack.invoke()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
    }

    fun outToRightAnimation(): Animation {
        val outtoRight: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, +1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f)
        outtoRight.duration = 500
        outtoRight.interpolator = AccelerateInterpolator()
        return outtoRight
    }

    fun enterAnimation(myView: View, isRight: Boolean): Animator? {
        // get the center for the clipping circle
        val cx = if (isRight) myView.right / 2 else myView.left / 2
        val cy = myView.bottom / 2

        // get the final radius for the clipping circle
        val dx = Math.max(cx, myView.width - cx)
        val dy = Math.max(cy, myView.height - cy)
        val finalRadius = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()

        // Android native animator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = 300
            animator.startDelay = 0
            animator.start()
            return animator
        }
        return null
    }

    fun existAnimation(myView: View, isRight: Boolean, listener: AnimatorListenerAdapter?): Animator? {
        // get the center for the clipping circle
        val cx = myView.bottom / 2
        val cy = if (isRight) myView.right / 2 else myView.left / 2

        // get the initial radius for the clipping circle
        val initialRadius = myView.width / 2

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // find the animation (the final radius is zero)
            val animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius.toFloat(), 5f)

            // make the view invisible when the animation is done
            animator.addListener(listener)
            animator.duration = 200
            animator.startDelay = 0
            animator.start()
            return animator
        }
        return null
    }
}