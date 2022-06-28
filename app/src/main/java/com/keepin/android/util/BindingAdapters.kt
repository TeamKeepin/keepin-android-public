package com.keepin.android.util

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import coil.load
import com.airbnb.lottie.LottieAnimationView
import com.keepin.android.R

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("imageCoil")
fun ImageView.imageCoil(url: String?) {
    url?.let {
        load(url) {
            placeholder(R.color.gray_f9f9f9)
        }
    }
}

@BindingAdapter("isLottiePlay")
fun LottieAnimationView.isLottiePlay(value: Boolean?) {
    value?.let {
        isVisible = value
        if (value) playAnimation() else cancelAnimation()
    }
}
