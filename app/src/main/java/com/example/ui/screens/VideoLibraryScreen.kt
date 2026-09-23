package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VideoExercise
import com.example.data.VideoLibraryData
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VideoLibraryScreen(
    videos: List<VideoExercise>,
    onBackClick: () -> Unit,
    onAddExerciseToWorkout: (VideoExercise) -> Unit = {},
    onToggleFavorite: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // Estado dos Filtros
    var searchQuery by remember { mutableStateOf("") }
    var selectedObjective by remember { mutableStateOf("todos") }
    var selectedRegion by remember { mutableStateOf("todas") }
    var selectedCondition by remember { mutableStateOf("todas") }
    var selectedLevel by remember { mutableStateOf("todos") }

    var showFilterDrawer by remember { mutableStateOf(false) }
    var activeDetailModal by remember { mutableStateOf<VideoExercise?>(null) }

    // Filtro ativo contador
    val activeFiltersCount = (if (selectedObjective != "todos") 1 else 0) +
            (if (selectedRegion != "todas") 1 else 0) +
            (if (selectedCondition != "todas") 1 else 0) +
            (if (selectedLevel != "todos") 1 else 0)

    // Filtragem em tempo real
    val filteredVideos = remember(videos, searchQuery, selectedObjective, selectedRegion, selectedCondition, selectedLevel) {
        videos.filter { ex ->
            val matchQuery = searchQuery.isBlank() ||
                    ex.name.contains(searchQuery, ignoreCase = true) ||
                    ex.objective.contains(searchQuery, ignoreCase = true) ||
                    ex.instruction.contains(searchQuery, ignoreCase = true)

            val matchObj = selectedObjective == "todos" || ex.objectiveCategory.equals(selectedObjective, ignoreCase = true)
            val matchReg = selectedRegion == "todas" || ex.region.equals(selectedRegion, ignoreCase = true)
            val matchCond = selectedCondition == "todas" || ex.conditions.any { it.equals(selectedCondition, ignoreCase = true) }
            val matchLvl = selectedLevel == "todos" || ex.level.equals(selectedLevel, ignoreCase = true)

            matchQuery && matchObj && matchReg && matchCond && matchLvl
        }
    }

    Scaffold(
        modifier = Modifier.testTag("video_library_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            // Top Bar
            TopBarWithBack(
                title = "Biblioteca de Vídeos",
                onBackClick = onBackClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Banner Heroico / Diferencial
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.5.dp, FitLime.copy(alpha = 0.7f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            FitLime.copy(alpha = 0.20f),
                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
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
                                        shape = RoundedCornerShape(10.dp),
                                        color = FitLimeDark,
                                        contentColor = Color.White
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Videocam,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp),
                                                tint = Color.White
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "DIFERENCIAL 60+FIT",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surface,
                                        border = BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = "⏱️ Vídeos de 10s",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FitLimeDark,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "Acervo de Vídeos Ilustrativos",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "Cada exercício possui vídeo demonstrativo de 10 segundos com ritmo adaptado para a terceira idade, áudio-guia e filtros especializados por objetivo, região, comorbidade e nível.",
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // 2. Barra de Pesquisa e Botão Filtros Avançados
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Pesquisar exercício ou objetivo...", fontSize = 14.sp) },
                                leadingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = FitLimeDark)
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(Icons.Default.Clear, contentDescription = "Limpar")
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FitLimeDark,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_search_videos")
                            )

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (activeFiltersCount > 0) FitLimeDark else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (activeFiltersCount > 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .clickable { showFilterDrawer = !showFilterDrawer }
                                    .testTag("btn_toggle_filters")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Filtros",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (activeFiltersCount > 0) "$activeFiltersCount" else "Filtros",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Seção expansível de filtros ou chips horizontais rápidos
                        AnimatedVisibility(visible = showFilterDrawer) {
                            FiltersExpandedCard(
                                selectedObjective = selectedObjective,
                                onSelectObjective = { selectedObjective = it },
                                selectedRegion = selectedRegion,
                                onSelectRegion = { selectedRegion = it },
                                selectedCondition = selectedCondition,
                                onSelectCondition = { selectedCondition = it },
                                selectedLevel = selectedLevel,
                                onSelectLevel = { selectedLevel = it },
                                onClearAll = {
                                    selectedObjective = "todos"
                                    selectedRegion = "todas"
                                    selectedCondition = "todas"
                                    selectedLevel = "todos"
                                    searchQuery = ""
                                }
                            )
                        }

                        // Linha de rolagem rápida dos Objetivos (acesso em 1 toque)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            VideoLibraryData.objectives.forEach { obj ->
                                val isSelected = selectedObjective.equals(obj, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedObjective = if (isSelected) "todos" else obj
                                    },
                                    label = {
                                        Text(
                                            text = if (obj == "todos") "Todos Objetivos" else obj.replaceFirstChar { it.uppercase() },
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = FitLimeDark,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        // Contador e Status dos Filtros Ativos
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Exibindo ${filteredVideos.size} de ${videos.size} vídeos",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (activeFiltersCount > 0 || searchQuery.isNotEmpty()) {
                                TextButton(
                                    onClick = {
                                        selectedObjective = "todos"
                                        selectedRegion = "todas"
                                        selectedCondition = "todas"
                                        selectedLevel = "todos"
                                        searchQuery = ""
                                    }
                                ) {
                                    Text(
                                        text = "Limpar filtros ($activeFiltersCount)",
                                        fontSize = 12.sp,
                                        color = FitLimeDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Lista de Vídeos dos Exercícios (Cards Ricos)
                if (filteredVideos.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "🔍", fontSize = 40.sp)
                                Text(
                                    text = "Nenhum vídeo encontrado",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Tente alterar os filtros de objetivo, região, comorbidade ou nível.",
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                OutlinedButton(
                                    onClick = {
                                        selectedObjective = "todos"
                                        selectedRegion = "todas"
                                        selectedCondition = "todas"
                                        selectedLevel = "todos"
                                        searchQuery = ""
                                    },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text("Resetar Filtros")
                                }
                            }
                        }
                    }
                } else {
                    items(filteredVideos, key = { it.id }) { videoExercise ->
                        VideoExerciseCard(
                            videoExercise = videoExercise,
                            onToggleFavorite = { onToggleFavorite(videoExercise.id) },
                            onAddToWorkout = {
                                onAddExerciseToWorkout(videoExercise)
                                Toast.makeText(
                                    context,
                                    "✅ ${videoExercise.name} adicionado ao treino ativo do aluno!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onViewDetails = { activeDetailModal = videoExercise }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Modal de Detalhes Clínicos e Vídeo Expandido
    activeDetailModal?.let { modalExercise ->
        VideoExerciseDetailDialog(
            exercise = modalExercise,
            onDismiss = { activeDetailModal = null },
            onAddToWorkout = {
                onAddExerciseToWorkout(modalExercise)
                Toast.makeText(context, "✅ Prescrito com sucesso!", Toast.LENGTH_SHORT).show()
                activeDetailModal = null
            }
        )
    }
}

/**
 * Card individual do exercício com Player de Vídeo Ilustrativo de 10s integrado
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoExerciseCard(
    videoExercise: VideoExercise,
    onToggleFavorite: () -> Unit,
    onAddToWorkout: () -> Unit,
    onViewDetails: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("video_card_${videoExercise.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top: Nome em destaque + Ações (Favorito)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = videoExercise.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.5.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = FitLime.copy(alpha = 0.25f),
                            border = BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "🎥 Vídeo de ${videoExercise.videoDurationSeconds} segundos",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FitLimeDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = videoExercise.region.replaceFirstChar { it.uppercase() },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (videoExercise.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Favoritar",
                        tint = if (videoExercise.isFavorite) FitLimeDark else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // PLAYER DE VÍDEO ILUSTRATIVO DE 10 SEGUNDOS
            VideoPlayer10s(exercise = videoExercise)

            // GRADE DE PARÂMETROS EXIGIDOS NO BRIEFING
            // Objetivo, Séries, Repetições, Descanso, Nível
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Linha Objetivo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Objetivo: ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = videoExercise.objective,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = FitLimeDark
                        )
                    }

                    // Grid com 4 colunas: Séries, Repetições, Descanso, Nível
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ParamPill(label = "Séries", value = "${videoExercise.sets}")
                        ParamPill(label = "Repetições", value = videoExercise.reps)
                        ParamPill(label = "Descanso", value = "${videoExercise.restSeconds} s")
                        ParamPill(
                            label = "Nível",
                            value = videoExercise.level.replaceFirstChar { it.uppercase() },
                            isHighlight = true
                        )
                    }
                }
            }

            // Tags das Comorbidades e Condições Clínicas
            if (videoExercise.conditions.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Indicado para comorbidades gerontológicas:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        videoExercise.conditions.take(5).forEach { cond ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "• $cond",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        if (videoExercise.conditions.size > 5) {
                            Text(
                                text = "+${videoExercise.conditions.size - 5}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FitLimeDark,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }

            // Botões de Ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Detalhes", fontSize = 12.sp)
                }

                Button(
                    onClick = onAddToWorkout,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FitLimeDark,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1.3f)
                ) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Prescrever", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Player de Vídeo Ilustrativo com looping/cronômetro de 10 segundos
 */
@Composable
fun VideoPlayer10s(
    exercise: VideoExercise
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentSecond by remember { mutableIntStateOf(0) }
    var isSlowMotion by remember { mutableStateOf(false) }

    val totalSeconds = exercise.videoDurationSeconds

    LaunchedEffect(isPlaying, isSlowMotion) {
        while (isPlaying) {
            val stepDelay = if (isSlowMotion) 1300L else 1000L
            delay(stepDelay)
            currentSecond = if (currentSecond >= totalSeconds) 0 else currentSecond + 1
        }
    }

    val progress = (currentSecond.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "video_progress"
    )

    // Fases do movimento para enriquecer a experiência pedagógica
    val phaseLabel = when {
        currentSecond <= 3 -> "Fase 1: Preparação & Alinhamento postural (0-3s)"
        currentSecond <= 7 -> "Fase 2: Movimento com controle excêntrico suave (4-7s)"
        else -> "Fase 3: Retorno firme à posição neutra e expiração (8-10s)"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        border = BorderStroke(1.5.dp, if (isPlaying) FitLime else Color(0xFF334155)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Bar do Player
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isPlaying) Color(0xFFEF4444) else Color(0xFF64748B))
                    )
                    Text(
                        text = if (isPlaying) "REPRODUZINDO" else "VÍDEO PAUSADO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isPlaying) Color.White else Color(0xFF94A3B8)
                    )
                }

                Text(
                    text = "00:${if (currentSecond < 10) "0$currentSecond" else "$currentSecond"} / 00:10",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Tela de Simulação do Vídeo (Canvas / Animação Biomecânica 60+)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1E293B),
                                Color(0xFF0B132B)
                            )
                        )
                    )
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = exercise.emoji,
                        fontSize = 42.sp
                    )

                    Text(
                        text = phaseLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE2E8F0),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }

                // Overlay Play/Pause icon no centro se pausado
                if (!isPlaying) {
                    Surface(
                        shape = CircleShape,
                        color = FitLimeDark.copy(alpha = 0.9f),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Iniciar Vídeo",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            // Barra de Progresso do Vídeo de 10s
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = FitLime,
                trackColor = Color(0xFF334155)
            )

            // Controles: Play/Pause, Reiniciar, Velocidade (0.75x / 1.0x)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .size(34.dp)
                            .background(if (isPlaying) Color(0xFF334155) else FitLimeDark, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproduzir",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            currentSecond = 0
                            isPlaying = true
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reiniciar 10s",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Botão de Velocidade Adaptada para Idosos (0.75x Didático)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSlowMotion) FitLimeDark else Color(0xFF1E293B),
                    border = BorderStroke(1.dp, if (isSlowMotion) FitLime else Color(0xFF475569)),
                    modifier = Modifier.clickable { isSlowMotion = !isSlowMotion }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = if (isSlowMotion) Color.White else Color(0xFFCBD5E1),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isSlowMotion) "0.75x Didático" else "1.0x Normal",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSlowMotion) Color.White else Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Pílula informativa de parâmetros: Séries, Reps, Descanso, Nível
 */
@Composable
fun ParamPill(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = if (isHighlight) FitLimeDark else MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Painel completo de Filtros Expandidos solicitado no Briefing:
 * - Objetivo: força, mobilidade, equilíbrio, flexibilidade, funcionalidade, marcha
 * - Região: membros inferiores, membros superiores, tronco, corpo inteiro
 * - Condição/comorbidade: hipertensão, diabetes, osteoporose, osteoartrite, Parkinson, pós-AVC, sarcopenia, fragilidade, alterações respiratórias, risco de quedas
 * - Nível: iniciante, intermediário, avançado
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FiltersExpandedCard(
    selectedObjective: String,
    onSelectObjective: (String) -> Unit,
    selectedRegion: String,
    onSelectRegion: (String) -> Unit,
    selectedCondition: String,
    onSelectCondition: (String) -> Unit,
    selectedLevel: String,
    onSelectLevel: (String) -> Unit,
    onClearAll: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtros de Prescrição Clínica",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(onClick = onClearAll) {
                    Text("Redefinir Todos", fontSize = 11.sp, color = FitLimeDark)
                }
            }

            // 1. Por Objetivo
            FilterSectionGroup(
                title = "Por Objetivo:",
                items = VideoLibraryData.objectives,
                selected = selectedObjective,
                onSelect = onSelectObjective
            )

            // 2. Por Região
            FilterSectionGroup(
                title = "Por Região:",
                items = VideoLibraryData.regions,
                selected = selectedRegion,
                onSelect = onSelectRegion
            )

            // 3. Por Condição / Comorbidade (As 10 comorbidades clínicas exigidas)
            FilterSectionGroup(
                title = "Por Condição / Comorbidade:",
                items = VideoLibraryData.conditions,
                selected = selectedCondition,
                onSelect = onSelectCondition
            )

            // 4. Por Nível
            FilterSectionGroup(
                title = "Por Nível:",
                items = VideoLibraryData.levels,
                selected = selectedLevel,
                onSelect = onSelectLevel
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSectionGroup(
    title: String,
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                val isSelected = selected.equals(item, ignoreCase = true)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) FitLimeDark else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) FitLimeDark else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.clickable { onSelect(item) }
                ) {
                    Text(
                        text = item.replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Diálogo de Detalhes com Áudio-Guia e Instruções Biomecânicas
 */
@Composable
fun VideoExerciseDetailDialog(
    exercise: VideoExercise,
    onDismiss: () -> Unit,
    onAddToWorkout: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = exercise.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "🎥 Vídeo Ilustrativo de ${exercise.videoDurationSeconds} segundos",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FitLimeDark
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Mini Player Integrado
                VideoPlayer10s(exercise = exercise)

                Text(
                    text = "Instrução de Execução:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = exercise.instruction,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Segurança Clínica Gerontológica:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB91C1C)
                )
                Text(
                    text = exercise.safetyTips,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "🔊 Áudio-guia: '${exercise.name}. ${exercise.instruction}'",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ouvir Áudio-Guia em Voz Alta", fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onAddToWorkout,
                colors = ButtonDefaults.buttonColors(containerColor = FitLimeDark)
            ) {
                Text("Prescrever para o Aluno")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}
