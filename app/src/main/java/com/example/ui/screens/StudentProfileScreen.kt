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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.StudentProfile
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark
import kotlinx.coroutines.launch

@Composable
fun StudentProfileScreen(
    profile: StudentProfile,
    onBackClick: () -> Unit,
    onSaveClick: (StudentProfile) -> Unit,
    onManagePlansClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 1. Dados Pessoais do Aluno
    var name by remember { mutableStateOf(profile.name) }
    var birthDate by remember { mutableStateOf(profile.birthDate) }
    var gender by remember { mutableStateOf(profile.gender) }
    var phone by remember { mutableStateOf(profile.phone) }
    var email by remember { mutableStateOf(profile.email) }
    var emergencyContact by remember { mutableStateOf(profile.emergencyContact) }
    var goal by remember { mutableStateOf(profile.mainGoal) }
    var goalMenuExpanded by remember { mutableStateOf(false) }

    // 2. Informações Profissionais
    var responsibleProfessional by remember { mutableStateOf(profile.responsibleProfessional) }
    var startDate by remember { mutableStateOf(profile.startDate) }
    var contractedPlan by remember { mutableStateOf(profile.contractedPlan) }
    var planMenuExpanded by remember { mutableStateOf(false) }
    var weeklyFrequency by remember { mutableStateOf(profile.weeklyFrequency) }
    var professionalNotes by remember { mutableStateOf(profile.professionalNotes) }

    val goalsList = listOf(
        "Ganhar força e autonomia",
        "Melhorar o equilíbrio",
        "Prevenção de quedas",
        "Aumentar flexibilidade e mobilidade",
        "Alívio de dores nas costas e joelhos",
        "Condicionamento físico geral"
    )

    val plansList = listOf(
        "60+fit Essencial (R$ 79,90/mês)",
        "60+fit Gerontológico (R$ 129,90/mês)",
        "60+fit Premium (R$ 199,90/mês)",
        "60+fit Personalizado (Consultar)"
    )

    val frequencyOptions = listOf(
        "1x por semana",
        "2x por semana",
        "3x por semana",
        "4x por semana",
        "5x por semana"
    )

    val genderOptions = listOf(
        "Feminino",
        "Masculino",
        "Outro"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding()
                .testTag("student_profile_screen")
        ) {
            TopBarWithBack(
                title = "Cadastro do Aluno",
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                // Header badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = FitLime.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.35f)),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "Ficha de Cadastro e Matrícula",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitLimeDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                // ==========================================
                // CAMPO: FOTO
                // ==========================================
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Image(
                            painter = painterResource(id = R.drawable.img_avatar_maria),
                            contentDescription = "Foto do Aluno",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(112.dp)
                                .clip(CircleShape)
                                .border(3.5.dp, FitLime, CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(FitLimeDark)
                                .clickable {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Foto do aluno atualizada!")
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Alterar Foto",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Toque no ícone da câmera para trocar a Foto do aluno",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 18.dp)
                )

                // ==========================================
                // SEÇÃO: DADOS PESSOAIS
                // ==========================================
                Text(
                    text = "Identificação do Aluno",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                // 1. Nome
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome completo") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FitLimeDark) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FitLimeDark,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_name_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Data de nascimento
                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    label = { Text("Data de nascimento (DD/MM/AAAA)") },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = FitLimeDark) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FitLimeDark,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_birthdate_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 3. Sexo (Selector Chips)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Sexo:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genderOptions.forEach { option ->
                            val isSelected = gender.equals(option, ignoreCase = true)
                            FilterChip(
                                selected = isSelected,
                                onClick = { gender = option },
                                label = { Text(option, fontSize = 13.sp) },
                                leadingIcon = if (isSelected) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = FitLime.copy(alpha = 0.3f),
                                    selectedLabelColor = FitLimeDark
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("profile_gender_$option")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 4. Telefone
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefone") },
                    placeholder = { Text("(11) 98765-4321") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = FitLimeDark) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FitLimeDark,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_phone_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 5. E-mail
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    placeholder = { Text("aluno@email.com") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = FitLimeDark) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FitLimeDark,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_email_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 6. Contato de emergência
                OutlinedTextField(
                    value = emergencyContact,
                    onValueChange = { emergencyContact = it },
                    label = { Text("Contato de emergência") },
                    placeholder = { Text("Nome do responsável e telefone com DDD") },
                    leadingIcon = { Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = Color(0xFFDC2626)) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FitLimeDark,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_emergency_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 7. Objetivo (Dropdown)
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = goal,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Objetivo") },
                        leadingIcon = { Icon(Icons.Default.TrackChanges, contentDescription = null, tint = FitLimeDark) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.clickable { goalMenuExpanded = true }
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FitLimeDark,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { goalMenuExpanded = true }
                            .testTag("profile_goal_input")
                    )

                    DropdownMenu(
                        expanded = goalMenuExpanded,
                        onDismissRequest = { goalMenuExpanded = false }
                    ) {
                        goalsList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    goal = item
                                    goalMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ==========================================
                // SEÇÃO: INFORMAÇÕES PROFISSIONAIS
                // ==========================================
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, FitLimeDark.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(FitLimeDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MedicalServices,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Informações profissionais",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Atribuição técnica, plano e cronograma",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // 8. Profissional responsável
                        OutlinedTextField(
                            value = responsibleProfessional,
                            onValueChange = { responsibleProfessional = it },
                            label = { Text("Profissional responsável") },
                            placeholder = { Text("Nome do professor / fisioterapeuta (CREF/CREFITO)") },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = FitLimeDark) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FitLimeDark,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("profile_responsible_professional_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 9. Data de início
                        OutlinedTextField(
                            value = startDate,
                            onValueChange = { startDate = it },
                            label = { Text("Data de início (DD/MM/AAAA)") },
                            leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = FitLimeDark) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FitLimeDark,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("profile_start_date_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 10. Plano contratado (Dropdown)
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = contractedPlan,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Plano contratado") },
                                leadingIcon = { Icon(Icons.Default.CardMembership, contentDescription = null, tint = FitLimeDark) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        modifier = Modifier.clickable { planMenuExpanded = true }
                                    )
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FitLimeDark,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { planMenuExpanded = true }
                                    .testTag("profile_contracted_plan_input")
                            )

                            DropdownMenu(
                                expanded = planMenuExpanded,
                                onDismissRequest = { planMenuExpanded = false }
                            ) {
                                plansList.forEach { plan ->
                                    DropdownMenuItem(
                                        text = { Text(plan) },
                                        onClick = {
                                            contractedPlan = plan
                                            planMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 11. Frequência semanal
                        Text(
                            text = "Frequência semanal:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            frequencyOptions.take(4).forEach { freq ->
                                val isSelected = weeklyFrequency == freq
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { weeklyFrequency = freq },
                                    label = { Text(freq.replace(" por semana", ""), fontSize = 12.sp) },
                                    leadingIcon = if (isSelected) {
                                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = FitLime.copy(alpha = 0.3f),
                                        selectedLabelColor = FitLimeDark
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("profile_freq_$freq")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 12. Observações
                        OutlinedTextField(
                            value = professionalNotes,
                            onValueChange = { professionalNotes = it },
                            label = { Text("Observações") },
                            leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = FitLimeDark) },
                            placeholder = { Text("Anotações clínicas, cuidados articulares, restrições e histórico...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(115.dp)
                                .testTag("profile_notes_input"),
                            maxLines = 5,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FitLimeDark,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                // Botão Salvar Cadastro
                PrimaryFitButton(
                    text = "Salvar Cadastro do Aluno",
                    onClick = {
                        val updated = profile.copy(
                            name = name,
                            birthDate = birthDate,
                            gender = gender,
                            phone = phone,
                            email = email,
                            emergencyContact = emergencyContact,
                            mainGoal = goal,
                            responsibleProfessional = responsibleProfessional,
                            startDate = startDate,
                            contractedPlan = contractedPlan,
                            weeklyFrequency = weeklyFrequency,
                            professionalNotes = professionalNotes
                        )
                        onSaveClick(updated)
                        Toast.makeText(context, "Cadastro do aluno salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    },
                    testTag = "profile_save_button"
                )

                Spacer(modifier = Modifier.height(26.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
