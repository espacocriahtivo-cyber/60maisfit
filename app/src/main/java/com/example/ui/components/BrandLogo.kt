package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FitLime

@Composable
fun BrandLogo(
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    showSubtitle: Boolean = true,
    scale: Float = 1.0f
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "60",
                fontSize = (44 * scale).sp,
                fontWeight = FontWeight.Black,
                color = textColor,
                lineHeight = (44 * scale).sp
            )
            Text(
                text = "+",
                fontSize = (34 * scale).sp,
                fontWeight = FontWeight.Black,
                color = FitLime,
                lineHeight = (34 * scale).sp,
                modifier = Modifier.padding(bottom = (8 * scale).dp)
            )
            Spacer(modifier = Modifier.width((4 * scale).dp))
            Text(
                text = "FIT",
                fontSize = (44 * scale).sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = FitLime,
                lineHeight = (44 * scale).sp
            )
        }

        if (showSubtitle) {
            Spacer(modifier = Modifier.height((2 * scale).dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.width((18 * scale).dp),
                    thickness = 1.5.dp,
                    color = FitLime
                )
                Text(
                    text = " MUSCULAÇÃO E FUNCIONALIDADE ",
                    fontSize = (9 * scale).sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = textColor.copy(alpha = 0.9f)
                )
                HorizontalDivider(
                    modifier = Modifier.width((18 * scale).dp),
                    thickness = 1.5.dp,
                    color = FitLime
                )
            }
        }
    }
}
