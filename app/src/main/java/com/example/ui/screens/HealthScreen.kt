package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PhysicalAssessment
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark
import com.example.ui.theme.HealthHeart
import com.example.ui.theme.HealthOxygen
import com.example.ui.theme.HealthPressure

@Composable
fun HealthScreen(
    assessment: PhysicalAssessment,
    onBackClick: () -> Unit,
    onUpdateMetrics: (PhysicalAssessment) -> Unit
) {
    var showRecordDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .testTag("health_screen")
    ) {
        TopBarWithBack(
            title = "Minha saúde",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Sinais Vitais e Monitoramento",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Acompanhamento diário para um treino seguro e personalizado.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Card 1: Frequência Cardíaca
                HealthMetricCard(
                    title = "Frequência cardíaca",
                    value = "${assessment.heartRateBpm} bpm",
                    status = "Normal",
                    icon = Icons.Default.Favorite,
                    iconBgColor = Color(0xFFFEE2E2),
                    iconColor = HealthHeart
                )

                // Card 2: Pressão Arterial
                HealthMetricCard(
                    title = "Pressão arterial",
                    value = assessment.bloodPressure,
                    status = "Normal",
                    icon = Icons.Default.Speed,
                    iconBgColor = Color(0xFFE0F2FE),
                    iconColor = HealthPressure
                )

                // Card 3: Saturação SpO2
                HealthMetricCard(
                    title = "Saturação (SpO₂)",
                    value = "${assessment.oxygenSaturation}%",
                    status = "Normal",
                    icon = Icons.Default.Air,
                    iconBgColor = Color(0xFFCFFAFE),
                    iconColor = HealthOxygen
                )
            }

            PrimaryFitButton(
                text = "Registrar Novos Dados",
                onClick = { showRecordDialog = true },
                testTag = "record_health_data_button"
            )
        }
    }

    if (showRecordDialog) {
        var newHeartRate by remember { mutableStateOf(assessment.heartRateBpm.toString()) }
        var newPressure by remember { mutableStateOf(assessment.bloodPressure) }
        var newOxygen by remember { mutableStateOf(assessment.oxygenSaturation.toString()) }

        AlertDialog(
            onDismissRequest = { showRecordDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Registrar Novos Dados",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newHeartRate,
                        onValueChange = { newHeartRate = it },
                        label = { Text("Frequência cardíaca (bpm)") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    OutlinedTextField(
                        value = newPressure,
                        onValueChange = { newPressure = it },
                        label = { Text("Pressão arterial (ex: 120/80)") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    OutlinedTextField(
                        value = newOxygen,
                        onValueChange = { newOxygen = it },
                        label = { Text("Saturação de oxigênio (%)") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            },
            confirmButton = {
                PrimaryFitButton(
                    text = "Salvar Dados",
                    onClick = {
                        onUpdateMetrics(
                            assessment.copy(
                                heartRateBpm = newHeartRate.toIntOrNull() ?: assessment.heartRateBpm,
                                bloodPressure = if (newPressure.isNotBlank()) newPressure else assessment.bloodPressure,
                                oxygenSaturation = newOxygen.toIntOrNull() ?: assessment.oxygenSaturation
                            )
                        )
                        showRecordDialog = false
                    }
                )
            }
        )
    }
}

@Composable
private fun HealthMetricCard(
    title: String,
    value: String,
    status: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(FitLime.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = status,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = FitLimeDark
                )
            }
        }
    }
}
