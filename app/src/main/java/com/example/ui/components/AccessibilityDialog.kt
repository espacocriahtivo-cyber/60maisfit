package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccessibilityState
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark

@Composable
fun AccessibilityDialog(
    state: AccessibilityState,
    onStateChange: (AccessibilityState) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Accessibility,
                    contentDescription = null,
                    tint = FitLimeDark,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Acessibilidade 60+",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Adapte o aplicativo para uma visualização confortável e segura.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AccessibilityToggleRow(
                    icon = Icons.Default.FormatSize,
                    title = "Letras Ampliadas",
                    subtitle = "Aumenta o tamanho dos textos para facilitar a leitura.",
                    checked = state.largeFont,
                    onCheckedChange = { onStateChange(state.copy(largeFont = it)) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                AccessibilityToggleRow(
                    icon = Icons.Default.Visibility,
                    title = "Alto Contraste",
                    subtitle = "Cores mais nítidas para melhorar a visibilidade.",
                    checked = state.highContrast,
                    onCheckedChange = { onStateChange(state.copy(highContrast = it)) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                AccessibilityToggleRow(
                    icon = Icons.Default.RecordVoiceOver,
                    title = "Instruções por Voz",
                    subtitle = "Lê em voz alta as instruções e descansos dos exercícios.",
                    checked = state.voiceAssistance,
                    onCheckedChange = { onStateChange(state.copy(voiceAssistance = it)) }
                )
            }
        },
        confirmButton = {
            PrimaryFitButton(
                text = "Concluir",
                onClick = onDismiss,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    )
}

@Composable
private fun AccessibilityToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (checked) FitLimeDark else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = FitLimeDark
                )
            )
        }
    }
}
