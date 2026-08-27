package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldPrimary
import com.example.viewmodel.TapEffect

@Composable
fun FloatingTapEffect(
    effect: TapEffect,
    modifier: Modifier = Modifier
) {
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(effect.id) {
        offsetY.animateTo(
            targetValue = -60f,
            animationSpec = tween(durationMillis = 600)
        )
    }
    LaunchedEffect(effect.id) {
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 600)
        )
    }

    Box(
        modifier = modifier
            .offset { IntOffset(0, offsetY.value.toInt()) }
            .alpha(alpha.value)
    ) {
        Text(
            text = effect.text,
            color = GoldPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
        )
    }
}
