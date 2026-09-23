package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.StudentProfile
import com.example.ui.components.StudentBottomNavBar
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark

data class MainMenuItem(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val badgeColor: Color,
    val testTag: String,
    val onClick: () -> Unit
)

@Composable
fun StudentHomeScreen(
    profile: StudentProfile,
    onStartWorkoutClick: () -> Unit = {},
    onProgressClick: () -> Unit = {},
    onHealthClick: () -> Unit = {},
    onExercisesClick: () -> Unit = {},
    onRemindersClick: () -> Unit = {},
    onPlansClick: () -> Unit = {},
    onAccessibilityClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAssessmentClick: () -> Unit = {},
    onWorkoutClick: () -> Unit = onStartWorkoutClick,
    onConditionWorkoutsClick: () -> Unit = {},
    onSafetyCheckClick: () -> Unit = {},
    onEvolutionClick: () -> Unit = onProgressClick,
    onNotificationsClick: () -> Unit = onRemindersClick,
    onContactProfessionalClick: (() -> Unit)? = null,
    onNavigateBottom: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showContactDialog by remember { mutableStateOf(false) }

    val menuItems = listOf(
        MainMenuItem(
            emoji = "🛡️",
            title = "Atenção antes do treino",
            subtitle = "Sistema de segurança • Checagem de tontura, dor, ar e quedas",
            badgeColor = Color(0xFFFEE2E2),
            testTag = "menu_item_safety_check",
            onClick = { onSafetyCheckClick() }
        ),
        MainMenuItem(
            emoji = "👤",
            title = "Meu Perfil",
            subtitle = "Dados cadastrais, peso, altura e objetivos",
            badgeColor = Color(0xFFE0E7FF),
            testTag = "menu_item_profile",
            onClick = { onProfileClick() }
        ),
        MainMenuItem(
            emoji = "📋",
            title = "Minha Avaliação",
            subtitle = "Sinais vitais, anamnese e testes funcionais",
            badgeColor = Color(0xFFE0F2FE),
            testTag = "menu_item_assessment",
            onClick = { onAssessmentClick() }
        ),
        MainMenuItem(
            emoji = "🏋️",
            title = "Meu Treino",
            subtitle = "Ficha do dia, séries e repetições adaptadas",
            badgeColor = Color(0xFFD9F99D),
            testTag = "menu_item_workout",
            onClick = { onWorkoutClick() }
        ),
        MainMenuItem(
            emoji = "🎯",
            title = "Treino Direcionado",
            subtitle = "Hipertensão, Diabetes, Osteoporose, Parkinson, Pós-AVC, etc.",
            badgeColor = Color(0xFFC7D2FE),
            testTag = "menu_item_condition_workouts",
            onClick = { onConditionWorkoutsClick() }
        ),
        MainMenuItem(
            emoji = "🎥",
            title = "Biblioteca de Vídeos",
            subtitle = "Vídeos demonstrativos de 10s adaptados para a melhor idade",
            badgeColor = Color(0xFFFEF08A),
            testTag = "menu_item_exercises",
            onClick = { onExercisesClick() }
        ),
        MainMenuItem(
            emoji = "📈",
            title = "Minha Evolução",
            subtitle = "Sua evolução desde a 1ª avaliação • Força, equilíbrio e marcha",
            badgeColor = Color(0xFFDDD6FE),
            testTag = "menu_item_evolution",
            onClick = { onEvolutionClick() }
        ),
        MainMenuItem(
            emoji = "❤️",
            title = "Minha Saúde",
            subtitle = "Pressão arterial, frequência cardíaca e SpO₂",
            badgeColor = Color(0xFFFECDD3),
            testTag = "menu_item_health",
            onClick = { onHealthClick() }
        ),
        MainMenuItem(
            emoji = "💳",
            title = "Meu Plano",
            subtitle = "Pagamentos, Pix automático, cartões e benefícios",
            badgeColor = Color(0xFFD1FAE5),
            testTag = "menu_item_plans",
            onClick = { onPlansClick() }
        ),
        MainMenuItem(
            emoji = "🔔",
            title = "Notificações e Lembretes",
            subtitle = "Lembretes de água, treino e medicamentos",
            badgeColor = Color(0xFFFFEDD5),
            testTag = "menu_item_notifications",
            onClick = { onNotificationsClick() }
        ),
        MainMenuItem(
            emoji = "💬",
            title = "Fale com o Profissional",
            subtitle = "Dúvidas, orientações e contato direto com seu instrutor",
            badgeColor = Color(0xFFCCFBF1),
            testTag = "menu_item_contact_professional",
            onClick = {
                if (onContactProfessionalClick != null) {
                    onContactProfessionalClick()
                } else {
                    showContactDialog = true
                }
            }
        )
    )

    Scaffold(
        bottomBar = {
            StudentBottomNavBar(
                currentRoute = "student_home",
                onNavigate = onNavigateBottom
            )
        },
        modifier = Modifier.testTag("student_home_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Top Indicator / Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = FitLime.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.4f)),
                        modifier = Modifier.testTag("home_top_screen_badge")
                    ) {
                        Text(
                            text = "Início",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = FitLimeDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = FitLimeDark,
                        modifier = Modifier
                            .clickable { onPlansClick() }
                            .testTag("home_top_quick_payment_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Pagamentos",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier
                            .size(42.dp)
                            .testTag("home_top_notifications")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificações",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(
                        onClick = onAccessibilityClick,
                        modifier = Modifier
                            .size(42.dp)
                            .testTag("home_top_accessibility")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Accessibility,
                            contentDescription = "Acessibilidade",
                            tint = FitLimeDark
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Image(
                        painter = painterResource(id = R.drawable.img_avatar_maria),
                        contentDescription = "Foto do perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(2.dp, FitLime, CircleShape)
                            .clickable { onProfileClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Brand Title & Subtitle Header
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "60+fit",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = FitLimeDark,
                    letterSpacing = (-0.5).sp
                )

                Text(
                    text = "Musculação e Funcionalidade",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Movimento, força e autonomia para envelhecer com mais segurança.",
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Card de Acesso Rápido ao Sistema de Pagamentos & Assinaturas
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FitLime.copy(alpha = 0.12f)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, FitLimeDark.copy(alpha = 0.8f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlansClick() }
                    .testTag("home_payment_quick_access_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(FitLimeDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payment,
                                    contentDescription = "Pagamentos",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "💳 Pagamentos & Meu Plano",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Plano atual: ${profile.contractedPlan}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = FitLimeDark
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = FitLimeDark
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Acessar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🟢 Pix • 🔄 Pix Automático • 💳 Cartões",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Gerenciar",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FitLimeDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Card Especial: Treino Direcionado
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, FitLimeDark.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onConditionWorkoutsClick() }
                    .testTag("home_directed_workout_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = FitLimeDark,
                            contentColor = Color.White
                        ) {
                            Text(
                                text = "🎯 ÁREA EXCLUSIVA 60+",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = FitLime.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "8 Condições Clínicas",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FitLimeDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(FitLime.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🩺", fontSize = 24.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Treino direcionado",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Prescrições clínicas: Hipertensão, Diabetes tipo 2, Osteoporose, Artrose, Parkinson, AVC, Sarcopenia e Fragilidade.",
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = FitLimeDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Card Especial: Sistema de Segurança • Atenção antes do treino
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFEF4444).copy(alpha = 0.8f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSafetyCheckClick() }
                    .testTag("home_safety_system_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDC2626),
                            contentColor = Color.White
                        ) {
                            Text(
                                text = "🛡️ SISTEMA DE SEGURANÇA",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF2F2),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                        ) {
                            Text(
                                text = "Check-in Pré-treino",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFDC2626),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF2F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚠️", fontSize = 24.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Atenção antes do treino",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Tontura, dor no peito, falta de ar, mal-estar ou queda recente? O app interrompe a sessão e orienta o cuidado adequado.",
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section: Menu Principal
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp, 20.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(FitLimeDark)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Opções e recursos:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // The Menu Items
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                menuItems.forEach { item ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { item.onClick() }
                            .testTag(item.testTag)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Emoji / Icon Container
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(item.badgeColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.emoji,
                                    fontSize = 26.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Text Column
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = item.subtitle,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Chevron Forward Indicator with 36dp touch target
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = "Abrir",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Modal / Dialog: Fale com o Profissional
    if (showContactDialog) {
        var messageText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showContactDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💬", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fale com o Profissional",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = { showContactDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Professional Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = FitLime.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(FitLimeDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "👩‍⚕️", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Dra. Mariana Lima",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Personal & Fisioterapeuta 60+ (CREF 045123)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Text(
                        text = "Assuntos rápidos frequentes:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Quick Chips
                    val quickTopics = listOf(
                        "Dúvida sobre postura no exercício",
                        "Senti um leve desconforto",
                        "Quero alterar os dias de treino"
                    )
                    quickTopics.forEach { topic ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { messageText = topic }
                        ) {
                            Text(
                                text = "• $topic",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        label = { Text("Sua mensagem") },
                        placeholder = { Text("Descreva sua dúvida ou mensagem...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Mensagem enviada para sua treinadora!", Toast.LENGTH_LONG).show()
                        showContactDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FitLimeDark)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Enviar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Iniciando chamada com Dr. Mariana...", Toast.LENGTH_SHORT).show()
                        showContactDialog = false
                    },
                    colors = ButtonDefaults.outlinedButtonColors()
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ligar")
                }
            }
        )
    }
}
