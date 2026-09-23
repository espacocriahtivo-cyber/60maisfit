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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PrescriptionExercise
import com.example.data.ProfessionalStudent
import com.example.data.WorkoutPrescription
import com.example.data.WorkoutSequence
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPrescriptionScreen(
    initialPrescription: WorkoutPrescription,
    students: List<ProfessionalStudent> = emptyList(),
    onBackClick: () -> Unit,
    onSavePrescription: (WorkoutPrescription) -> Unit,
    onPreviewWorkout: () -> Unit,
    onOpenVideoLibrary: () -> Unit = {},
    onOpenConditionWorkouts: () -> Unit = {}
) {
    val context = LocalContext.current
    var prescriptionState by remember { mutableStateOf(initialPrescription) }
    var selectedStudentId by remember { mutableStateOf(initialPrescription.studentId) }
    var selectedWorkoutCode by remember { mutableStateOf(initialPrescription.workoutCode) }
    var selectedSequenceFilter by remember { mutableIntStateOf(0) } // 0 = Todas, 1 a 6
    var expandedSequences by remember { mutableStateOf(setOf(1, 2, 3, 4, 5, 6)) }

    // Dialog para adicionar novo exercício em sequência
    var showAddExerciseDialog by remember { mutableStateOf<Int?>(null) }
    var newExerciseName by remember { mutableStateOf("") }
    var newExerciseReps by remember { mutableStateOf("10 a 12 reps") }
    var newExerciseIntensity by remember { mutableStateOf("Peso corporal") }
    var newExerciseNotes by remember { mutableStateOf("") }

    // Dialog de PDF / Relatório
    var showPdfDialog by remember { mutableStateOf(false) }

    // Estatísticas calculadas
    val allExercises = prescriptionState.sequences.flatMap { it.exercises }
    val selectedExercises = allExercises.filter { it.isSelected }
    val totalEstimatedMinutes = (selectedExercises.size * 2.2).toInt().coerceAtLeast(15)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .testTag("workout_prescription_screen")
    ) {
        TopBarWithBack(
            title = "Prescrição de Treino",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header do Profissional & Seleção de Aluno
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, FitLime.copy(alpha = 0.8f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(FitLime.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("👩‍⚕️", fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Painel de Prescrição Clínica 60+",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = prescriptionState.professionalName,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = FitLime.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "6 SEQUÊNCIAS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = FitLimeDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "SELECIONE O ALUNO PARA PRESCRIÇÃO:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Seletor de alunos
                        val studentList = if (students.isNotEmpty()) students else listOf(
                            ProfessionalStudent("s1", "Maria Silva", 68, "60+fit Gerontológico", "Hoje", 92),
                            ProfessionalStudent("s2", "José de Alencar", 72, "60+fit Essencial", "Ontem", 85),
                            ProfessionalStudent("s3", "Antônia Santos", 65, "60+fit Gerontológico", "12/09", 78),
                            ProfessionalStudent("s4", "Carlos Eduardo", 70, "60+fit Personal", "10/09", 95)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(studentList) { student ->
                                val isSelected = student.id == selectedStudentId
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) FitLimeDark else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) FitLimeDark else MaterialTheme.colorScheme.outlineVariant
                                    ),
                                    modifier = Modifier
                                        .clickable {
                                            selectedStudentId = student.id
                                            prescriptionState = prescriptionState.copy(
                                                studentId = student.id,
                                                studentName = student.name
                                            )
                                        }
                                        .testTag("student_chip_${student.id}")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = if (student.id == "s1" || student.id == "s3") "👵" else "🧓",
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${student.name} (${student.age}a)",
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Seletor de Código do Treino (Treino A, B, C)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Divisão do Treino:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("Treino A", "Treino B", "Treino C").forEach { code ->
                                    val isCurrentCode = selectedWorkoutCode == code
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isCurrentCode) FitLime.copy(alpha = 0.25f) else Color.Transparent,
                                        border = BorderStroke(
                                            1.5.dp,
                                            if (isCurrentCode) FitLimeDark else MaterialTheme.colorScheme.outlineVariant
                                        ),
                                        modifier = Modifier.clickable {
                                            selectedWorkoutCode = code
                                            prescriptionState = prescriptionState.copy(workoutCode = code)
                                        }
                                    ) {
                                        Text(
                                            text = code,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCurrentCode) FitLimeDark else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Barra de Resumo de Prescrição
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Exercícios Selecionados",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${selectedExercises.size} de ${allExercises.size} prescritos",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = FitLimeDark
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Tempo Estimado",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "~$totalEstimatedMinutes min de sessão",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Chips Horizontais para Filtrar ou Saltar entre Sequências 1 a 6
            item {
                Text(
                    text = "SEQUÊNCIAS DE TREINO (1 A 6):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterSequenceChip(
                            label = "Todas",
                            isSelected = selectedSequenceFilter == 0,
                            onClick = { selectedSequenceFilter = 0 }
                        )
                    }
                    items(prescriptionState.sequences) { seq ->
                        FilterSequenceChip(
                            label = "${seq.emoji} Seq. ${seq.id}",
                            isSelected = selectedSequenceFilter == seq.id,
                            onClick = { selectedSequenceFilter = seq.id }
                        )
                    }
                }
            }

            // Lista das 6 Sequências
            val displayedSequences = if (selectedSequenceFilter == 0) {
                prescriptionState.sequences
            } else {
                prescriptionState.sequences.filter { it.id == selectedSequenceFilter }
            }

            items(displayedSequences) { sequence ->
                val isExpanded = expandedSequences.contains(sequence.id)
                val seqSelectedCount = sequence.exercises.count { it.isSelected }

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        1.dp,
                        if (seqSelectedCount > 0) FitLime.copy(alpha = 0.9f) else MaterialTheme.colorScheme.outlineVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_sequence_${sequence.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Top row of Sequence Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedSequences = if (isExpanded) {
                                        expandedSequences - sequence.id
                                    } else {
                                        expandedSequences + sequence.id
                                    }
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(FitLime.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = sequence.emoji, fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = sequence.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = sequence.subtitle,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (seqSelectedCount > 0) FitLimeDark else MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = "$seqSelectedCount/${sequence.exercises.size}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (seqSelectedCount > 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Conteúdo expansível da Sequência
                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                // Ações rápidas da Sequência (Marcar Todos / Desmarcar / + Exercício)
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            text = "Selecionar todos",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FitLimeDark,
                                            modifier = Modifier
                                                .clickable {
                                                    val updatedSeq = sequence.copy(
                                                        exercises = sequence.exercises.map { it.copy(isSelected = true) }
                                                    )
                                                    prescriptionState = prescriptionState.copy(
                                                        sequences = prescriptionState.sequences.map {
                                                            if (it.id == sequence.id) updatedSeq else it
                                                        }
                                                    )
                                                }
                                                .padding(vertical = 4.dp)
                                        )
                                        Text(
                                            text = "•",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "Desmarcar todos",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier
                                                .clickable {
                                                    val updatedSeq = sequence.copy(
                                                        exercises = sequence.exercises.map { it.copy(isSelected = false) }
                                                    )
                                                    prescriptionState = prescriptionState.copy(
                                                        sequences = prescriptionState.sequences.map {
                                                            if (it.id == sequence.id) updatedSeq else it
                                                        }
                                                    )
                                                }
                                                .padding(vertical = 4.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = FitLime.copy(alpha = 0.15f),
                                        border = BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.4f)),
                                        modifier = Modifier.clickable {
                                            showAddExerciseDialog = sequence.id
                                            newExerciseName = ""
                                            newExerciseReps = "10 a 12 reps"
                                            newExerciseIntensity = "Peso corporal"
                                            newExerciseNotes = ""
                                        }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null,
                                                tint = FitLimeDark,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "Adicionar",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = FitLimeDark
                                            )
                                        }
                                    }
                                }

                                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(8.dp))

                                // Lista de Exercícios da Sequência
                                sequence.exercises.forEach { exercise ->
                                    PrescriptionExerciseItem(
                                        exercise = exercise,
                                        onToggleSelection = { isSelected ->
                                            val updatedExercises = sequence.exercises.map {
                                                if (it.id == exercise.id) it.copy(isSelected = isSelected) else it
                                            }
                                            prescriptionState = prescriptionState.copy(
                                                sequences = prescriptionState.sequences.map {
                                                    if (it.id == sequence.id) sequence.copy(exercises = updatedExercises) else it
                                                }
                                            )
                                        },
                                        onUpdateExercise = { updated ->
                                            val updatedExercises = sequence.exercises.map {
                                                if (it.id == exercise.id) updated else it
                                            }
                                            prescriptionState = prescriptionState.copy(
                                                sequences = prescriptionState.sequences.map {
                                                    if (it.id == sequence.id) sequence.copy(exercises = updatedExercises) else it
                                                }
                                            )
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Bloco de Ações e Finalização
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Ações Clínicas do Profissional:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        PrimaryFitButton(
                            text = "Salvar e Prescrever para ${prescriptionState.studentName}",
                            leadingIcon = Icons.Default.CheckCircle,
                            onClick = {
                                onSavePrescription(prescriptionState)
                                Toast.makeText(
                                    context,
                                    "Prescrição salva! Treino atualizado para ${prescriptionState.studentName} com ${selectedExercises.size} exercícios.",
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            testTag = "btn_save_prescription"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onPreviewWorkout() }
                                    .testTag("btn_preview_student_workout")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Ver Treino (Tela 9)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showPdfDialog = true }
                                    .testTag("btn_export_prescription_pdf")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = null,
                                        tint = FitLimeDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Ficha em PDF",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FitLimeDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = onOpenVideoLibrary,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.5.dp, FitLimeDark),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_prescription_open_video_library")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = FitLimeDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Consultar Biblioteca de Vídeos (Tela 7)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = FitLimeDark
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = onOpenConditionWorkouts,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.5.dp, FitLimeDark),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_prescription_open_condition_workouts")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = FitLimeDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🎯 Treinos por Condição • Treino Direcionado (Tela 8)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = FitLimeDark
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botão de restaurar padrão gerontológico
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    prescriptionState = initialPrescription
                                    Toast.makeText(context, "Prescrição restaurada para o padrão recomendado.", Toast.LENGTH_SHORT).show()
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Restaurar sugestão gerontológica padrão",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Modal para adicionar novo exercício em sequência
    if (showAddExerciseDialog != null) {
        val targetSeqId = showAddExerciseDialog!!
        val targetSeq = prescriptionState.sequences.firstOrNull { it.id == targetSeqId }

        AlertDialog(
            onDismissRequest = { showAddExerciseDialog = null },
            title = {
                Text(
                    text = "Adicionar Exercício em ${targetSeq?.title ?: "Sequência"}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newExerciseName,
                        onValueChange = { newExerciseName = it },
                        label = { Text("Nome do Exercício") },
                        placeholder = { Text("Ex: Rotação externa de ombro") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newExerciseReps,
                        onValueChange = { newExerciseReps = it },
                        label = { Text("Séries e Repetições / Duração") },
                        placeholder = { Text("Ex: 2 séries de 10 reps") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newExerciseIntensity,
                        onValueChange = { newExerciseIntensity = it },
                        label = { Text("Carga / Apoio / Intensidade") },
                        placeholder = { Text("Ex: Caneleira 1kg / Com cadeira") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newExerciseNotes,
                        onValueChange = { newExerciseNotes = it },
                        label = { Text("Observação Clínica 60+ (Opcional)") },
                        placeholder = { Text("Ex: Manter alinhamento e respirar") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newExerciseName.isNotBlank()) {
                            val newEx = PrescriptionExercise(
                                id = "custom_${System.currentTimeMillis()}",
                                name = newExerciseName.trim().lowercase(),
                                sequenceId = targetSeqId,
                                isSelected = true,
                                sets = 2,
                                repsOrTime = newExerciseReps.trim(),
                                loadOrIntensity = newExerciseIntensity.trim(),
                                restSeconds = 45,
                                notes = newExerciseNotes.trim()
                            )
                            val updatedSequences = prescriptionState.sequences.map { seq ->
                                if (seq.id == targetSeqId) {
                                    seq.copy(exercises = seq.exercises + newEx)
                                } else seq
                            }
                            prescriptionState = prescriptionState.copy(sequences = updatedSequences)
                            showAddExerciseDialog = null
                            Toast.makeText(context, "Exercício adicionado!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Adicionar", fontWeight = FontWeight.Bold, color = FitLimeDark)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddExerciseDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Modal de Exportação PDF
    if (showPdfDialog) {
        AlertDialog(
            onDismissRequest = { showPdfDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null, tint = FitLimeDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ficha Clínica de Prescrição", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Documento oficial gerado com sucesso:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("• Aluno: ${prescriptionState.studentName}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("• Treino: ${prescriptionState.workoutCode}", fontSize = 12.sp)
                            Text("• Profissional: ${prescriptionState.professionalName}", fontSize = 12.sp)
                            Text("• Total de Exercícios: ${selectedExercises.size}", fontSize = 12.sp)
                            Text("• Tempo de Sessão: ~$totalEstimatedMinutes min", fontSize = 12.sp)
                            Text("• Protocolo: 6 Sequências Gerontológicas 60+fit", fontSize = 12.sp)
                        }
                    }
                    Text(
                        text = "O PDF pode ser impresso para a sala de musculação ou compartilhado diretamente via WhatsApp/e-mail com o aluno.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPdfDialog = false
                        Toast.makeText(context, "Ficha de treino em PDF enviada para download!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Baixar PDF", fontWeight = FontWeight.Bold, color = FitLimeDark)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPdfDialog = false }) {
                    Text("Fechar")
                }
            }
        )
    }
}

@Composable
fun FilterSequenceChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) FitLimeDark else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            if (isSelected) FitLimeDark else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun PrescriptionExerciseItem(
    exercise: PrescriptionExercise,
    onToggleSelection: (Boolean) -> Unit,
    onUpdateExercise: (PrescriptionExercise) -> Unit
) {
    var isExpandedDetails by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (exercise.isSelected) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.dp,
            if (exercise.isSelected) FitLimeDark.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = exercise.isSelected,
                        onCheckedChange = { onToggleSelection(it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = FitLimeDark,
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = exercise.name.replaceFirstChar { it.uppercase() },
                            fontSize = 13.sp,
                            fontWeight = if (exercise.isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (exercise.isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${exercise.sets} séries • ${exercise.repsOrTime} • ${exercise.loadOrIntensity}",
                            fontSize = 11.sp,
                            color = if (exercise.isSelected) FitLimeDark else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = { isExpandedDetails = !isExpandedDetails },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isExpandedDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Ajustar parâmetros",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Detalhes editáveis se o profissional abrir
            AnimatedVisibility(visible = isExpandedDetails) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                ) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Séries & Descanso
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Séries
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Séries", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf(1, 2, 3, 4).forEach { s ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (exercise.sets == s) FitLimeDark else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { onUpdateExercise(exercise.copy(sets = s)) }
                                    ) {
                                        Text(
                                            text = "$s",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (exercise.sets == s) Color.White else MaterialTheme.colorScheme.onSurface,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Descanso
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Descanso", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf(30 to "30s", 45 to "45s", 60 to "60s").forEach { (sec, label) ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (exercise.restSeconds == sec) FitLimeDark else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { onUpdateExercise(exercise.copy(restSeconds = sec)) }
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (exercise.restSeconds == sec) Color.White else MaterialTheme.colorScheme.onSurface,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Repetições e Carga
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = exercise.repsOrTime,
                            onValueChange = { onUpdateExercise(exercise.copy(repsOrTime = it)) },
                            label = { Text("Reps / Tempo", fontSize = 10.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = exercise.loadOrIntensity,
                            onValueChange = { onUpdateExercise(exercise.copy(loadOrIntensity = it)) },
                            label = { Text("Carga / Apoio", fontSize = 10.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Observação Clínica
                    OutlinedTextField(
                        value = exercise.notes,
                        onValueChange = { onUpdateExercise(exercise.copy(notes = it)) },
                        label = { Text("Observação Clínica 60+ (Adaptação, postura, segurança)", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
