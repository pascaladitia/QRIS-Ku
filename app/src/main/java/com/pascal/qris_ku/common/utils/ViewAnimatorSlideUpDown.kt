package com.pascal.qris_ku.common.utils

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup


object ViewAnimatorSlideUpDown {
    fun slideDown(view: View) {
        view.visibility = View.VISIBLE
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = 1
        view.layoutParams = layoutParams
        view.measure(View.MeasureSpec.makeMeasureSpec(Resources.getSystem().displayMetrics.widthPixels,
            View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED))
        val height: Int = view.measuredHeight
        val valueAnimator = ObjectAnimator.ofInt(1, height)
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            if (height > value) {
                val layoutParams: ViewGroup.LayoutParams = view.layoutParams
                layoutParams.height = value
                view.setLayoutParams(layoutParams)
            } else {
                val layoutParams: ViewGroup.LayoutParams = view.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                view.setLayoutParams(layoutParams)
            }
        }
        valueAnimator.start()
    }

    fun slideUp(view: View) {
        view.post(Runnable {
            val height: Int = view.height
            val valueAnimator = ObjectAnimator.ofInt(height, 0)
            valueAnimator.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                if (value > 0) {
                    val layoutParams: ViewGroup.LayoutParams = view.layoutParams
                    layoutParams.height = value
                    view.layoutParams = layoutParams
                } else {
                    view.visibility = View.GONE
                }
            }
            valueAnimator.start()
        })
    }
}