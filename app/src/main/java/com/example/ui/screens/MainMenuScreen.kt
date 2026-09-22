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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BrandLogo

data class MainMenuOption(
    val emoji: String,
    val title: String,
    val onClick: () -> Unit,
    val isHighlighted: Boolean = false
)

@Composable
fun MainMenuScreen(
    onNavigate: (String) -> Unit,
    onBackToStart: () -> Unit
) {
    val scrollState = rememberScrollState()

    val menuItems = listOf(
        MainMenuOption("👤", "Meu Perfil", { onNavigate("student_profile") }),
        MainMenuOption("📋", "Minha Avaliação", { onNavigate("physical_assessment") }),
        MainMenuOption("🏋️", "Meu Treino", { onNavigate("workout_overview") }, isHighlighted = true),
        MainMenuOption("🎥", "Biblioteca de Exercícios", { onNavigate("exercise_active/ex_2") }),
        MainMenuOption("📈", "Minha Evolução", { onNavigate("evolution") }),
        MainMenuOption("❤️", "Minha Saúde", { onNavigate("health") }),
        MainMenuOption("💳", "Meu Plano", { onNavigate("plans") }),
        MainMenuOption("🔔", "Notificações", { onNavigate("reminders") }),
        MainMenuOption("💬", "Fale com o Profissional", { onNavigate("contact_professional") })
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B150C))
            .systemBarsPadding()
            .testTag("main_menu_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 22.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Logo & Subtítulo
            BrandLogo(
                textColor = Color.White,
                scale = 1.2f
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Slogan solicitado
            Text(
                text = "Movimento, força e autonomia para envelhecer com mais segurança.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFD1FAE5),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Header da lista
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MENU PRINCIPAL:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8CEE00),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "9 opções",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 9 Itens do Menu
            menuItems.forEach { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (item.isHighlighted) Color(0xFF18311A) else Color(0xFF132215)
                        )
                        .border(
                            width = if (item.isHighlighted) 1.5.dp else 1.dp,
                            color = if (item.isHighlighted) Color(0xFF8CEE00) else Color(0xFF234227),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { item.onClick() }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.emoji,
                                fontSize = 20.sp,
                                modifier = Modifier.width(34.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = item.title,
                                fontSize = 15.sp,
                                fontWeight = if (item.isHighlighted) FontWeight.ExtraBold else FontWeight.Bold,
                                color = if (item.isHighlighted) Color(0xFF8CEE00) else Color.White
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color(0xFF8CEE00),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Botão Principal Acessar Treino
            Button(
                onClick = { onNavigate("workout_overview") },
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8CEE00),
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .height(56.dp)
            ) {
                Text(
                    text = "🏋️ ACESSAR MEU TREINO",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Voltar
            Text(
                text = "← Voltar ao Início",
                color = Color(0xFFCBD5E1),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { onBackToStart() }
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
