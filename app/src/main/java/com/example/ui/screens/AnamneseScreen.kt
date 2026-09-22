package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HealthConditions
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnamneseScreen(
    conditions: HealthConditions,
    onBackClick: () -> Unit,
    onNextClick: (HealthConditions) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // 1. BLOCO SAÚDE
    var medicalDiagnoses by remember { mutableStateOf(conditions.medicalDiagnoses) }
    var surgeries by remember { mutableStateOf(conditions.surgeries) }
    var hospitalizations by remember { mutableStateOf(conditions.hospitalizations) }
    var medications by remember { mutableStateOf(conditions.medications) }
    var allergies by remember { mutableStateOf(conditions.allergies) }
    var fallHistory by remember { mutableStateOf(conditions.fallHistory) }

    // 2. BLOCO DOR
    var painLocation by remember { mutableStateOf(conditions.painLocation) }
    var painIntensity by remember { mutableFloatStateOf(conditions.painIntensity.toFloat()) }

    // 3. BLOCO ESTILO DE VIDA (SONO & NÍVEL DE ATIVIDADE FÍSICA)
    var sleepQuality by remember { mutableStateOf(conditions.sleepQuality) }
    var physicalActivityLevel by remember { mutableStateOf(conditions.physicalActivityLevel) }

    // 4. BLOCO DOENÇAS / COMORBIDADES GERONTOLÓGICAS (17 itens solicitados)
    var hypertension by remember { mutableStateOf(conditions.hypertension) }
    var type2Diabetes by remember { mutableStateOf(conditions.type2Diabetes) }
    var obesity by remember { mutableStateOf(conditions.obesity) }
    var osteopenia by remember { mutableStateOf(conditions.osteopenia) }
    var osteoporosis by remember { mutableStateOf(conditions.osteoporosis) }
    var arthrosis by remember { mutableStateOf(conditions.arthrosis) }
    var arthritis by remember { mutableStateOf(conditions.arthritis) }
    var lowerBackPain by remember { mutableStateOf(conditions.lowerBackPain) }
    var cardiovascularDiseases by remember { mutableStateOf(conditions.cardiovascularDiseases) }
    var respiratoryDiseases by remember { mutableStateOf(conditions.respiratoryDiseases) }
    var parkinson by remember { mutableStateOf(conditions.parkinson) }
    var strokeSequelae by remember { mutableStateOf(conditions.strokeSequelae) }
    var sarcopenia by remember { mutableStateOf(conditions.sarcopenia) }
    var frailty by remember { mutableStateOf(conditions.frailty) }
    var balanceChanges by remember { mutableStateOf(conditions.balanceChanges) }
    var fallRisk by remember { mutableStateOf(conditions.fallRisk) }
    var mobilityLimitations by remember { mutableStateOf(conditions.mobilityLimitations) }

    // 5. OBSERVAÇÕES E VALIDAÇÃO PROFISSIONAL
    var generalNotes by remember { mutableStateOf(conditions.notes) }
    var professionalValidationNotes by remember { mutableStateOf(conditions.professionalValidationNotes) }
    var isProfessionalValidated by remember { mutableStateOf(conditions.isProfessionalValidated) }

    val sleepOptions = listOf(
        "Muito bom (7-9h ininterruptas)",
        "Regular (6-7h, acorda 1x)",
        "Ruim (insônia ou dor ao dormir)",
        "Fragmentado (acorda várias vezes)"
    )

    val activityOptions = listOf(
        "Sedentário (pouco ou nenhum movimento)",
        "Levemente ativo (caminhadas 1-2x/sem)",
        "Moderadamente ativo (exercícios 3x/sem)",
        "Muito ativo (musculação/funcional regular)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .testTag("anamnese_screen")
    ) {
        TopBarWithBack(
            title = "Anamnese Gerontológica",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Header Badge / Subtítulo
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = FitLime.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.35f)),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "Avaliação Clínica e Funcional 60+",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FitLimeDark,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            // AVISO METODOLÓGICO CRÍTICO
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = BorderStroke(1.5.dp, Color(0xFFF59E0B)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF59E0B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = "Aviso Clínico",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Apoio à Decisão Profissional",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF92400E)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "A presença de uma condição ou diagnóstico não gera automaticamente um treino pré-fabricado. O sistema apoia o profissional de Educação Física e Fisioterapia para validar, ajustar volume e individualizar cada prescrição com segurança.",
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp,
                            color = Color(0xFF78350F)
                        )
                    }
                }
            }

            // ========================================================
            // BLOCO 1: SAÚDE E HISTÓRICO CLÍNICO
            // ========================================================
            BlockHeader(
                icon = Icons.Default.MedicalServices,
                title = "1. Bloco Saúde",
                subtitle = "Histórico clínico, fármacos e eventos"
            )

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Diagnósticos médicos
                    OutlinedTextField(
                        value = medicalDiagnoses,
                        onValueChange = { medicalDiagnoses = it },
                        label = { Text("Diagnósticos médicos") },
                        placeholder = { Text("Ex: Hipertensão essencial, artrose bilateral...") },
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("anamnese_diagnoses_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cirurgias
                    OutlinedTextField(
                        value = surgeries,
                        onValueChange = { surgeries = it },
                        label = { Text("Cirurgias pregressas") },
                        placeholder = { Text("Ex: Artroplastia de quadril, catarata, hérnia...") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("anamnese_surgeries_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Internações
                    OutlinedTextField(
                        value = hospitalizations,
                        onValueChange = { hospitalizations = it },
                        label = { Text("Internações (últimos 12-24 meses)") },
                        placeholder = { Text("Ex: Nenhuma internação recente / Pneumonia em 2024...") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("anamnese_hospitalizations_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Medicamentos em uso
                    OutlinedTextField(
                        value = medications,
                        onValueChange = { medications = it },
                        label = { Text("Medicamentos em uso") },
                        placeholder = { Text("Ex: Anti-hipertensivo, estatina, anticoagulante, insulina...") },
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("anamnese_medications_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Alergias
                    OutlinedTextField(
                        value = allergies,
                        onValueChange = { allergies = it },
                        label = { Text("Alergias conhecidas") },
                        placeholder = { Text("Ex: Medicamentos, esparadrapo, látex, alimentos...") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("anamnese_allergies_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Histórico de quedas
                    OutlinedTextField(
                        value = fallHistory,
                        onValueChange = { fallHistory = it },
                        label = { Text("Histórico de quedas (últimos 12 meses)") },
                        placeholder = { Text("Ex: Quantas quedas, local, circunstância e se houve fratura...") },
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("anamnese_fall_history_input")
                    )
                }
            }

            // ========================================================
            // BLOCO 2: DOR E SINTOMATOLOGIA
            // ========================================================
            BlockHeader(
                icon = Icons.Default.Healing,
                title = "2. Bloco Dor",
                subtitle = "Local anatômico e escala analógica de intensidade"
            )

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Localização da dor
                    OutlinedTextField(
                        value = painLocation,
                        onValueChange = { painLocation = it },
                        label = { Text("Localização da dor") },
                        placeholder = { Text("Ex: Joelho direito, lombar, ombro esquerdo...") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("anamnese_pain_location_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Intensidade da dor (Escala EVA 0 a 10)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Intensidade da dor (Escala EVA):",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                painIntensity.toInt() == 0 -> Color(0xFF10B981)
                                painIntensity.toInt() <= 3 -> Color(0xFF84CC16)
                                painIntensity.toInt() <= 6 -> Color(0xFFF59E0B)
                                else -> Color(0xFFEF4444)
                            }
                        ) {
                            Text(
                                text = "${painIntensity.toInt()} / 10",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Slider(
                        value = painIntensity,
                        onValueChange = { painIntensity = it },
                        valueRange = 0f..10f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = FitLimeDark,
                            activeTrackColor = FitLimeDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                            .testTag("anamnese_pain_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0: Sem dor", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("5: Dor moderada", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("10: Pior dor", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // ========================================================
            // BLOCO 3: ESTILO DE VIDA (SONO & NÍVEL DE ATIVIDADE)
            // ========================================================
            BlockHeader(
                icon = Icons.Default.Bedtime,
                title = "3. Bloco Sono & Atividade Física",
                subtitle = "Hábitos cotidianos e prontidão motora"
            )

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Sono
                    Text(
                        text = "Qualidade do Sono:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    sleepOptions.forEach { opt ->
                        val isSelected = sleepQuality == opt
                        FilterChip(
                            selected = isSelected,
                            onClick = { sleepQuality = opt },
                            label = { Text(opt, fontSize = 13.sp) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FitLime.copy(alpha = 0.25f),
                                selectedLabelColor = FitLimeDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Nível de atividade física
                    Text(
                        text = "Nível de atividade física atual:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    activityOptions.forEach { opt ->
                        val isSelected = physicalActivityLevel == opt
                        FilterChip(
                            selected = isSelected,
                            onClick = { physicalActivityLevel = opt },
                            label = { Text(opt, fontSize = 13.sp) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FitLime.copy(alpha = 0.25f),
                                selectedLabelColor = FitLimeDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        )
                    }
                }
            }

            // ========================================================
            // BLOCO 4: DOENÇAS / COMORBIDADES GERONTOLÓGICAS (17 ITENS)
            // ========================================================
            BlockHeader(
                icon = Icons.Default.Favorite,
                title = "4. Bloco Doenças & Comorbidades",
                subtitle = "Condições crônicas e gerontológicas (17 parâmetros)"
            )

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                    Text(
                        text = "Selecione as condições presentes no aluno:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    // 1. Hipertensão arterial
                    GerontologicalCheckboxRow(
                        label = "Hipertensão arterial",
                        checked = hypertension,
                        onCheckedChange = { hypertension = it }
                    )
                    // 2. Diabetes tipo 2
                    GerontologicalCheckboxRow(
                        label = "Diabetes tipo 2",
                        checked = type2Diabetes,
                        onCheckedChange = { type2Diabetes = it }
                    )
                    // 3. Obesidade
                    GerontologicalCheckboxRow(
                        label = "Obesidade",
                        checked = obesity,
                        onCheckedChange = { obesity = it }
                    )
                    // 4. Osteopenia
                    GerontologicalCheckboxRow(
                        label = "Osteopenia",
                        checked = osteopenia,
                        onCheckedChange = { osteopenia = it }
                    )
                    // 5. Osteoporose
                    GerontologicalCheckboxRow(
                        label = "Osteoporose",
                        checked = osteoporosis,
                        onCheckedChange = { osteoporosis = it }
                    )
                    // 6. Artrose
                    GerontologicalCheckboxRow(
                        label = "Artrose",
                        checked = arthrosis,
                        onCheckedChange = { arthrosis = it }
                    )
                    // 7. Artrite
                    GerontologicalCheckboxRow(
                        label = "Artrite",
                        checked = arthritis,
                        onCheckedChange = { arthritis = it }
                    )
                    // 8. Lombalgia
                    GerontologicalCheckboxRow(
                        label = "Lombalgia",
                        checked = lowerBackPain,
                        onCheckedChange = { lowerBackPain = it }
                    )
                    // 9. Doenças cardiovasculares
                    GerontologicalCheckboxRow(
                        label = "Doenças cardiovasculares",
                        checked = cardiovascularDiseases,
                        onCheckedChange = { cardiovascularDiseases = it }
                    )
                    // 10. Doenças respiratórias
                    GerontologicalCheckboxRow(
                        label = "Doenças respiratórias (DPOC, asma, etc.)",
                        checked = respiratoryDiseases,
                        onCheckedChange = { respiratoryDiseases = it }
                    )
                    // 11. Parkinson
                    GerontologicalCheckboxRow(
                        label = "Parkinson",
                        checked = parkinson,
                        onCheckedChange = { parkinson = it }
                    )
                    // 12. AVC / sequelas neurológicas
                    GerontologicalCheckboxRow(
                        label = "AVC / sequelas neurológicas",
                        checked = strokeSequelae,
                        onCheckedChange = { strokeSequelae = it }
                    )
                    // 13. Sarcopenia
                    GerontologicalCheckboxRow(
                        label = "Sarcopenia",
                        checked = sarcopenia,
                        onCheckedChange = { sarcopenia = it }
                    )
                    // 14. Fragilidade
                    GerontologicalCheckboxRow(
                        label = "Fragilidade (Síndrome de Fragilidade)",
                        checked = frailty,
                        onCheckedChange = { frailty = it }
                    )
                    // 15. Alterações de equilíbrio
                    GerontologicalCheckboxRow(
                        label = "Alterações de equilíbrio",
                        checked = balanceChanges,
                        onCheckedChange = { balanceChanges = it }
                    )
                    // 16. Risco de quedas
                    GerontologicalCheckboxRow(
                        label = "Risco de quedas (elevado/moderado)",
                        checked = fallRisk,
                        onCheckedChange = { fallRisk = it }
                    )
                    // 17. Limitações de mobilidade
                    GerontologicalCheckboxRow(
                        label = "Limitações de mobilidade (marcha/transferência)",
                        checked = mobilityLimitations,
                        onCheckedChange = { mobilityLimitations = it }
                    )
                }
            }

            // ========================================================
            // BLOCO 5: OBSERVAÇÕES & VALIDAÇÃO DO PROFISSIONAL
            // ========================================================
            BlockHeader(
                icon = Icons.Default.VerifiedUser,
                title = "5. Apoio e Validação do Profissional",
                subtitle = "Personalização da prescrição baseada nas comorbidades"
            )

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, FitLimeDark.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Switch de validação
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Validação Técnica do Profissional",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Confirmação de que o profissional revisou os dados antes de prescrever.",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isProfessionalValidated,
                            onCheckedChange = { isProfessionalValidated = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = FitLimeDark
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Parecer técnico do profissional
                    OutlinedTextField(
                        value = professionalValidationNotes,
                        onValueChange = { professionalValidationNotes = it },
                        label = { Text("Parecer / Conduta do Profissional Responsável") },
                        placeholder = { Text("Descreva as adaptações: evitar cargas axiais, suporte de apoio, intervalos estendidos...") },
                        minLines = 3,
                        maxLines = 6,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("anamnese_professional_notes_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Observações gerais adicionais
                    OutlinedTextField(
                        value = generalNotes,
                        onValueChange = { generalNotes = it },
                        label = { Text("Observações gerais adicionais") },
                        placeholder = { Text("Preferências do aluno, histórico desportivo, comentários...") },
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("anamnese_general_notes_input")
                    )
                }
            }

            // BOTÃO AVANÇAR
            PrimaryFitButton(
                text = "Salvar Anamnese e Ir para Avaliação",
                onClick = {
                    val updatedConditions = conditions.copy(
                        medicalDiagnoses = medicalDiagnoses,
                        surgeries = surgeries,
                        hospitalizations = hospitalizations,
                        medications = medications,
                        allergies = allergies,
                        fallHistory = fallHistory,
                        painLocation = painLocation,
                        painIntensity = painIntensity.toInt(),
                        sleepQuality = sleepQuality,
                        physicalActivityLevel = physicalActivityLevel,
                        hypertension = hypertension,
                        type2Diabetes = type2Diabetes,
                        obesity = obesity,
                        osteopenia = osteopenia,
                        osteoporosis = osteoporosis,
                        arthrosis = arthrosis,
                        arthritis = arthritis,
                        lowerBackPain = lowerBackPain,
                        cardiovascularDiseases = cardiovascularDiseases,
                        respiratoryDiseases = respiratoryDiseases,
                        parkinson = parkinson,
                        strokeSequelae = strokeSequelae,
                        sarcopenia = sarcopenia,
                        frailty = frailty,
                        balanceChanges = balanceChanges,
                        fallRisk = fallRisk,
                        mobilityLimitations = mobilityLimitations,
                        notes = generalNotes,
                        professionalValidationNotes = professionalValidationNotes,
                        isProfessionalValidated = isProfessionalValidated,
                        // retrocompatibilidade
                        diabetes = type2Diabetes,
                        heartConditions = cardiovascularDiseases,
                        arthrosisArthritis = arthrosis || arthritis
                    )
                    onNextClick(updatedConditions)
                    Toast.makeText(context, "Anamnese gerontológica salva com sucesso!", Toast.LENGTH_SHORT).show()
                },
                testTag = "anamnese_save_button"
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun BlockHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(FitLimeDark),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun GerontologicalCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 14.dp, vertical = 5.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = FitLimeDark,
                checkmarkColor = Color.White
            )
        )
        Text(
            text = label,
            fontSize = 14.5.sp,
            fontWeight = if (checked) FontWeight.SemiBold else FontWeight.Normal,
            color = if (checked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}
