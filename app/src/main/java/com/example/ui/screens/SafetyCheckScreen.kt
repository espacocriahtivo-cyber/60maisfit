package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PreWorkoutSymptom
import com.example.data.SafetyCheckRecord
import com.example.data.StudentProfile
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SafetyCheckScreen(
    symptoms: List<PreWorkoutSymptom>,
    records: List<SafetyCheckRecord>,
    studentProfile: StudentProfile,
    onBackClick: () -> Unit,
    onProceedToWorkout: () -> Unit,
    onSaveRecord: (SafetyCheckRecord) -> Unit
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Check-in, 1: Histórico

    // Estado do Check-in
    var selectedSymptomIds by remember { mutableStateOf(setOf<String>()) }
    var feltAllClear by remember { mutableStateOf(false) }
    var inputBloodPressure by remember { mutableStateOf("120/80") }
    var inputHeartRate by remember { mutableStateOf("72") }
    var inputNotes by remember { mutableStateOf("") }
    var showEvaluationResult by remember { mutableStateOf(false) }
    var showNotifyProfessionalDialog by remember { mutableStateOf(false) }

    val hasSymptoms = selectedSymptomIds.isNotEmpty()

    Scaffold(
        modifier = Modifier.testTag("safety_check_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // Barra Superior
            TopBarWithBack(
                title = "Sistema de Segurança",
                onBackClick = onBackClick
            )

            // Abas de navegação interna
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = FitLimeDark,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = FitLimeDark,
                        height = 3.dp
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.HealthAndSafety, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Atenção antes do treino", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Histórico de Registros", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                )
            }

            if (selectedTabIndex == 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // 1. Banner da Área: "Atenção antes do treino"
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.5.dp, Color(0xFFEF4444).copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFFFEF2F2),
                                                MaterialTheme.colorScheme.surface
                                            )
                                        )
                                    )
                                    .padding(18.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFDC2626),
                                            contentColor = Color.White
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Shield,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = Color.White
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "PROTOCOLO DE SEGURANÇA",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.surface,
                                            border = BorderStroke(1.dp, Color(0xFFDC2626).copy(alpha = 0.3f))
                                        ) {
                                            Text(
                                                text = "60+ Protegido",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFDC2626),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = "Atenção antes do treino",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Text(
                                        text = "Antes de iniciar qualquer atividade física, informe com sinceridade como está se sentindo. Caso apresente qualquer sinal de alerta, a sessão será interrompida preventivamente.",
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFFFFBEB),
                                        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = null,
                                                tint = Color(0xFFB45309),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = "O aplicativo orienta condutas e zela pela sua integridade física, sem realizar diagnósticos médicos.",
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp,
                                                color = Color(0xFF92400E),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Se já avaliou e há resultado a exibir
                    if (showEvaluationResult) {
                        item {
                            if (hasSymptoms) {
                                // 🛑 INTERRUPÇÃO DA SESSÃO
                                SessionInterruptedCard(
                                    symptomsReported = symptoms.filter { selectedSymptomIds.contains(it.id) },
                                    studentProfile = studentProfile,
                                    onCallEmergency = {
                                        try {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:192"))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Discagem para SAMU (192)", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onCallContact = {
                                        try {
                                            val cleanPhone = studentProfile.emergencyContact.filter { it.isDigit() }
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Discando contato de emergência...", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onNotifyProfessional = {
                                        showNotifyProfessionalDialog = true
                                    },
                                    onResetCheck = {
                                        showEvaluationResult = false
                                        selectedSymptomIds = emptySet()
                                        feltAllClear = false
                                    }
                                )
                            } else {
                                // 🟢 SINAL VERDE
                                SessionClearedCard(
                                    bloodPressure = inputBloodPressure,
                                    heartRate = inputHeartRate,
                                    notes = inputNotes,
                                    onProceedToWorkout = {
                                        val now = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(Date())
                                        val record = SafetyCheckRecord(
                                            dateFormatted = now,
                                            studentName = studentProfile.name,
                                            isCleared = true,
                                            reportedSymptoms = emptyList(),
                                            bloodPressure = inputBloodPressure,
                                            heartRate = inputHeartRate,
                                            studentNotes = inputNotes,
                                            recommendationText = "Aluno liberado para o treino. Sem sinais impeditivos relatados."
                                        )
                                        onSaveRecord(record)
                                        Toast.makeText(context, "✅ Checagem concluída com sucesso! Bom treino!", Toast.LENGTH_SHORT).show()
                                        onProceedToWorkout()
                                    },
                                    onRecheck = {
                                        showEvaluationResult = false
                                    }
                                )
                            }
                        }
                    } else {
                        // 2. Opção Rápida: "Estou me sentindo ótimo(a) e sem sintomas hoje"
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (feltAllClear) FitLime.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(
                                    1.5.dp,
                                    if (feltAllClear) FitLimeDark else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        feltAllClear = !feltAllClear
                                        if (feltAllClear) {
                                            selectedSymptomIds = emptySet()
                                        }
                                    }
                                    .testTag("btn_feeling_all_clear")
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (feltAllClear) FitLimeDark else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = if (feltAllClear) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Estou me sentindo bem e sem sintomas hoje",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Sem tonturas, sem dores no peito, sem falta de ar e sem quedas recentes.",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Checkbox(
                                        checked = feltAllClear,
                                        onCheckedChange = { checked ->
                                            feltAllClear = checked
                                            if (checked) {
                                                selectedSymptomIds = emptySet()
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = FitLimeDark)
                                    )
                                }
                            }
                        }

                        // 3. Título da Lista de Sintomas
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "OU SELECIONE O QUE ESTÁ SENTINDO HOJE:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 0.5.sp
                                )

                                if (selectedSymptomIds.isNotEmpty()) {
                                    Text(
                                        text = "${selectedSymptomIds.size} selecionado(s)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFDC2626)
                                    )
                                }
                            }
                        }

                        // 4. Os 7 Sintomas Solicitados no Briefing
                        items(symptoms, key = { it.id }) { symptom ->
                            val isChecked = selectedSymptomIds.contains(symptom.id)

                            SymptomCheckCard(
                                symptom = symptom,
                                isChecked = isChecked,
                                onToggle = {
                                    feltAllClear = false
                                    selectedSymptomIds = if (isChecked) {
                                        selectedSymptomIds - symptom.id
                                    } else {
                                        selectedSymptomIds + symptom.id
                                    }
                                }
                            )
                        }

                        // 5. Parâmetros Fisiológicos Adicionais (Opcionais)
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "Medições complementares de hoje (opcional):",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = inputBloodPressure,
                                            onValueChange = { inputBloodPressure = it },
                                            label = { Text("Pressão (mmHg)") },
                                            placeholder = { Text("ex: 120/80") },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = FitLimeDark
                                            )
                                        )

                                        OutlinedTextField(
                                            value = inputHeartRate,
                                            onValueChange = { inputHeartRate = it },
                                            label = { Text("FC (bpm)") },
                                            placeholder = { Text("ex: 72") },
                                            singleLine = true,
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = FitLimeDark
                                            )
                                        )
                                    }

                                    OutlinedTextField(
                                        value = inputNotes,
                                        onValueChange = { inputNotes = it },
                                        label = { Text("Observação ou como está seu dia") },
                                        placeholder = { Text("ex: 'Dormi 8 horas', 'Tomei os remédios no horário'") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = FitLimeDark
                                        )
                                    )
                                }
                            }
                        }

                        // 6. Botão de Avaliar Segurança
                        item {
                            Button(
                                onClick = {
                                    showEvaluationResult = true
                                    val now = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(Date())
                                    val record = SafetyCheckRecord(
                                        dateFormatted = now,
                                        studentName = studentProfile.name,
                                        isCleared = !hasSymptoms,
                                        reportedSymptoms = symptoms.filter { selectedSymptomIds.contains(it.id) }.map { it.title },
                                        bloodPressure = inputBloodPressure,
                                        heartRate = inputHeartRate,
                                        studentNotes = inputNotes,
                                        recommendationText = if (hasSymptoms) {
                                            "Treino suspenso por precaução clínica devido a: ${selectedSymptomIds.joinToString()}."
                                        } else {
                                            "Aluno em perfeitas condições para realização do treino programado."
                                        }
                                    )
                                    onSaveRecord(record)
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (hasSymptoms) Color(0xFFDC2626) else FitLimeDark,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("btn_confirm_safety_evaluation")
                            ) {
                                Icon(
                                    imageVector = if (hasSymptoms) Icons.Default.Warning else Icons.Default.Shield,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (hasSymptoms) "Verificar Alertas de Segurança (Sintomas Marcados)" else "Confirmar Checagem de Segurança",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            } else {
                // Aba 2: Histórico de Registros de Segurança
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "REGISTRO DE CHECAGENS ANTERIORES (${records.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 0.5.sp
                        )
                    }

                    if (records.isEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("📋", fontSize = 36.sp)
                                    Text(
                                        text = "Nenhum registro anterior encontrado",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Os check-ins pré-treino realizados aparecerão salvos aqui.",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        items(records, key = { it.id }) { record ->
                            SafetyRecordItemCard(record = record)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    // Diálogo de Confirmação de Aviso ao Instrutor
    if (showNotifyProfessionalDialog) {
        AlertDialog(
            onDismissRequest = { showNotifyProfessionalDialog = false },
            icon = {
                Icon(Icons.Default.Send, contentDescription = null, tint = FitLimeDark, modifier = Modifier.size(28.dp))
            },
            title = {
                Text(
                    text = "Avisar Instrutor / Profissional",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Uma mensagem oficial de interrupção de treino será encaminhada para ${studentProfile.responsibleProfessional}:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\"Olá professor(a). A aluna ${studentProfile.name} registrou sintomas (${selectedSymptomIds.joinToString()}) no check-in pré-treino de hoje. A sessão foi interrompida preventivamente pelo sistema 60+ FIT.\"",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(10.dp),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showNotifyProfessionalDialog = false
                        Toast.makeText(context, "✅ Notificação enviada para ${studentProfile.responsibleProfessional}!", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FitLimeDark)
                ) {
                    Text("Enviar Notificação")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotifyProfessionalDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Card individual de Sintoma para seleção
 */
@Composable
fun SymptomCheckCard(
    symptom: PreWorkoutSymptom,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isChecked) Color(0xFFDC2626) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
        label = "borderColor"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(if (isChecked) 1.5.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .testTag("symptom_card_${symptom.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isChecked) Color(0xFFDC2626) else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = symptom.emoji, fontSize = 20.sp)
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = symptom.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isChecked) Color(0xFF991B1B) else MaterialTheme.colorScheme.onSurface
                    )

                    if (symptom.isEmergency) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFDC2626),
                            contentColor = Color.White
                        ) {
                            Text(
                                text = "Alerta Alto",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = symptom.subtitle,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (isChecked) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFEE2E2),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Atenção: ${symptom.clinicalRiskNote}",
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = Color(0xFF991B1B),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFDC2626)
                )
            )
        }
    }
}

/**
 * Card exibido quando a sessão é interrompida por presença de sintomas
 */
@Composable
fun SessionInterruptedCard(
    symptomsReported: List<PreWorkoutSymptom>,
    studentProfile: StudentProfile,
    onCallEmergency: () -> Unit,
    onCallContact: () -> Unit,
    onNotifyProfessional: () -> Unit,
    onResetCheck: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
        border = BorderStroke(2.dp, Color(0xFFDC2626)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("session_interrupted_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFDC2626),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = "🛑 SESSÃO INTERROMPIDA",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF991B1B)
                    )
                    Text(
                        text = "Segurança em primeiro lugar",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB91C1C)
                    )
                }
            }

            Text(
                text = "Por precaução e zelo à sua saúde, o treino de hoje foi suspenso. O 60+ FIT não realiza diagnóstico médico, mas na presença dos sintomas assinalados, o esforço físico deve ser evitado antes de avaliação presencial.",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = Color(0xFF7F1D1D)
            )

            // Lista dos sintomas informados
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Sintomas relatados por você:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B)
                    )

                    symptomsReported.forEach { s ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(s.emoji, fontSize = 14.sp)
                            Text(
                                text = s.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937)
                            )
                        }
                    }
                }
            }

            // O que fazer agora
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFFBEB),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Orientações imediatas recomendadas:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                    Text("• Sente-se em uma cadeira firme com as costas apoiadas.", fontSize = 12.sp, color = Color(0xFF78350F))
                    Text("• Beba um copo de água e respire compassadamente.", fontSize = 12.sp, color = Color(0xFF78350F))
                    Text("• Não faça movimentos bruscos ou deite de estômago cheio.", fontSize = 12.sp, color = Color(0xFF78350F))
                    Text("• Caso os sintomas persistam ou se agravem, acione o serviço de emergência imediatamente.", fontSize = 12.sp, color = Color(0xFF78350F))
                }
            }

            // Ações de Emergência e Comunicação
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // 1. Ligar SAMU 192
                Button(
                    onClick = onCallEmergency,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("btn_call_samu_192")
                ) {
                    Icon(Icons.Default.PhoneInTalk, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ligar para SAMU (192)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                // 2. Contato de Emergência Cadastrado
                OutlinedButton(
                    onClick = onCallContact,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFDC2626)),
                    modifier = Modifier.fillMaxWidth().testTag("btn_call_emergency_contact")
                ) {
                    Icon(Icons.Default.ContactPhone, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ligar Contato: ${studentProfile.emergencyContact}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFFDC2626)
                    )
                }

                // 3. Notificar Instrutor
                Button(
                    onClick = onNotifyProfessional,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLimeDark),
                    modifier = Modifier.fillMaxWidth().testTag("btn_notify_professional")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Avisar Instrutor (${studentProfile.responsibleProfessional})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // 4. Refazer Checagem
                TextButton(
                    onClick = onResetCheck,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Refazer Checagem (se marcou por engano)", fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * Card exibido quando o aluno está sem sintomas impeditivos
 */
@Composable
fun SessionClearedCard(
    bloodPressure: String,
    heartRate: String,
    notes: String,
    onProceedToWorkout: () -> Unit,
    onRecheck: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(2.dp, FitLimeDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("session_cleared_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = FitLimeDark,
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = "SINAL VERDE PARA O TREINO 🟢",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = FitLimeDark
                    )
                    Text(
                        text = "Você está apto(a) para a sessão de hoje!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "Nenhum sintoma de risco imediato foi relatado. Lembre-se de respeitar seus limites, beber água nos intervalos e respirar de forma contínua durante as repetições.",
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Pressão", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(bloodPressure.ifEmpty { "Normal" }, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Divider(modifier = Modifier.height(24.dp).width(1.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Frequência", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${heartRate.ifEmpty { "72" }} bpm", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Divider(modifier = Modifier.height(24.dp).width(1.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Status", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Liberado", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FitLimeDark)
                    }
                }
            }

            Button(
                onClick = onProceedToWorkout,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FitLimeDark,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_proceed_to_workout")
            ) {
                Icon(Icons.Default.FitnessCenter, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciar Treino de Hoje", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }

            TextButton(
                onClick = onRecheck,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Alterar Respostas", fontSize = 12.sp)
            }
        }
    }
}

/**
 * Item individual no Histórico de Checagens
 */
@Composable
fun SafetyRecordItemCard(record: SafetyCheckRecord) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            1.dp,
            if (record.isCleared) FitLimeDark.copy(alpha = 0.4f) else Color(0xFFDC2626).copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (record.isCleared) FitLimeDark else Color(0xFFDC2626),
                    contentColor = Color.White
                ) {
                    Text(
                        text = if (record.isCleared) "🟢 LIBERADO" else "🛑 INTERROMPIDO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = record.dateFormatted,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!record.isCleared && record.reportedSymptoms.isNotEmpty()) {
                Text(
                    text = "Sintomas: ${record.reportedSymptoms.joinToString(", ")}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDC2626)
                )
            }

            Text(
                text = record.recommendationText,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (record.bloodPressure.isNotEmpty() || record.heartRate.isNotEmpty()) {
                Text(
                    text = "Sinais: PA ${record.bloodPressure} • FC ${record.heartRate}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
