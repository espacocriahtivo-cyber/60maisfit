package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.BrandLogo

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

        // Gradient Dark Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x99000000),
                            Color(0x4408120A),
                            Color(0x88060D07),
                            Color(0xF5040805)
                        ),
                        startY = 0f
                    )
                )
        )

        // Dynamic Green Swooshes / Framing waves from TELA INICIAL.jpg
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Top-left dynamic curve
            val topPath = Path().apply {
                moveTo(0f, h * 0.18f)
                cubicTo(
                    w * 0.12f, h * 0.14f,
                    w * 0.15f, h * 0.05f,
                    w * 0.08f, 0f
                )
            }
            drawPath(
                path = topPath,
                color = Color(0xFF76C00D),
                style = Stroke(width = 12f)
            )

            // Bottom-left green swoosh
            val botLeft = Path().apply {
                moveTo(0f, h * 0.78f)
                cubicTo(
                    w * 0.15f, h * 0.83f,
                    w * 0.28f, h * 0.90f,
                    0f, h
                )
            }
            drawPath(
                path = botLeft,
                color = Color(0xFF76C00D),
                style = Stroke(width = 14f)
            )

            // Bottom-right green swoosh
            val botRight = Path().apply {
                moveTo(w, h * 0.82f)
                cubicTo(
                    w * 0.85f, h * 0.87f,
                    w * 0.75f, h * 0.94f,
                    w * 0.95f, h
                )
            }
            drawPath(
                path = botRight,
                color = Color(0xFF76C00D),
                style = Stroke(width = 12f)
            )
        }

        // Screen Content matching exact visual composition of TELA INICIAL.jpg
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 28.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Brand Logo & Exact Headline
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                BrandLogo(
                    textColor = Color.White,
                    scale = 1.35f
                )

                Spacer(modifier = Modifier.height(34.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                            append("Mais saúde,\n")
                            append("força e\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF8CEE00),
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append("independência\n")
                        }
                        withStyle(style = SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                            append("para o seu\n")
                            append("dia a dia!")
                        }
                    },
                    fontSize = 30.sp,
                    lineHeight = 38.sp,
                    letterSpacing = (-0.5).sp
                )
            }

            // Bottom Actions: Pill Button COMEÇAR & Accessibility
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            ) {
                // Vibrant Lime Pill Button with black arrow circle and COMEÇAR text
                Button(
                    onClick = onStartClick,
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8CEE00),
                        contentColor = Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 2.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(60.dp)
                        .testTag("splash_start_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Black circle with right arrow
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF8CEE00),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = "COMEÇAR",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            letterSpacing = 1.5.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Accessibility button with circular icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onAccessibilityClick() }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("accessibility_toggle_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF5BA70D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Accessibility,
                            contentDescription = "Acessibilidade",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Acessibilidade",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
