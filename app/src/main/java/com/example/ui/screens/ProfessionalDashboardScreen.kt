package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.FunctionalDimension
import com.example.data.ProfessionalStudent
import com.example.data.StudentFunctionalProfile
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.ProfessionalBottomNavBar
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark

@Composable
fun ProfessionalDashboardScreen(
    students: List<ProfessionalStudent>,
    selectedStudentId: String,
    onSelectStudent: (String) -> Unit,
    getFunctionalProfile: (String) -> StudentFunctionalProfile,
    onNavigateToAnamnese: () -> Unit,
    onNavigateToAssessment: () -> Unit,
    onNavigateToWorkout: () -> Unit,
    onNavigateToVideoLibrary: () -> Unit = {},
    onNavigateToConditionWorkouts: () -> Unit = {},
    onNavigateToSafetyCheck: () -> Unit = {},
    onBackClick: () -> Unit,
    onNavigateBottom: (String) -> Unit
) {
    val context = LocalContext.current
    val currentProfile = getFunctionalProfile(selectedStudentId)
    var selectedTabFilter by remember { mutableStateOf<String?>("TODOS") }

    Scaffold(
        bottomBar = {
            ProfessionalBottomNavBar(
                currentRoute = "professional_area",
                onNavigate = onNavigateBottom
            )
        },
        modifier = Modifier.testTag("professional_dashboard_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            TopBarWithBack(
                title = "Dashboard do Profissional",
                onBackClick = onBackClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                // 1. Cabeçalho do Profissional
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_trainer_avatar),
                                contentDescription = "Instrutora Carol",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, FitLimeDark, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Prof. Dra. Camila Rocha",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "CREF 098452-G/SP • Fisiologia Geriátrica 60+",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "4 alunos ativos • Painel de Status Funcional",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = FitLimeDark
                                )
                            }
                        }
                    }
                }

                // 2. Seletor de Alunos (Chips horizontais)
                item {
                    Column {
                        Text(
                            text = "SELECIONE O ALUNO PARA ABRIR O STATUS FUNCIONAL:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.8.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            students.forEach { student ->
                                val isSelected = student.id == selectedStudentId
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onSelectStudent(student.id) },
                                    label = {
                                        Text(
                                            text = "${student.name} (${student.age}a)",
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = FitLimeDark,
                                        selectedLabelColor = Color.White,
                                        selectedLeadingIconColor = Color.White
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = if (isSelected) FitLimeDark else MaterialTheme.colorScheme.outline
                                    )
                                )
                            }
                        }
                    }
                }

                // 3. Ficha Resumo do Aluno Aberto
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.5.dp, FitLime.copy(alpha = 0.6f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape)
                                        .border(2.5.dp, FitLimeDark, CircleShape)
                                        .background(Color(0xFFDCFCE7)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (currentProfile.studentId == "s1") "👵" else "🧓",
                                        fontSize = 32.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = currentProfile.studentName,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFDCFCE7)
                                        ) {
                                            Text(
                                                text = "Ativo",
                                                color = Color(0xFF166534),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${currentProfile.age} anos • Plano ${currentProfile.plan}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Frequência: ${currentProfile.frequency} • Avaliação: ${currentProfile.lastAssessmentDate}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )

                            // Quick Badges (Escore global e Risco de quedas)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "CAPACIDADE FUNCIONAL",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = currentProfile.overallScore,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF166534)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "RISCO DE QUEDAS",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = currentProfile.fallRiskLevel,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.weight(0.9f)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "ADESÃO",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "${currentProfile.adherencePercent}% treinos",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FitLimeDark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Seção Destaque: STATUS FUNCIONAL (7 DIMENSÕES)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Status Funcional",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "7 dimensões gerontológicas com gráficos evolutivos",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = FitLime.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "7 DIMENSÕES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = FitLimeDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // 5. Lista dos 7 Blocos de Status Funcional
                items(currentProfile.dimensions) { dimension ->
                    FunctionalDimensionCard(dimension = dimension)
                }

                // 6. Barra de Ações Clínicas Rápidas do Profissional
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Ações do Profissional para ${currentProfile.studentName}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            PrimaryFitButton(
                                text = "Prescrever Treino por Sequências (Tela 6)",
                                leadingIcon = Icons.Default.FitnessCenter,
                                onClick = onNavigateToWorkout,
                                testTag = "btn_pro_adjust_workout"
                            )

                            OutlinedButton(
                                onClick = onNavigateToConditionWorkouts,
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.5.dp, FitLimeDark),
                                modifier = Modifier.fillMaxWidth().testTag("btn_pro_condition_workouts")
                            ) {
                                Icon(Icons.Default.MedicalServices, contentDescription = null, modifier = Modifier.size(18.dp), tint = FitLimeDark)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "🎯 Treinos por Condição (Tela 8) • Treino Direcionado",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FitLimeDark
                                )
                            }

                            OutlinedButton(
                                onClick = onNavigateToSafetyCheck,
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.5.dp, Color(0xFFDC2626)),
                                modifier = Modifier.fillMaxWidth().testTag("btn_pro_safety_check")
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFFDC2626))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "🛡️ Sistema de Segurança (Tela 9) • Atenção Pré-Treino",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDC2626)
                                )
                            }

                            OutlinedButton(
                                onClick = onNavigateToVideoLibrary,
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.5.dp, FitLimeDark),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp), tint = FitLimeDark)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Biblioteca de Vídeos (Tela 7) • Filtros Clínicos",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FitLimeDark
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onNavigateToAssessment,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp), tint = FitLimeDark)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Nova Avaliação", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                }

                                OutlinedButton(
                                    onClick = onNavigateToAnamnese,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.MedicalServices, contentDescription = null, modifier = Modifier.size(16.dp), tint = FitLimeDark)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Ver Anamnese", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Laudo de evolução funcional de ${currentProfile.studentName} gerado em PDF!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = FitLimeDark)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Exportar Relatório Clínico de Evolução", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun FunctionalDimensionCard(dimension: FunctionalDimension) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_dimension_${dimension.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header da Dimensão: Ícone, Nome (FORÇA, MOBILIDADE, etc) e Selo de Evolução
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(FitLime.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = dimension.icon, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = dimension.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = dimension.primaryMetric,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Badge de Evolução (Ex: ↑ evolução)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (dimension.isPositive) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                    border = BorderStroke(
                        1.dp,
                        if (dimension.isPositive) Color(0xFF86EFAC) else Color(0xFFFDE68A)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (dimension.id == "dim_quedas") Icons.AutoMirrored.Filled.TrendingDown else Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = if (dimension.isPositive) Color(0xFF15803D) else Color(0xFFB45309),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = dimension.evolutionLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (dimension.isPositive) Color(0xFF15803D) else Color(0xFFB45309)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Caixa Comparativa Principal: Avaliação Inicial vs Atual (Como solicitado pelo usuário)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Avaliação inicial",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = dimension.initialValue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = null,
                    tint = FitLimeDark,
                    modifier = Modifier.size(24.dp)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Atual",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = dimension.currentValue,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = FitLimeDark
                    )
                }
            }

            // Métrica Secundária (Ex: Sentar e Levantar, Tandem, Cadência)
            if (dimension.secondaryMetric != null && dimension.secondaryCurrent != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${dimension.secondaryMetric}:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Inicial: ${dimension.secondaryInitial} → Atual: ${dimension.secondaryCurrent} (${dimension.secondaryEvolution})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Gráfico mostrando evolução
            if (dimension.historyPoints.isNotEmpty()) {
                Text(
                    text = "GRÁFICO DE EVOLUÇÃO LONGITUDINAL:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                FunctionalEvolutionChart(
                    points = dimension.historyPoints,
                    isPositive = dimension.isPositive,
                    isFallRisk = dimension.id == "dim_quedas"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Diretriz Clínica do Profissional
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF0FDF4),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "💡", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dimension.clinicalGuideline,
                        fontSize = 11.sp,
                        color = Color(0xFF166534),
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun FunctionalEvolutionChart(
    points: List<Pair<String, Float>>,
    isPositive: Boolean,
    isFallRisk: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0D180C))
            .padding(12.dp)
    ) {
        val lineColor = if (isFallRisk) Color(0xFF38BDF8) else FitLime
        val pointColor = Color.White

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (points.size < 2) return@Canvas

            val minVal = points.minOf { it.second }
            val maxVal = points.maxOf { it.second }
            val range = if (maxVal - minVal > 0) maxVal - minVal else 1f

            val bottomPadding = 24.dp.toPx()
            val topPadding = 16.dp.toPx()
            val availableHeight = size.height - bottomPadding - topPadding
            val stepX = size.width / (points.size - 1)

            val chartPoints = points.mapIndexed { index, pair ->
                val x = index * stepX
                val normalizedY = (pair.second - minVal) / range
                val y = size.height - bottomPadding - (normalizedY * availableHeight)
                Offset(x, y)
            }

            // Gradiente sombreado abaixo da linha
            val fillPath = Path().apply {
                moveTo(chartPoints.first().x, size.height - bottomPadding)
                chartPoints.forEach { lineTo(it.x, it.y) }
                lineTo(chartPoints.last().x, size.height - bottomPadding)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.35f), Color.Transparent),
                    startY = topPadding,
                    endY = size.height - bottomPadding
                )
            )

            // Linha da curva
            val strokePath = Path().apply {
                chartPoints.forEachIndexed { i, offset ->
                    if (i == 0) moveTo(offset.x, offset.y) else lineTo(offset.x, offset.y)
                }
            }

            drawPath(
                path = strokePath,
                color = lineColor,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Pontos circulares e rótulos
            chartPoints.forEachIndexed { index, offset ->
                drawCircle(
                    color = lineColor,
                    radius = 5.dp.toPx(),
                    center = offset
                )
                drawCircle(
                    color = pointColor,
                    radius = 2.5.dp.toPx(),
                    center = offset
                )

                // Rótulo do valor e do mês
                val valueText = points[index].second.let {
                    if (it % 1.0f == 0f) "${it.toInt()}" else String.format(java.util.Locale.US, "%.1f", it)
                }
                val labelText = points[index].first

                drawContext.canvas.nativeCanvas.apply {
                    val paintValue = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 24f
                        isFakeBoldText = true
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    val paintLabel = android.graphics.Paint().apply {
                        color = android.graphics.Color.LTGRAY
                        textSize = 22f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }

                    // Valor acima do ponto
                    drawText(valueText, offset.x, offset.y - 12f, paintValue)
                    // Mês abaixo
                    drawText(labelText, offset.x, size.height - 4f, paintLabel)
                }
            }
        }
    }
}
