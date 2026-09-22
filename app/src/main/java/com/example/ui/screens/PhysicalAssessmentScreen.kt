package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AssessmentComparison
import com.example.data.PhysicalAssessment
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark
import java.util.Locale

@Composable
fun PhysicalAssessmentScreen(
    assessment: PhysicalAssessment,
    history: List<PhysicalAssessment> = emptyList(),
    comparison: AssessmentComparison? = null,
    onBackClick: () -> Unit,
    onSaveClick: (PhysicalAssessment) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Obrigatórios & IMC", "Perimetria & Dobras", "Funcional & Sinais", "Goniometria & AGA", "Comparativo")

    // Metadados
    var evalDate by remember { mutableStateOf(assessment.date) }
    var professional by remember { mutableStateOf(assessment.responsibleProfessional) }

    // 1. Obrigatórios
    var weight by remember { mutableStateOf(assessment.weightKg) }
    var heightCm by remember { mutableStateOf(assessment.heightCm) }

    // 2. Opcionais: Perimetria
    var arm by remember { mutableStateOf(assessment.armCircumferenceCm) }
    var waist by remember { mutableStateOf(assessment.waistCircumferenceCm) }
    var abdomen by remember { mutableStateOf(assessment.abdomenCircumferenceCm) }
    var hip by remember { mutableStateOf(assessment.hipCircumferenceCm) }
    var thigh by remember { mutableStateOf(assessment.thighCircumferenceCm) }
    var calf by remember { mutableStateOf(assessment.calfCircumferenceCm) }

    // 3. Opcionais: Dobras cutâneas
    var triceps by remember { mutableStateOf(assessment.tricepsSkinfoldMm) }
    var subscapular by remember { mutableStateOf(assessment.subscapularSkinfoldMm) }
    var iliacCrest by remember { mutableStateOf(assessment.iliacCrestSkinfoldMm) }

    // 4. Opcionais: Testes e Sinais Vitais
    var systolic by remember { mutableStateOf(assessment.systolicPressureMmHg) }
    var diastolic by remember { mutableStateOf(assessment.diastolicPressureMmHg) }
    var heartRate by remember { mutableStateOf(assessment.heartRateBpm.toString()) }
    var oxygenSat by remember { mutableStateOf(assessment.oxygenSaturation.toString()) }
    var handgrip by remember { mutableStateOf(assessment.handgripStrengthKgf) }
    var gait3m by remember { mutableStateOf(assessment.gaitSpeed3mSeconds) }
    var flexibility by remember { mutableStateOf(assessment.flexibilityCm) }

    // 5. Opcionais: Goniometria, Equilíbrio & AGA
    var goniometry by remember { mutableStateOf(assessment.goniometryNotes) }
    var balanceTest by remember { mutableStateOf(assessment.balanceTestResult) }
    var agaScore by remember { mutableStateOf(assessment.agaScore) }
    var professionalNotes by remember { mutableStateOf(assessment.professionalNotes) }

    var showNewEvalDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Cálculos automáticos reativos
    val calculatedBmi by remember(weight, heightCm) {
        derivedStateOf {
            val w = weight.replace(",", ".").toDoubleOrNull() ?: 0.0
            val h = heightCm.replace(",", ".").toDoubleOrNull() ?: 0.0
            if (w > 0 && h > 0) {
                val hM = if (h > 40) h / 100.0 else h
                w / (hM * hM)
            } else 0.0
        }
    }

    val bmiClassification by remember(calculatedBmi) {
        derivedStateOf {
            when {
                calculatedBmi <= 0.0 -> "Informe peso e altura válidos"
                calculatedBmi < 22.0 -> "Baixo peso (Risco de desnutrição / sarcopenia)"
                calculatedBmi <= 27.0 -> "Eutrófico / Peso adequado (Critério Lipschitz 60+)"
                calculatedBmi <= 30.0 -> "Sobrepeso (Atenção às sobrecargas articulares)"
                else -> "Obesidade (Risco cardiovascular e osteoarticular)"
            }
        }
    }

    // Evolução do peso comparado com a última avaliação anterior
    val previousEval = remember(history, assessment.id) {
        history.firstOrNull { it.id != assessment.id }
    }

    val weightDeltaKg by remember(weight, previousEval) {
        derivedStateOf {
            val curW = weight.replace(",", ".").toDoubleOrNull()
            val prevW = previousEval?.weightKg?.replace(",", ".")?.toDoubleOrNull()
            if (curW != null && prevW != null) curW - prevW else null
        }
    }

    val weightDeltaPercent by remember(weightDeltaKg, previousEval) {
        derivedStateOf {
            val prevW = previousEval?.weightKg?.replace(",", ".")?.toDoubleOrNull()
            if (weightDeltaKg != null && prevW != null && prevW > 0) {
                (weightDeltaKg!! / prevW) * 100.0
            } else null
        }
    }

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

        // Banner de Identificação da Avaliação (Data + Profissional Responsável)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = FitLimeDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Avaliação: $evalDate",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = professional,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }

                TextButton(
                    onClick = { showNewEvalDialog = true },
                    modifier = Modifier.testTag("btn_new_assessment")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = FitLimeDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Nova", color = FitLimeDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Abas de Navegação das Seções da Avaliação
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = FitLimeDark,
                    height = 3.dp
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (selectedTab == index) FitLimeDark else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        // Conteúdo da Aba Selecionada
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (selectedTab) {
                0 -> {
                    // ==========================================
                    // ABA 0: OBRIGATÓRIOS & CÁLCULOS AUTOMÁTICOS
                    // ==========================================
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = FitLime.copy(alpha = 0.12f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = FitLimeDark,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Campos com asterisco (*) são obrigatórios. O aplicativo calcula automaticamente o IMC, classificação geriátrica e evolução do peso.",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Campos Obrigatórios
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedAssessmentField(
                                label = "Peso (kg) *",
                                value = weight,
                                onValueChange = { weight = it },
                                keyboardType = KeyboardType.Decimal,
                                modifier = Modifier.weight(1f),
                                testTag = "input_weight"
                            )

                            OutlinedAssessmentField(
                                label = "Altura (cm) *",
                                value = heightCm,
                                onValueChange = { heightCm = it },
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f),
                                testTag = "input_height"
                            )
                        }

                        // Card: CÁLCULOS AUTOMÁTICOS
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, FitLimeDark.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Cálculo Automático",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(FitLime.copy(alpha = 0.2f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Em tempo real",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FitLimeDark
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // IMC Display
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Índice de Massa Corporal (IMC)",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = if (calculatedBmi > 0) String.format(Locale.US, "%.1f kg/m²", calculatedBmi) else "--",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (calculatedBmi in 22.0..27.0) FitLimeDark else Color(0xFFD97706)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (calculatedBmi in 22.0..27.0) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = if (calculatedBmi in 22.0..27.0) "✓ Adequado (60+)" else "Atenção",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (calculatedBmi in 22.0..27.0) Color(0xFF166534) else Color(0xFF92400E)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Classificação detalhada do IMC
                                Text(
                                    text = "Classificação: $bmiClassification",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "Referência geriátrica OPAS/Lipschitz: a faixa ideal para idosos é de 22 a 27 kg/m² para proteção contra sarcopenia e perda óssea.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 15.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                                // Evolução do Peso
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Evolução do Peso",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (previousEval != null && weightDeltaKg != null) {
                                            val sign = if (weightDeltaKg!! >= 0) "+" else ""
                                            val percentStr = String.format(Locale.US, "%.1f%%", weightDeltaPercent ?: 0.0)
                                            Text(
                                                text = "Variação de ${sign}${String.format(Locale.US, "%.1f", weightDeltaKg)} kg ($percentStr) vs ${previousEval.date}",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else {
                                            Text(
                                                text = "Avaliação de referência inicial (baseline).",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    if (weightDeltaKg != null) {
                                        val icon = when {
                                            weightDeltaKg!! < -0.2 -> Icons.Default.ArrowDownward
                                            weightDeltaKg!! > 0.2 -> Icons.Default.ArrowUpward
                                            else -> Icons.Default.TrendingFlat
                                        }
                                        val badgeColor = when {
                                            kotlin.math.abs(weightDeltaKg!!) <= 1.0 -> FitLimeDark
                                            weightDeltaKg!! < -0.2 -> Color(0xFF2563EB)
                                            else -> Color(0xFFD97706)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(badgeColor.copy(alpha = 0.15f))
                                                .padding(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = badgeColor,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // ==========================================
                    // ABA 1: PERIMETRIA & DOBRAS CUTÂNEAS
                    // ==========================================
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionHeader(title = "Perimetria (Circunferências em cm)", subtitle = "Medidas com fita métrica inextensível")

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedAssessmentField(label = "Braço (cm)", value = arm, onValueChange = { arm = it }, modifier = Modifier.weight(1f))
                            OutlinedAssessmentField(label = "Cintura (cm)", value = waist, onValueChange = { waist = it }, modifier = Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedAssessmentField(label = "Abdômen (cm)", value = abdomen, onValueChange = { abdomen = it }, modifier = Modifier.weight(1f))
                            OutlinedAssessmentField(label = "Quadril (cm)", value = hip, onValueChange = { hip = it }, modifier = Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedAssessmentField(label = "Coxa (cm)", value = thigh, onValueChange = { thigh = it }, modifier = Modifier.weight(1f))
                            OutlinedAssessmentField(label = "Panturrilha (cm)", value = calf, onValueChange = { calf = it }, modifier = Modifier.weight(1f))
                        }

                        // Alerta gerontológico de Panturrilha
                        val calfNum = calf.replace(",", ".").toDoubleOrNull()
                        if (calfNum != null) {
                            val isCalfOk = calfNum >= 31.0
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isCalfOk) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isCalfOk) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (isCalfOk) Color(0xFF166534) else Color(0xFFB91C1C),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isCalfOk) "Panturrilha ≥ 31 cm: Massa muscular preservada (baixo risco de sarcopenia)."
                                        else "Panturrilha < 31 cm: Alerta para sarcopenia e perda de massa muscular esquelética.",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isCalfOk) Color(0xFF166534) else Color(0xFF991B1B)
                                    )
                                }
                            }
                        }

                        // Relação Cintura / Quadril (RCQ)
                        val wVal = waist.replace(",", ".").toDoubleOrNull()
                        val hVal = hip.replace(",", ".").toDoubleOrNull()
                        if (wVal != null && hVal != null && hVal > 0) {
                            val rcq = wVal / hVal
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Relação Cintura-Quadril (RCQ):", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    Text(String.format(Locale.US, "%.2f", rcq), fontWeight = FontWeight.Black, fontSize = 15.sp, color = FitLimeDark)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        SectionHeader(title = "Dobras Cutâneas (mm)", subtitle = "Aferição com plicômetro / adipômetro")

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedAssessmentField(label = "Tríceps (mm)", value = triceps, onValueChange = { triceps = it }, modifier = Modifier.weight(1f))
                            OutlinedAssessmentField(label = "Subescapular (mm)", value = subscapular, onValueChange = { subscapular = it }, modifier = Modifier.weight(1f))
                        }

                        OutlinedAssessmentField(
                            label = "Crista ilíaca (mm)",
                            value = iliacCrest,
                            onValueChange = { iliacCrest = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                2 -> {
                    // ==========================================
                    // ABA 2: FUNCIONAL & SINAIS VITAIS
                    // ==========================================
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionHeader(title = "Pressão Arterial & Hemodinâmica", subtitle = "Aferição de segurança antes e durante a prescrição")

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedAssessmentField(
                                label = "Sistólica (mmHg)",
                                value = systolic,
                                onValueChange = { systolic = it },
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedAssessmentField(
                                label = "Diastólica (mmHg)",
                                value = diastolic,
                                onValueChange = { diastolic = it },
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedAssessmentField(
                                label = "Frequência Cardíaca (bpm)",
                                value = heartRate,
                                onValueChange = { heartRate = it },
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedAssessmentField(
                                label = "Saturação SpO₂ (%)",
                                value = oxygenSat,
                                onValueChange = { oxygenSat = it },
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        SectionHeader(title = "Testes Funcionais Gerontológicos", subtitle = "Força de preensão, velocidade e flexibilidade")

                        // Dinamometria / Pressão Palmar
                        OutlinedAssessmentField(
                            label = "Pressão palmar / Força de preensão (kgf)",
                            value = handgrip,
                            onValueChange = { handgrip = it },
                            keyboardType = KeyboardType.Decimal,
                            modifier = Modifier.fillMaxWidth()
                        )
                        val gripVal = handgrip.replace(",", ".").toDoubleOrNull()
                        if (gripVal != null) {
                            val isGripPreserved = gripVal >= 16.0 // corte feminino EWGSOP2
                            Text(
                                text = if (isGripPreserved) "✓ Força palmar preservada (≥ 16 kgf para mulheres 60+)."
                                else "⚠ Força palmar reduzida (< 16 kgf): indício de provável sarcopenia.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isGripPreserved) Color(0xFF166534) else Color(0xFFB91C1C)
                            )
                        }

                        // Marcha 3 Metros
                        OutlinedAssessmentField(
                            label = "Marcha 3 metros (tempo em segundos)",
                            value = gait3m,
                            onValueChange = { gait3m = it },
                            keyboardType = KeyboardType.Decimal,
                            modifier = Modifier.fillMaxWidth()
                        )
                        val gaitVal = gait3m.replace(",", ".").toDoubleOrNull()
                        if (gaitVal != null && gaitVal > 0) {
                            val speed = 3.0 / gaitVal
                            val isSpeedOk = speed >= 0.8
                            Text(
                                text = "Velocidade estimada: ${String.format(Locale.US, "%.2f", speed)} m/s (${if (isSpeedOk) "Adequada / Baixo risco de fragilidade" else "Lenta / Alerta para risco de quedas"}).",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSpeedOk) Color(0xFF166534) else Color(0xFFB91C1C)
                            )
                        }

                        // Flexibilidade
                        OutlinedAssessmentField(
                            label = "Flexibilidade (cm no banco de Wells ou amplitude)",
                            value = flexibility,
                            onValueChange = { flexibility = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                3 -> {
                    // ==========================================
                    // ABA 3: GONIOMETRIA, EQUILÍBRIO & AGA
                    // ==========================================
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionHeader(title = "Goniometria Articular", subtitle = "Amplitude de movimento (graus de flexão e extensão)")
                        OutlinedAssessmentField(
                            label = "Goniometria (graus articulares)",
                            value = goniometry,
                            onValueChange = { goniometry = it },
                            singleLine = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        SectionHeader(title = "Teste de Equilíbrio", subtitle = "Apoio unipodal, teste de Tandem e estabilidade postural")
                        OutlinedAssessmentField(
                            label = "Resultado do teste de equilíbrio",
                            value = balanceTest,
                            onValueChange = { balanceTest = it },
                            singleLine = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        SectionHeader(title = "Teste AGA (Avaliação Geriátrica Ampla)", subtitle = "Autonomia funcional (ABVD/AIVD), cognição e nutrição")
                        OutlinedAssessmentField(
                            label = "Pontuação e síntese do Teste AGA (opcional)",
                            value = agaScore,
                            onValueChange = { agaScore = it },
                            singleLine = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        SectionHeader(title = "Parecer e Observações Profissionais", subtitle = "Diretrizes de personalização do treino")
                        OutlinedAssessmentField(
                            label = "Observações clínicas do profissional",
                            value = professionalNotes,
                            onValueChange = { professionalNotes = it },
                            singleLine = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                4 -> {
                    // ==========================================
                    // ABA 4: COMPARATIVO COM AVALIAÇÕES ANTERIORES
                    // ==========================================
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = FitLime.copy(alpha = 0.12f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.CompareArrows, contentDescription = null, tint = FitLimeDark, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Comparação longitudinal: permite acompanhar os ganhos de força muscular, velocidade de marcha e estabilidade corporal ao longo do tempo.",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Comparação Lado a Lado (Atual vs Anterior)
                        if (previousEval != null) {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, FitLimeDark.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Atual vs Anterior",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "$evalDate vs ${previousEval.date}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FitLimeDark
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    ComparisonRow(
                                        metric = "Peso Corporal",
                                        current = "$weight kg",
                                        previous = "${previousEval.weightKg} kg",
                                        delta = if (weightDeltaKg != null) "${if (weightDeltaKg!! >= 0) "+" else ""}${String.format(Locale.US, "%.1f", weightDeltaKg)} kg" else null
                                    )

                                    ComparisonRow(
                                        metric = "IMC",
                                        current = if (calculatedBmi > 0) String.format(Locale.US, "%.1f", calculatedBmi) else "--",
                                        previous = String.format(Locale.US, "%.1f", previousEval.calculateBmi()),
                                        delta = null
                                    )

                                    ComparisonRow(
                                        metric = "Circ. Panturrilha",
                                        current = "$calf cm",
                                        previous = "${previousEval.calfCircumferenceCm} cm",
                                        delta = null
                                    )

                                    ComparisonRow(
                                        metric = "Força Palmar (Dinamometria)",
                                        current = "$handgrip kgf",
                                        previous = "${previousEval.handgripStrengthKgf} kgf",
                                        delta = null
                                    )

                                    ComparisonRow(
                                        metric = "Marcha 3 metros",
                                        current = "$gait3m s",
                                        previous = "${previousEval.gaitSpeed3mSeconds} s",
                                        delta = null
                                    )

                                    ComparisonRow(
                                        metric = "Pressão Arterial",
                                        current = "$systolic/$diastolic mmHg",
                                        previous = previousEval.bloodPressure,
                                        delta = null
                                    )

                                    ComparisonRow(
                                        metric = "Profissional Responsável",
                                        current = professional,
                                        previous = previousEval.responsibleProfessional,
                                        delta = null
                                    )
                                }
                            }
                        }

                        // Histórico Completo de Avaliações
                        Text(
                            text = "Histórico de Avaliações Registradas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        history.forEach { item ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // Carrega a avaliação selecionada
                                        evalDate = item.date
                                        professional = item.responsibleProfessional
                                        weight = item.weightKg
                                        heightCm = item.heightCm
                                        arm = item.armCircumferenceCm
                                        waist = item.waistCircumferenceCm
                                        abdomen = item.abdomenCircumferenceCm
                                        hip = item.hipCircumferenceCm
                                        thigh = item.thighCircumferenceCm
                                        calf = item.calfCircumferenceCm
                                        triceps = item.tricepsSkinfoldMm
                                        subscapular = item.subscapularSkinfoldMm
                                        iliacCrest = item.iliacCrestSkinfoldMm
                                        systolic = item.systolicPressureMmHg
                                        diastolic = item.diastolicPressureMmHg
                                        heartRate = item.heartRateBpm.toString()
                                        oxygenSat = item.oxygenSaturation.toString()
                                        handgrip = item.handgripStrengthKgf
                                        gait3m = item.gaitSpeed3mSeconds
                                        flexibility = item.flexibilityCm
                                        goniometry = item.goniometryNotes
                                        balanceTest = item.balanceTestResult
                                        agaScore = item.agaScore
                                        professionalNotes = item.professionalNotes
                                        selectedTab = 0
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = item.date,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            if (item.id == assessment.id) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(FitLime.copy(alpha = 0.2f))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text("Ativa", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FitLimeDark)
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = item.responsibleProfessional,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Peso: ${item.weightKg} kg • IMC: ${String.format(Locale.US, "%.1f", item.calculateBmi())} • Força: ${item.handgripStrengthKgf} kgf • Marcha: ${item.gaitSpeed3mSeconds}s",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = "Carregar avaliação",
                                        tint = FitLimeDark,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão Principal de Salvar Avaliação
            PrimaryFitButton(
                text = "Salvar Avaliação Física",
                onClick = {
                    val updated = assessment.copy(
                        date = evalDate,
                        responsibleProfessional = professional,
                        weightKg = weight,
                        heightCm = heightCm,
                        heightM = if ((heightCm.toDoubleOrNull() ?: 0.0) > 40) {
                            String.format(Locale.US, "%.2f", (heightCm.toDoubleOrNull() ?: 158.0) / 100.0)
                        } else heightCm,
                        armCircumferenceCm = arm,
                        waistCircumferenceCm = waist,
                        abdomenCircumferenceCm = abdomen,
                        hipCircumferenceCm = hip,
                        thighCircumferenceCm = thigh,
                        calfCircumferenceCm = calf,
                        tricepsSkinfoldMm = triceps,
                        subscapularSkinfoldMm = subscapular,
                        iliacCrestSkinfoldMm = iliacCrest,
                        systolicPressureMmHg = systolic,
                        diastolicPressureMmHg = diastolic,
                        bloodPressure = "$systolic / $diastolic mmHg",
                        heartRateBpm = heartRate.toIntOrNull() ?: 72,
                        oxygenSaturation = oxygenSat.toIntOrNull() ?: 98,
                        handgripStrengthKgf = handgrip,
                        gaitSpeed3mSeconds = gait3m,
                        flexibilityCm = flexibility,
                        goniometryNotes = goniometry,
                        balanceTestResult = balanceTest,
                        agaScore = agaScore,
                        professionalNotes = professionalNotes
                    )
                    onSaveClick(updated)
                },
                testTag = "assessment_save_button"
            )
        }
    }

    // Modal para Registrar Nova Avaliação (Data e Profissional)
    if (showNewEvalDialog) {
        var newDate by remember { mutableStateOf("22/09/2026") }
        var newProf by remember { mutableStateOf(professional) }

        AlertDialog(
            onDismissRequest = { showNewEvalDialog = false },
            title = {
                Text("Registrar Nova Avaliação", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Inicie uma nova ficha de avaliação física com data e profissional responsável para permitir a comparação evolutiva.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = newDate,
                        onValueChange = { newDate = it },
                        label = { Text("Data da avaliação") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newProf,
                        onValueChange = { newProf = it },
                        label = { Text("Profissional responsável") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        evalDate = newDate
                        professional = newProf
                        showNewEvalDialog = false
                    }
                ) {
                    Text("Criar", fontWeight = FontWeight.Bold, color = FitLimeDark)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewEvalDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ComparisonRow(metric: String, current: String, previous: String, delta: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(text = metric, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (delta != null) {
                Text(text = delta, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FitLimeDark)
            }
        }
        Text(
            text = current,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = previous,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}

@Composable
private fun OutlinedAssessmentField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier,
    testTag: String? = null
) {
    var mod = modifier
    if (testTag != null) {
        mod = mod.testTag(testTag)
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 13.sp) },
        singleLine = singleLine,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FitLimeDark,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
        ),
        modifier = mod
    )
}
