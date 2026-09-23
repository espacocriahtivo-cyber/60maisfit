package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EvolutionMetricCategory
import com.example.data.EvolutionMetricItem
import com.example.data.EvolutionTrophy
import com.example.data.WeightHistoryPoint
import com.example.ui.components.StudentBottomNavBar
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark
import com.example.ui.theme.GoldTrophy

@Composable
fun EvolutionScreen(
    metrics: List<EvolutionMetricItem> = emptyList(),
    trophies: List<EvolutionTrophy> = emptyList(),
    history: List<WeightHistoryPoint> = emptyList(),
    currentWeight: String = "62.0",
    goalWeight: String = "58 a 62 kg",
    studentName: String = "Maria Silva",
    onBackClick: () -> Unit,
    onNavigateBottom: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(EvolutionMetricCategory.ALL) }
    var expandedMetricId by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    val filteredMetrics = remember(selectedCategory, metrics) {
        if (selectedCategory == EvolutionMetricCategory.ALL) {
            metrics
        } else {
            metrics.filter { it.category == selectedCategory }
        }
    }

    // Determine chart dataset based on selection
    val activeMetricForChart = remember(selectedCategory, metrics) {
        if (selectedCategory == EvolutionMetricCategory.ALL) {
            metrics.find { it.category == EvolutionMetricCategory.STRENGTH }
                ?: metrics.firstOrNull()
        } else {
            metrics.find { it.category == selectedCategory } ?: metrics.firstOrNull()
        }
    }

    Scaffold(
        bottomBar = {
            StudentBottomNavBar(
                currentRoute = "evolution",
                onNavigate = onNavigateBottom
            )
        },
        modifier = Modifier.testTag("evolution_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            TopBarWithBack(
                title = "Minha Evolução",
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==========================================
                // HERO BANNER: "Sua evolução desde a primeira avaliação"
                // ==========================================
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(2.dp, FitLimeDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hero_evolution_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = FitLimeDark,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = "📈 EVOLUÇÃO LONGITUDINAL",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = FitLime.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, FitLimeDark)
                            ) {
                                Text(
                                    text = "8 Meses Ativos",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FitLimeDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Sua evolução desde a primeira avaliação",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 26.sp,
                                modifier = Modifier.testTag("hero_evolution_title")
                            )
                            Text(
                                text = "Acompanhe seus ganhos clínicos de autonomia desde o primeiro dia (Jan/2026) até a sua avaliação atual (Set/2026).",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }

                        // Highlights Row / Quick Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            EvolutionHighlightBadge(
                                emoji = "💪",
                                label = "Força",
                                delta = "+40%",
                                isPositive = true,
                                modifier = Modifier.weight(1f)
                            )
                            EvolutionHighlightBadge(
                                emoji = "✊",
                                label = "Preensão",
                                delta = "+28.6%",
                                isPositive = true,
                                modifier = Modifier.weight(1f)
                            )
                            EvolutionHighlightBadge(
                                emoji = "⚖️",
                                label = "Equilíbrio",
                                delta = "+133%",
                                isPositive = true,
                                modifier = Modifier.weight(1f)
                            )
                            EvolutionHighlightBadge(
                                emoji = "🚶",
                                label = "Marcha",
                                delta = "+32%",
                                isPositive = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Clinical Insight Callout
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = FitLime.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.35f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("✨", fontSize = 22.sp)
                                Text(
                                    text = "Parabéns, $studentName! Você reduziu o risco de quedas pela metade, ganhou massa muscular protetora na panturrilha e manteve 92% de adesão aos treinos.",
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // ==========================================
                // HORIZONTAL CHIP SELECTOR (9 MÉTRICAS + TODAS)
                // ==========================================
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Acompanhar por categoria (9 pilares de evolução):",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EvolutionMetricCategory.entries.forEach { category ->
                            val isSelected = selectedCategory == category
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = category },
                                label = {
                                    Text(
                                        text = "${category.emoji} ${category.title}",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FitLimeDark,
                                    selectedLabelColor = Color.White,
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) FitLimeDark else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    selectedBorderColor = FitLimeDark
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("evolution_chip_${category.name.lowercase()}")
                            )
                        }
                    }
                }

                // ==========================================
                // INTERACTIVE PROGRESSION CHART
                // ==========================================
                if (activeMetricForChart != null && activeMetricForChart.historyPoints.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth().testTag("evolution_chart_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Gráfico de Evolução • ${activeMetricForChart.category.title}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${activeMetricForChart.title} (${activeMetricForChart.unit})",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFDCFCE7),
                                    border = BorderStroke(1.dp, Color(0xFF86EFAC))
                                ) {
                                    Text(
                                        text = activeMetricForChart.deltaPercentage,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF166534),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Custom Line Chart for the active metric
                            MetricHistoryChart(
                                points = activeMetricForChart.historyPoints,
                                unit = activeMetricForChart.unit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )
                        }
                    }
                }

                // ==========================================
                // DETAILED METRICS LIST (ALL 9 TRACKABLE ITEMS)
                // ==========================================
                Text(
                    text = if (selectedCategory == EvolutionMetricCategory.ALL)
                        "Todas as 9 métricas de evolução:"
                    else
                        "Métrica selecionada:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                filteredMetrics.forEach { metric ->
                    val isExpanded = expandedMetricId == metric.id

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.2.dp, if (isExpanded) FitLimeDark else MaterialTheme.colorScheme.outline),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedMetricId = if (isExpanded) null else metric.id
                            }
                            .testTag("metric_card_${metric.id}")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Card Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(FitLime.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(metric.category.emoji, fontSize = 20.sp)
                                    }

                                    Column {
                                        Text(
                                            text = metric.title,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = metric.category.title,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = FitLimeDark
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (metric.isPositiveImprovement) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                                    border = BorderStroke(1.dp, if (metric.isPositiveImprovement) Color(0xFF86EFAC) else Color(0xFFFCA5A5))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                            contentDescription = null,
                                            tint = if (metric.isPositiveImprovement) Color(0xFF166534) else Color(0xFF991B1B),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = metric.deltaValue,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (metric.isPositiveImprovement) Color(0xFF166534) else Color(0xFF991B1B)
                                        )
                                    }
                                }
                            }

                            // Comparison: 1ª Avaliação vs Avaliação Atual
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "1ª Avaliação (${metric.firstAssessmentDate})",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = metric.firstAssessmentValue,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = FitLimeDark,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .size(18.dp)
                                )

                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "Atual (${metric.currentAssessmentDate})",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FitLimeDark
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = metric.currentAssessmentValue,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = FitLimeDark
                                    )
                                }
                            }

                            // Practical Benefit for daily life
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("🎯", fontSize = 14.sp)
                                Text(
                                    text = metric.practicalBenefitForElderly,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Expandable Clinical Interpretation
                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = FitLimeDark,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Interpretação Clínica Especializada:",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FitLimeDark
                                        )
                                    }
                                    Text(
                                        text = metric.clinicalInterpretation,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp,
                                        color = Color(0xFF334155)
                                    )
                                }
                            }

                            // Click hint
                            Text(
                                text = if (isExpanded) "Toque para recolher detalhes clínicos ▲" else "Toque para ver a análise clínica completa ▼",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = FitLimeDark,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                // ==========================================
                // TROPHIES & ACHIEVEMENTS SECTION
                // ==========================================
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = FitLime.copy(alpha = 0.12f)),
                    border = BorderStroke(1.5.dp, FitLimeDark.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth().testTag("evolution_trophies_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(GoldTrophy.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = GoldTrophy,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "Conquistas de Autonomia",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = FitLimeDark
                                )
                                Text(
                                    text = "Marcos históricos alcançados desde o início",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        trophies.forEach { trophy ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(trophy.emoji, fontSize = 24.sp)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = trophy.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = trophy.subtitle,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = FitLime.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = trophy.dateEarned,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FitLimeDark,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // SHARE / EXPORT ACTION
                // ==========================================
                OutlinedButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Relatório completo de evolução exportado com sucesso! Compartilhe com seu médico ou instrutor.",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, FitLimeDark),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FitLimeDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("export_evolution_btn")
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Compartilhar Relatório com Médico ou Família",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun EvolutionHighlightBadge(
    emoji: String,
    label: String,
    delta: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(emoji, fontSize = 16.sp)
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = delta,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = if (isPositive) Color(0xFF166534) else Color(0xFFDC2626)
            )
        }
    }
}

@Composable
private fun MetricHistoryChart(
    points: List<Pair<String, Float>>,
    unit: String,
    modifier: Modifier = Modifier
) {
    val lineColor = FitLimeDark
    val pointColor = FitLime

    val minY = remember(points) {
        (points.minOfOrNull { it.second } ?: 0f) * 0.9f
    }
    val maxY = remember(points) {
        (points.maxOfOrNull { it.second } ?: 100f) * 1.1f
    }

    Column(modifier = modifier) {
        Canvas(modifier = Modifier.weight(1f).fillMaxWidth()) {
            val width = size.width
            val height = size.height
            val paddingLeft = 40f
            val paddingRight = 30f
            val paddingTop = 25f
            val paddingBottom = 25f

            val chartWidth = width - paddingLeft - paddingRight
            val chartHeight = height - paddingTop - paddingBottom

            val range = if (maxY - minY == 0f) 1f else (maxY - minY)

            // Horizontal grid lines
            val steps = 3
            for (i in 0..steps) {
                val y = paddingTop + (chartHeight / steps) * i
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(paddingLeft, y),
                    end = Offset(width - paddingRight, y),
                    strokeWidth = 1f
                )
            }

            if (points.isNotEmpty()) {
                val stepX = if (points.size > 1) chartWidth / (points.size - 1) else chartWidth / 2
                val offsets = points.mapIndexed { index, pair ->
                    val x = paddingLeft + index * stepX
                    val normalized = ((pair.second - minY) / range).coerceIn(0f, 1f)
                    val y = paddingTop + chartHeight * (1f - normalized)
                    Offset(x, y)
                }

                // Line Path
                val path = Path().apply {
                    moveTo(offsets.first().x, offsets.first().y)
                    for (i in 1 until offsets.size) {
                        lineTo(offsets[i].x, offsets[i].y)
                    }
                }

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 4f, cap = StrokeCap.Round)
                )

                // Circles and values
                offsets.forEachIndexed { index, offset ->
                    drawCircle(
                        color = Color.White,
                        radius = 8f,
                        center = offset
                    )
                    drawCircle(
                        color = if (index == 0) Color(0xFF64748B) else if (index == offsets.size - 1) Color(0xFF16A34A) else pointColor,
                        radius = 6f,
                        center = offset
                    )
                }
            }
        }

        // Horizontal X-axis labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            points.forEachIndexed { index, pair ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = pair.first,
                        fontSize = 11.sp,
                        fontWeight = if (index == 0 || index == points.size - 1) FontWeight.Bold else FontWeight.Medium,
                        color = if (index == 0) Color(0xFF64748B) else if (index == points.size - 1) FitLimeDark else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (pair.second % 1.0f == 0f) "${pair.second.toInt()}" else String.format(java.util.Locale.US, "%.1f", pair.second),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
