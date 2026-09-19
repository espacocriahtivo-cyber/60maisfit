package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark
import kotlinx.coroutines.launch

@Composable
fun HostingerConfigScreen(
    repository: AppRepository = AppRepository.instance,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    val hostingerConfig by repository.hostingerConfig.collectAsState()

    var inputUrl by remember { mutableStateOf(hostingerConfig.serverUrl) }
    var inputToken by remember { mutableStateOf(hostingerConfig.apiToken) }

    var testStatusMessage by remember { mutableStateOf<String?>(null) }
    var testStatusSuccess by remember { mutableStateOf<Boolean?>(null) }
    var isTesting by remember { mutableStateOf(false) }
    var isCreatingTables by remember { mutableStateOf(false) }
    var isSyncing by remember { mutableStateOf(false) }

    var expandedStep by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        modifier = Modifier.testTag("hostinger_config_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            TopBarWithBack(
                title = "Banco de Dados Hostinger",
                onBackClick = onBackClick
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Card de Status da Conexão com Hostinger
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (hostingerConfig.isConnected) Color(0xFFECFDF5) else Color(0xFFF8FAFC)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (hostingerConfig.isConnected) Color(0xFF10B981) else MaterialTheme.colorScheme.outline
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().testTag("hostinger_status_card")
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(if (hostingerConfig.isConnected) Color(0xFF10B981) else FitLimeDark),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (hostingerConfig.isConnected) Icons.Default.CloudDone else Icons.Default.Storage,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Status da Hostinger",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = if (hostingerConfig.isConnected) "Conectado ao MySQL" else "Modo Local / Desconectado",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (hostingerConfig.isConnected) Color(0xFF047857) else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (hostingerConfig.isConnected) Color(0xFF10B981) else Color(0xFFE2E8F0)
                                ) {
                                    Text(
                                        text = if (hostingerConfig.isConnected) "ONLINE" else "OFFLINE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (hostingerConfig.isConnected) Color.White else Color(0xFF475569),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            if (hostingerConfig.serverInfo.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = hostingerConfig.serverInfo,
                                    fontSize = 12.sp,
                                    color = Color(0xFF065F46),
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Última sincronização: ${hostingerConfig.lastSyncTime}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Formulário de Configuração do Endpoint
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Parâmetros do Servidor Hostinger",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Informe a URL da API PHP instalada no seu domínio Hostinger.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Campo URL
                            OutlinedTextField(
                                value = inputUrl,
                                onValueChange = {
                                    inputUrl = it
                                    repository.updateHostingerConfig(it, inputToken)
                                },
                                label = { Text("URL da API Hostinger (api.php)") },
                                placeholder = { Text("https://seudominio.com.br/api/api.php") },
                                leadingIcon = {
                                    Icon(Icons.Default.Language, contentDescription = null, tint = FitLimeDark)
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("hostinger_url_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Campo Token
                            OutlinedTextField(
                                value = inputToken,
                                onValueChange = {
                                    inputToken = it
                                    repository.updateHostingerConfig(inputUrl, it)
                                },
                                label = { Text("Token de Segurança (Hostinger X-API-KEY)") },
                                placeholder = { Text("rDEe8IwynGbuFLrNqRxcUZdVU5xcHdmSqp8VGxJQcec65c7d") },
                                leadingIcon = {
                                    Icon(Icons.Default.Key, contentDescription = null, tint = FitLimeDark)
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("hostinger_token_input")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Botão Testar Conexão
                            Button(
                                onClick = {
                                    isTesting = true
                                    testStatusMessage = null
                                    coroutineScope.launch {
                                        val result = repository.testHostingerConnection(inputUrl, inputToken)
                                        isTesting = false
                                        if (result.isSuccess) {
                                            testStatusSuccess = true
                                            testStatusMessage = result.getOrNull() ?: "Conexão estabelecida com sucesso!"
                                            Toast.makeText(context, "Conectado ao MySQL da Hostinger com sucesso!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            testStatusSuccess = false
                                            testStatusMessage = result.exceptionOrNull()?.localizedMessage ?: "Erro desconhecido ao conectar"
                                        }
                                    }
                                },
                                enabled = !isTesting && !isCreatingTables && !isSyncing && inputUrl.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(containerColor = FitLimeDark),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("btn_test_hostinger_connection")
                            ) {
                                if (isTesting) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Testando conexão com a Hostinger...", fontWeight = FontWeight.Bold, color = Color.White)
                                } else {
                                    Icon(Icons.Default.CloudSync, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Testar Conexão com Hostinger", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Botão Criar Tabelas no MySQL da Hostinger
                            Button(
                                onClick = {
                                    isCreatingTables = true
                                    testStatusMessage = null
                                    coroutineScope.launch {
                                        val result = repository.createHostingerTables()
                                        isCreatingTables = false
                                        if (result.isSuccess) {
                                            testStatusSuccess = true
                                            testStatusMessage = result.getOrNull() ?: "Tabelas criadas com sucesso!"
                                            Toast.makeText(context, "Tabelas criadas com sucesso no MySQL!", Toast.LENGTH_LONG).show()
                                        } else {
                                            testStatusSuccess = false
                                            testStatusMessage = result.exceptionOrNull()?.localizedMessage ?: "Erro ao criar tabelas"
                                        }
                                    }
                                },
                                enabled = !isTesting && !isCreatingTables && !isSyncing && inputUrl.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("btn_create_hostinger_tables")
                            ) {
                                if (isCreatingTables) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Criando tabelas no MySQL...", fontWeight = FontWeight.Bold, color = Color.White)
                                } else {
                                    Icon(Icons.Default.Storage, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("⚡ Criar Tabelas no MySQL da Hostinger", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Botão Sincronizar Dados Agora
                            OutlinedButton(
                                onClick = {
                                    isSyncing = true
                                    coroutineScope.launch {
                                        val result = repository.syncWithHostinger()
                                        isSyncing = false
                                        if (result.isSuccess) {
                                            testStatusSuccess = true
                                            testStatusMessage = result.getOrNull() ?: "Dados sincronizados com sucesso!"
                                            Toast.makeText(context, "Dados sincronizados com o banco Hostinger!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            testStatusSuccess = false
                                            testStatusMessage = result.exceptionOrNull()?.localizedMessage ?: "Falha ao sincronizar"
                                        }
                                    }
                                },
                                enabled = !isTesting && !isCreatingTables && !isSyncing && inputUrl.isNotBlank(),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("btn_sync_hostinger_data")
                            ) {
                                if (isSyncing) {
                                    CircularProgressIndicator(
                                        color = FitLimeDark,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Sincronizando com a Hostinger...", fontWeight = FontWeight.Bold, color = FitLimeDark)
                                } else {
                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = FitLimeDark)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Sincronizar Dados Agora", fontWeight = FontWeight.Bold, color = FitLimeDark)
                                }
                            }

                            // Banner de Feedback do Teste
                            AnimatedVisibility(visible = testStatusMessage != null) {
                                testStatusMessage?.let { msg ->
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (testStatusSuccess == true) Color(0xFFD1FAE5) else Color(0xFFFEE2E2),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (testStatusSuccess == true) Color(0xFF10B981) else Color(0xFFEF4444)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                imageVector = if (testStatusSuccess == true) Icons.Default.CheckCircle else Icons.Default.Error,
                                                contentDescription = null,
                                                tint = if (testStatusSuccess == true) Color(0xFF047857) else Color(0xFFB91C1C),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = msg,
                                                fontSize = 13.sp,
                                                color = if (testStatusSuccess == true) Color(0xFF065F46) else Color(0xFF991B1B)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Seção: Guia Passo a Passo da Hostinger
                item {
                    Text(
                        text = "📖 Como Configurar na Hostinger (Passo a Passo)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Os arquivos de banco de dados e API já foram gerados na pasta hostinger_backend/ do projeto.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Passo 1
                item {
                    HostingerStepCard(
                        stepNumber = 1,
                        title = "Criar o Banco de Dados MySQL no hPanel",
                        description = "1. No hPanel da Hostinger, vá em 'Bancos de Dados' -> 'Bancos de Dados MySQL'.\n2. Crie um novo banco (ex: fit60), definindo um usuário e senha forte.\n3. A Hostinger criará nomes como u123456789_fit60.",
                        codeSnippet = "Nome do Banco: u123456789_fit60\nUsuário: u123456789_admin",
                        isExpanded = expandedStep == 1,
                        onToggle = { expandedStep = if (expandedStep == 1) null else 1 },
                        onCopy = { clipboardManager.setText(AnnotatedString("u123456789_fit60")) }
                    )
                }

                // Passo 2
                item {
                    HostingerStepCard(
                        stepNumber = 2,
                        title = "Criar as Tabelas no MySQL (3 Opções Rápidas)",
                        description = "Você pode criar as tabelas de 3 formas fáceis:\n\n• Opção A (Pelo App): Informe a URL da API acima e clique no botão '⚡ Criar Tabelas no MySQL da Hostinger'.\n• Opção B (No Navegador): Acesse https://seusite.com.br/api/install.php e clique em 'Criar Todas as Tabelas'.\n• Opção C (phpMyAdmin): No hPanel, abra o phpMyAdmin, vá em 'Importar' e selecione o arquivo 'hostinger_backend/database_schema.sql'.",
                        codeSnippet = "https://seusite.com.br/api/install.php",
                        isExpanded = expandedStep == 2,
                        onToggle = { expandedStep = if (expandedStep == 2) null else 2 },
                        onCopy = { clipboardManager.setText(AnnotatedString("https://seusite.com.br/api/install.php")) }
                    )
                }

                // Passo 3
                item {
                    HostingerStepCard(
                        stepNumber = 3,
                        title = "Configurar config.php e enviar para public_html/api/",
                        description = "1. Abra 'hostinger_backend/config.php' e coloque o nome do banco, usuário e senha que você criou.\n2. No hPanel, abra o 'Gerenciador de Arquivos'.\n3. Na pasta public_html, crie a pasta 'api' e faça upload de config.php e api.php.\n4. Teste acessando https://seusite.com.br/api/api.php?action=test no navegador.",
                        codeSnippet = "define('DB_HOST', 'localhost');\ndefine('DB_NAME', 'u123456789_fit60');\ndefine('DB_USER', 'u123456789_admin');\ndefine('DB_PASS', 'SuaSenhaSegura');",
                        isExpanded = expandedStep == 3,
                        onToggle = { expandedStep = if (expandedStep == 3) null else 3 },
                        onCopy = { clipboardManager.setText(AnnotatedString("https://seusite.com.br/api/api.php?action=test")) }
                    )
                }

                // Passo 4
                item {
                    HostingerStepCard(
                        stepNumber = 4,
                        title = "Conectar e Sincronizar no Aplicativo",
                        description = "1. Cole a URL da sua API no campo acima (ex: https://seusite.com.br/api/api.php).\n2. Clique em 'Testar Conexão com Hostinger'.\n3. Em seguida, clique em 'Sincronizar Dados Agora'.\n4. O aplicativo passará a sincronizar treinos, alunos e planos diretamente no MySQL da sua hospedagem!",
                        codeSnippet = "https://seusite.com.br/api/api.php",
                        isExpanded = expandedStep == 4,
                        onToggle = { expandedStep = if (expandedStep == 4) null else 4 },
                        onCopy = { clipboardManager.setText(AnnotatedString("https://seusite.com.br/api/api.php")) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun HostingerStepCard(
    stepNumber: Int,
    title: String,
    description: String,
    codeSnippet: String? = null,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onCopy: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(FitLimeDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stepNumber.toString(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = FitLimeDark
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = description,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (codeSnippet != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E293B),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = codeSnippet,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF86EFAC),
                                    modifier = Modifier.weight(1f)
                                )
                                if (onCopy != null) {
                                    IconButton(
                                        onClick = {
                                            onCopy()
                                            Toast.makeText(context, "Copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copiar",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
