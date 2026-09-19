package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.PlanItem
import com.example.data.ProfessionalStudent
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.ProfessionalBottomNavBar
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark

@Composable
fun ProfessionalScreen(
    students: List<ProfessionalStudent>,
    plans: List<PlanItem> = emptyList(),
    onCreatePlan: (PlanItem) -> Unit = {},
    onDeletePlan: (String) -> Unit = {},
    onNavigateToHostingerConfig: () -> Unit = {},
    onBackClick: () -> Unit,
    onNavigateBottom: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedActionTitle by remember { mutableStateOf<String?>(null) }
    var showPlanManagementDialog by remember { mutableStateOf(false) }
    var showCreatePlanModal by remember { mutableStateOf(false) }

    // Form fields for creating a new plan
    var newPlanName by remember { mutableStateOf("") }
    var newPlanPrice by remember { mutableStateOf("") }
    var newPlanDescription by remember { mutableStateOf("") }
    var newPlanFeatures by remember { mutableStateOf("Treinos personalizados\nAcompanhamento semanal\nAvaliações periódicas") }
    var newPlanIsRecommended by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            ProfessionalBottomNavBar(
                currentRoute = "professional_area",
                onNavigate = onNavigateBottom
            )
        },
        modifier = Modifier.testTag("professional_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            TopBarWithBack(
                title = "Área do profissional",
                onBackClick = onBackClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // Trainer Profile Header Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_trainer_avatar),
                                contentDescription = "Instrutora Carol",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, FitLimeDark, CircleShape)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "Prof. Dra. Camila Rocha",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "CREF 098452-G/SP • Especialista em Fisiologia 60+",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${students.size} alunos ativos no programa",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = FitLimeDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Gestão Comercial e Prescrição",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                // DESTAQUE: Criar e Gerenciar Planos (Solicitado pelo usuário)
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = FitLime.copy(alpha = 0.12f)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, FitLimeDark),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPlanManagementDialog = true }
                            .testTag("menu_manage_plans")
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(FitLimeDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Criar e Gerenciar Planos",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = FitLimeDark
                                    ) {
                                        Text(
                                            text = "${plans.size} planos",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Configure valores de venda, periodicidades e novos pacotes",
                                    fontSize = 12.sp,
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

                item {
                    // Card: Banco de Dados Hostinger (MySQL)
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, FitLimeDark.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToHostingerConfig() }
                            .testTag("card_hostinger_database_config")
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(FitLimeDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Banco de Dados Hostinger",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF10B981)
                                    ) {
                                        Text(
                                            text = "MySQL",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Conectar API PHP, sincronizar alunos e importar SQL no hPanel",
                                    fontSize = 12.sp,
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

                item {
                    ProfessionalMenuItemCard(
                        title = "Meus alunos",
                        subtitle = "Acompanhe a evolução de cada praticante",
                        icon = Icons.Default.Groups,
                        onClick = { selectedActionTitle = "Meus Alunos" }
                    )
                }

                item {
                    ProfessionalMenuItemCard(
                        title = "Cadastrar aluno",
                        subtitle = "Novo cadastro e anamnese orientada",
                        icon = Icons.Default.PersonAdd,
                        onClick = { selectedActionTitle = "Cadastrar Novo Aluno" }
                    )
                }

                item {
                    ProfessionalMenuItemCard(
                        title = "Avaliações",
                        subtitle = "Física e funcional periódica",
                        icon = Icons.Default.Assignment,
                        onClick = { selectedActionTitle = "Avaliações Físicas e Funcionais" }
                    )
                }

                item {
                    ProfessionalMenuItemCard(
                        title = "Prescrição de treinos",
                        subtitle = "Edite e personalize cargas e exercícios",
                        icon = Icons.Default.FitnessCenter,
                        onClick = { selectedActionTitle = "Prescrição de Treinos" }
                    )
                }

                item {
                    ProfessionalMenuItemCard(
                        title = "Meus vídeos",
                        subtitle = "Envie seus vídeos explicativos personalizados",
                        icon = Icons.Default.VideoLibrary,
                        onClick = { selectedActionTitle = "Gerenciamento de Vídeos" }
                    )
                }

                item {
                    ProfessionalMenuItemCard(
                        title = "Relatórios clínicos",
                        subtitle = "Resultados consolidados e gráficos clínicos",
                        icon = Icons.Default.PieChart,
                        onClick = { selectedActionTitle = "Relatórios Clínicos" }
                    )
                }

                item {
                    ProfessionalMenuItemCard(
                        title = "Alertas e lembretes",
                        subtitle = "Notificações automáticas para os alunos",
                        icon = Icons.Default.Notifications,
                        onClick = { selectedActionTitle = "Alertas e Notificações" }
                    )
                }

                item {
                    ProfessionalMenuItemCard(
                        title = "Configurações da conta",
                        subtitle = "Ajustes de conta, dados bancários Pix e integrações",
                        icon = Icons.Default.Settings,
                        onClick = { selectedActionTitle = "Configurações da Conta" }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Modal de Gestão de Planos pelo Profissional
    if (showPlanManagementDialog) {
        AlertDialog(
            onDismissRequest = { showPlanManagementDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gestão de Planos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    IconButton(
                        onClick = {
                            newPlanName = ""
                            newPlanPrice = ""
                            newPlanDescription = ""
                            showCreatePlanModal = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Criar Novo Plano",
                            tint = FitLimeDark
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Planos disponíveis para os seus alunos contratarem:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    plans.forEach { plan ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (plan.isCreatedByProfessional) FitLime.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (plan.isCreatedByProfessional) FitLimeDark else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = plan.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        if (plan.isCreatedByProfessional) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFF2563EB)
                                            ) {
                                                Text(
                                                    text = "Criado por você",
                                                    fontSize = 9.sp,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${plan.price} • ${plan.description}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2
                                    )
                                }

                                if (plan.isCreatedByProfessional) {
                                    IconButton(
                                        onClick = {
                                            onDeletePlan(plan.id)
                                            Toast.makeText(context, "Plano removido", Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Excluir Plano",
                                            tint = Color(0xFFDC2626),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    PrimaryFitButton(
                        text = "+ Criar Novo Plano",
                        onClick = {
                            newPlanName = ""
                            newPlanPrice = ""
                            newPlanDescription = ""
                            showCreatePlanModal = true
                        },
                        testTag = "btn_open_create_plan"
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPlanManagementDialog = false }) {
                    Text("Fechar")
                }
            }
        )
    }

    // Modal para Criar Novo Plano pelo Profissional
    if (showCreatePlanModal) {
        AlertDialog(
            onDismissRequest = { showCreatePlanModal = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Criar Novo Plano",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = newPlanName,
                        onValueChange = { newPlanName = it },
                        label = { Text("Nome do Plano") },
                        placeholder = { Text("Ex: 60+fit Consultoria Vip") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("input_new_plan_name")
                    )

                    OutlinedTextField(
                        value = newPlanPrice,
                        onValueChange = { newPlanPrice = it },
                        label = { Text("Preço Mensal (R$)") },
                        placeholder = { Text("Ex: 149,90") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("input_new_plan_price")
                    )

                    OutlinedTextField(
                        value = newPlanDescription,
                        onValueChange = { newPlanDescription = it },
                        label = { Text("Descrição Resumida") },
                        placeholder = { Text("Ex: Treinos com foco em pós-operatório ou reabilitação") },
                        singleLine = false,
                        maxLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("input_new_plan_desc")
                    )

                    OutlinedTextField(
                        value = newPlanFeatures,
                        onValueChange = { newPlanFeatures = it },
                        label = { Text("Recursos inclusos (1 por linha)") },
                        singleLine = false,
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("input_new_plan_features")
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = newPlanIsRecommended,
                            onCheckedChange = { newPlanIsRecommended = it },
                            colors = CheckboxDefaults.colors(checkedColor = FitLimeDark)
                        )
                        Text(
                            text = "Marcar como recomendado para os alunos",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            confirmButton = {
                PrimaryFitButton(
                    text = "Salvar Plano",
                    enabled = newPlanName.isNotBlank() && newPlanPrice.isNotBlank(),
                    onClick = {
                        val numericPrice = newPlanPrice.replace(",", ".").toDoubleOrNull() ?: 99.90
                        val featuresList = newPlanFeatures.lines().filter { it.isNotBlank() }
                        val createdPlan = PlanItem(
                            id = "plan_custom_${System.currentTimeMillis()}",
                            name = newPlanName.trim(),
                            monthlyPrice = numericPrice,
                            price = "R$ ${String.format("%.2f", numericPrice)}/mês",
                            description = newPlanDescription.trim(),
                            features = if (featuresList.isNotEmpty()) featuresList else listOf("Treinos personalizados", "Acompanhamento profissional"),
                            isRecommended = newPlanIsRecommended,
                            isCreatedByProfessional = true,
                            authorName = "Prof. Dra. Camila Rocha"
                        )
                        onCreatePlan(createdPlan)
                        showCreatePlanModal = false
                        Toast.makeText(context, "Plano '${createdPlan.name}' criado com sucesso!", Toast.LENGTH_SHORT).show()
                    },
                    testTag = "btn_save_new_plan"
                )
            },
            dismissButton = {
                TextButton(onClick = { showCreatePlanModal = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Modal Genérico de Ações do Profissional
    if (selectedActionTitle != null) {
        AlertDialog(
            onDismissRequest = { selectedActionTitle = null },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = selectedActionTitle ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (selectedActionTitle == "Meus Alunos") {
                        students.forEach { student ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "${student.name} (${student.age} anos)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(text = "Treino: ${student.program} • ${student.completionRate}% adesão", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Módulo de $selectedActionTitle pronto para interação e gestão dos alunos 60+.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            confirmButton = {
                PrimaryFitButton(
                    text = "Fechar",
                    onClick = { selectedActionTitle = null }
                )
            }
        )
    }
}

@Composable
private fun ProfessionalMenuItemCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(FitLime.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = FitLimeDark,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
