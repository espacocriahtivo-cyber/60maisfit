package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AccessibilityFooterButton
import com.example.ui.components.BrandLogo
import com.example.ui.components.PrimaryFitButton
import com.example.ui.theme.FitLime

@Composable
fun SplashScreen(
    onStartClick: () -> Unit,
    onAccessibilityClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("splash_screen")
    ) {
        // Hero Background Photo
        Image(
            painter = painterResource(id = R.drawable.img_splash_hero),
            contentDescription = "Mulher 60+ praticando atividade física com vitalidade",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient Dark Overlay (rich dark athletic gradient for strong readability)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x77000000),
                            Color(0x550B1710),
                            Color(0xEE09130D),
                            Color(0xFF070E0A)
                        ),
                        startY = 100f
                    )
                )
        )

        // Screen Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 28.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Middle Section: Brand Logo & Impact Message
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                BrandLogo(
                    textColor = Color.White,
                    scale = 1.15f
                )

                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Mais ")
                        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                            append("saúde,\n")
                        }
                        append("força e ")
                        withStyle(style = SpanStyle(color = FitLime, fontWeight = FontWeight.ExtraBold)) {
                            append("autonomia\n")
                        }
                        append("para o seu dia a dia!")
                    },
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Movimento, força e autonomia para envelhecer com mais segurança.",
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFFD1D5DB)
                )
            }

            // Bottom Actions: Start Button & Accessibility Toggle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                PrimaryFitButton(
                    text = "Começar",
                    leadingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = onStartClick,
                    testTag = "splash_start_button"
                )

                Spacer(modifier = Modifier.height(20.dp))

                AccessibilityFooterButton(
                    onClick = onAccessibilityClick,
                    isDarkBackground = true
                )
            }
        }
    }
}
