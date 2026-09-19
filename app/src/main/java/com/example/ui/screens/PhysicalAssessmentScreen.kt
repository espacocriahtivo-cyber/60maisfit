package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PhysicalAssessment
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark

@Composable
fun PhysicalAssessmentScreen(
    assessment: PhysicalAssessment,
    onBackClick: () -> Unit,
    onSaveClick: (PhysicalAssessment) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dados, 1: Funcional

    var weight by remember { mutableStateOf(assessment.weightKg) }
    var height by remember { mutableStateOf(assessment.heightM) }
    var bloodPressure by remember { mutableStateOf(assessment.bloodPressure) }
    var heartRate by remember { mutableStateOf(assessment.heartRateBpm.toString()) }
    var oxygenSat by remember { mutableStateOf(assessment.oxygenSaturation.toString()) }

    var sitToStand by remember { mutableStateOf(assessment.sitToStandReps.toString()) }
    var walkDistance by remember { mutableStateOf(assessment.walkDistanceMeters.toString()) }
    var flexibility by remember { mutableStateOf(assessment.flexibilityRating) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .testTag("physical_assessment_screen")
    ) {
        TopBarWithBack(
            title = "Avaliação física",
            onBackClick = onBackClick
        )

        // Tabs: [Dados] [Funcional]
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = FitLimeDark,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "Dados",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 15.sp,
                        color = if (selectedTab == 0) FitLimeDark else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "Funcional",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 15.sp,
                        color = if (selectedTab == 1) FitLimeDark else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (selectedTab == 0) {
                // Biometrics Tab (Dados)
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    AssessmentField(
                        label = "Peso (kg)",
                        value = weight,
                        onValueChange = { weight = it }
                    )

                    AssessmentField(
                        label = "Altura (m)",
                        value = height,
                        onValueChange = { height = it }
                    )

                    AssessmentField(
                        label = "Pressão arterial",
                        value = bloodPressure,
                        onValueChange = { bloodPressure = it }
                    )

                    AssessmentField(
                        label = "Frequência cardíaca",
                        value = heartRate,
                        suffix = "bpm",
                        onValueChange = { heartRate = it }
                    )

                    AssessmentField(
                        label = "Saturação de oxigênio (SpO₂)",
                        value = oxygenSat,
                        suffix = "%",
                        onValueChange = { oxygenSat = it }
                    )
                }
            } else {
                // Functional Tests Tab
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    AssessmentField(
                        label = "Sentar e Levantar (30s)",
                        value = sitToStand,
                        suffix = "repetições",
                        onValueChange = { sitToStand = it }
                    )

                    AssessmentField(
                        label = "Teste de Caminhada (6 min)",
                        value = walkDistance,
                        suffix = "metros",
                        onValueChange = { walkDistance = it }
                    )

                    AssessmentField(
                        label = "Flexibilidade e Mobilidade",
                        value = flexibility,
                        onValueChange = { flexibility = it }
                    )

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FitLime.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Classificação Geral: Excelente",
                                fontWeight = FontWeight.Bold,
                                color = FitLimeDark,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Nível de força e equilíbrio ideais para iniciar programas intermediários de musculação adaptada.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryFitButton(
                text = "Salvar Avaliação",
                onClick = {
                    val updated = assessment.copy(
                        weightKg = weight,
                        heightM = height,
                        bloodPressure = bloodPressure,
                        heartRateBpm = heartRate.toIntOrNull() ?: 72,
                        oxygenSaturation = oxygenSat.toIntOrNull() ?: 98,
                        sitToStandReps = sitToStand.toIntOrNull() ?: 14,
                        walkDistanceMeters = walkDistance.toIntOrNull() ?: 420,
                        flexibilityRating = flexibility
                    )
                    onSaveClick(updated)
                },
                testTag = "assessment_save_button"
            )
        }
    }
}

@Composable
private fun AssessmentField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    suffix: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        suffix = if (suffix != null) { { Text(suffix) } } else null,
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FitLimeDark,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
