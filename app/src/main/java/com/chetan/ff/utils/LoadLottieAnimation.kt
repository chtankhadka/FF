package com.chetan.ff.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LoadLottieAnimation(
    modifier: Modifier,
    image: Int
){
    val composation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(image))
    LottieAnimation(
        composition = composation ,
        iterations = LottieConstants.IterateForever,
        modifier = modifier,
        contentScale = ContentScale.Fit,
        renderMode = RenderMode.SOFTWARE)
}