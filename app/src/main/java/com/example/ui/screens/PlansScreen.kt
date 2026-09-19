package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BillingPeriod
import com.example.data.PaymentMethod
import com.example.data.PlanItem
import com.example.data.UserSubscription
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(
    plans: List<PlanItem>,
    selectedPlanId: String,
    userSubscription: UserSubscription,
    onBackClick: () -> Unit,
    onSelectPlan: (String) -> Unit,
    onConfirmSubscription: (String, BillingPeriod, PaymentMethod) -> Unit,
    onCancelSubscription: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Planos & Pagamento, 1 = Minha Assinatura

    var activePlanId by remember { mutableStateOf(selectedPlanId) }
    var selectedPeriod by remember { mutableStateOf(BillingPeriod.MENSAL) }
    var selectedMethod by remember { mutableStateOf(PaymentMethod.CREDIT_CARD) }

    // Payment Form fields
    var cardNumber by remember { mutableStateOf("•••• •••• •••• 4242") }
    var cardName by remember { mutableStateOf("MARIA SILVA") }
    var cardExpiry by remember { mutableStateOf("12/28") }
    var cardCvv by remember { mutableStateOf("123") }
    var pixPayerKey by remember { mutableStateOf("maria.silva@email.com") }

    // Confirmation Modal
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    val activePlan = plans.find { it.id == activePlanId } ?: plans.first()
    val (totalAmount, monthlyEquivalent) = activePlan.calculatePrice(selectedPeriod)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding()
                .testTag("plans_screen")
        ) {
            TopBarWithBack(
                title = "Planos e Pagamentos",
                onBackClick = onBackClick
            )

            // Tabs: Planos & Contratação vs Minha Assinatura
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = FitLimeDark
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Escolher Plano",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier.testTag("tab_choose_plan")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "📆 Minha Assinatura",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_my_subscription")
                )
            }

            if (selectedTab == 0) {
                // TAB 0: SELEÇÃO DE PLANO + PERÍODO + MÉTODO DE PAGAMENTO
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column {
                            Text(
                                text = "Modelo de Venda 60+fit",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Movimento, força e longevidade com suporte profissional sob medida.",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    // 1. SELEÇÃO DE PRODUTO
                    item {
                        Text(
                            text = "1. Escolha seu produto:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    items(plans) { plan ->
                        val isSelected = plan.id == activePlanId
                        PlanProductCard(
                            plan = plan,
                            isSelected = isSelected,
                            onSelect = {
                                activePlanId = plan.id
                                onSelectPlan(plan.id)
                            }
                        )
                    }

                    // 2. PERIODICIDADE DE COBRANÇA
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "2. Período de faturamento:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Planos de longo prazo garantem maior economia e continuidade.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    BillingPeriod.values().forEach { period ->
                                        val isPeriodSelected = selectedPeriod == period
                                        val (_, equiv) = activePlan.calculatePrice(period)
                                        FilterChip(
                                            selected = isPeriodSelected,
                                            onClick = { selectedPeriod = period },
                                            label = {
                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(
                                                        text = period.title,
                                                        fontSize = 12.sp,
                                                        fontWeight = if (isPeriodSelected) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                    Text(
                                                        text = period.badge,
                                                        fontSize = 10.sp,
                                                        color = if (isPeriodSelected) FitLimeDark else MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = FitLime.copy(alpha = 0.3f),
                                                selectedLabelColor = FitLimeDark
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("period_${period.name.lowercase()}")
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Resumo do valor no período
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = FitLime.copy(alpha = 0.15f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "Equivalente a:",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "R$ ${String.format("%.2f", monthlyEquivalent)}/mês",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = FitLimeDark
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "Total a pagar:",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "R$ ${String.format("%.2f", totalAmount)}",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 3. FORMAS DE PAGAMENTO
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "3. Forma de pagamento integrada:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Transações seguras com criptografia ponta a ponta.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
                                )

                                PaymentMethod.values().forEach { method ->
                                    val isMethodSelected = selectedMethod == method
                                    PaymentMethodOptionRow(
                                        method = method,
                                        isSelected = isMethodSelected,
                                        onSelect = { selectedMethod = method }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Dynamic fields according to payment method
                                when (selectedMethod) {
                                    PaymentMethod.CREDIT_CARD, PaymentMethod.DEBIT_CARD -> {
                                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Text(
                                                text = if (selectedMethod == PaymentMethod.CREDIT_CARD) "Dados do Cartão de Crédito" else "Dados do Cartão de Débito",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            OutlinedTextField(
                                                value = cardNumber,
                                                onValueChange = { cardNumber = it },
                                                label = { Text("Número do Cartão") },
                                                leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null, tint = FitLimeDark) },
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                singleLine = true,
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.fillMaxWidth().testTag("input_card_number")
                                            )

                                            OutlinedTextField(
                                                value = cardName,
                                                onValueChange = { cardName = it },
                                                label = { Text("Nome impresso no Cartão") },
                                                singleLine = true,
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.fillMaxWidth().testTag("input_card_name")
                                            )

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                OutlinedTextField(
                                                    value = cardExpiry,
                                                    onValueChange = { cardExpiry = it },
                                                    label = { Text("Validade") },
                                                    placeholder = { Text("MM/AA") },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    singleLine = true,
                                                    shape = RoundedCornerShape(12.dp),
                                                    modifier = Modifier.weight(1f).testTag("input_card_expiry")
                                                )
                                                OutlinedTextField(
                                                    value = cardCvv,
                                                    onValueChange = { cardCvv = it },
                                                    label = { Text("CVV") },
                                                    placeholder = { Text("123") },
                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                    singleLine = true,
                                                    shape = RoundedCornerShape(12.dp),
                                                    modifier = Modifier.weight(1f).testTag("input_card_cvv")
                                                )
                                            }

                                            if (selectedMethod == PaymentMethod.CREDIT_CARD && selectedPeriod.months > 1) {
                                                Text(
                                                    text = "Opção de parcelamento: até ${selectedPeriod.months}x sem juros de R$ ${String.format("%.2f", monthlyEquivalent)}",
                                                    fontSize = 12.sp,
                                                    color = FitLimeDark,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }
                                    }

                                    PaymentMethod.PIX -> {
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(14.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.QrCode,
                                                    contentDescription = "QR Code Pix",
                                                    modifier = Modifier.size(72.dp),
                                                    tint = FitLimeDark
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "Chave Pix Copia e Cola instantânea:",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = MaterialTheme.colorScheme.surface,
                                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(8.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = "00020126580014br.gov.bcb.pix013660fit-pix-${activePlan.id}-qr2026",
                                                            fontSize = 11.sp,
                                                            maxLines = 1,
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                        IconButton(
                                                            onClick = {
                                                                clipboardManager.setText(AnnotatedString("00020126580014br.gov.bcb.pix013660fit-pix-${activePlan.id}-qr2026"))
                                                                scope.launch {
                                                                    snackbarHostState.showSnackbar("Código Pix copiado para a área de transferência!")
                                                                }
                                                            }
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.ContentCopy,
                                                                contentDescription = "Copiar Código Pix",
                                                                tint = FitLimeDark,
                                                                modifier = Modifier.size(18.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = "Aprovação em até 10 segundos após a confirmação.",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }

                                    PaymentMethod.PIX_AUTOMATICO -> {
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = FitLime.copy(alpha = 0.12f),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, FitLimeDark.copy(alpha = 0.35f)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Autorenew,
                                                        contentDescription = null,
                                                        tint = FitLimeDark,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Pix Automático / Recorrente (BACEN)",
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = FitLimeDark
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = "Autorize o débito periódico diretamente no seu banco (Itaú, Bradesco, Banco do Brasil, Caixa, Nubank, etc.) sem consumir o limite do cartão.",
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    lineHeight = 16.sp
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                OutlinedTextField(
                                                    value = pixPayerKey,
                                                    onValueChange = { pixPayerKey = it },
                                                    label = { Text("Sua Chave Pix (CPF, E-mail ou Celular)") },
                                                    singleLine = true,
                                                    shape = RoundedCornerShape(12.dp),
                                                    modifier = Modifier.fillMaxWidth().testTag("input_pix_auto_key")
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 4. BOTÃO DE CONFIRMAR ASSINATURA
                    item {
                        PrimaryFitButton(
                            text = "Assinar ${activePlan.name} • R$ ${String.format("%.2f", totalAmount)}",
                            onClick = {
                                onConfirmSubscription(activePlan.id, selectedPeriod, selectedMethod)
                                showSuccessDialog = true
                            },
                            testTag = "btn_confirm_subscription"
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = FitLimeDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Garantia de 7 dias • Cancele quando quiser sem taxas",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                // TAB 1: 📆 MINHA ASSINATURA ATUAL
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(2.dp, FitLimeDark),
                            modifier = Modifier.fillMaxWidth().testTag("my_subscription_card")
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Status da Assinatura",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = userSubscription.planName,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (userSubscription.status.startsWith("Ativa")) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                    ) {
                                        Text(
                                            text = userSubscription.status,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (userSubscription.status.startsWith("Ativa")) Color(0xFF166534) else Color(0xFF991B1B),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Valor Cobrado",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = userSubscription.currentBillingAmount,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = FitLimeDark
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Próxima Renovação",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = userSubscription.nextRenewalDate,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Método de Cobrança",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "${userSubscription.paymentMethod.iconEmoji} ${userSubscription.paymentMethod.title}",
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Periodicidade",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = userSubscription.billingPeriod.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Renovação automática switch
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Renovação Automática",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "Renova automaticamente no vencimento",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Switch(
                                        checked = userSubscription.autoRenew,
                                        onCheckedChange = {
                                            if (!it) showCancelDialog = true
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = FitLimeDark
                                        ),
                                        modifier = Modifier.testTag("switch_auto_renew")
                                    )
                                }
                            }
                        }
                    }

                    // Ações de gerenciamento
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Gerenciamento do Plano",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                PrimaryFitButton(
                                    text = "Trocar de Plano ou Periodicidade",
                                    onClick = { selectedTab = 0 },
                                    testTag = "btn_change_plan"
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                TextButton(
                                    onClick = { showCancelDialog = true },
                                    modifier = Modifier.fillMaxWidth().testTag("btn_cancel_subscription")
                                ) {
                                    Text(
                                        text = "Cancelar Assinatura",
                                        color = Color(0xFFDC2626),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    // Histórico de Faturas
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Receipt,
                                        contentDescription = null,
                                        tint = FitLimeDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Histórico de Faturas Recentes",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                InvoiceHistoryRow(
                                    date = "16/09/2026",
                                    description = "${userSubscription.planName} (${userSubscription.billingPeriod.title})",
                                    amount = userSubscription.currentBillingAmount.substringBefore("(").trim(),
                                    status = "Pago",
                                    onDownload = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Comprovante enviado para seu e-mail!")
                                        }
                                    }
                                )

                                Divider(modifier = Modifier.padding(vertical = 8.dp))

                                InvoiceHistoryRow(
                                    date = "16/08/2026",
                                    description = "60+fit Essencial (Mensal)",
                                    amount = "R$ 79,90",
                                    status = "Pago",
                                    onDownload = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Comprovante enviado para seu e-mail!")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Modal de Sucesso na Assinatura
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            icon = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(FitLime.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = FitLimeDark,
                        modifier = Modifier.size(36.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Assinatura Ativada!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Parabéns! O seu plano ${activePlan.name} foi contratado com sucesso.",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "• Período: ${selectedPeriod.title}\n• Forma: ${selectedMethod.iconEmoji} ${selectedMethod.title}\n• Valor total: R$ ${String.format("%.2f", totalAmount)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = FitLimeDark
                    )
                    Text(
                        text = "Seus treinos, avaliações e suporte já estão liberados no app!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                PrimaryFitButton(
                    text = "Acessar Meu Treino",
                    onClick = {
                        showSuccessDialog = false
                        selectedTab = 1
                    },
                    testTag = "btn_modal_success_continue"
                )
            }
        )
    }

    // Modal de Cancelamento de Assinatura
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            icon = {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Deseja cancelar sua assinatura?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "Seu acesso continuará ativo até o final do período contratado (${userSubscription.nextRenewalDate}). Não haverá renovação automática após essa data.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                PrimaryFitButton(
                    text = "Confirmar Cancelamento",
                    onClick = {
                        onCancelSubscription()
                        showCancelDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Assinatura cancelada. Válida até ${userSubscription.nextRenewalDate}")
                        }
                    },
                    testTag = "btn_confirm_cancel"
                )
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Manter Plano")
                }
            }
        )
    }
}

@Composable
private fun PlanProductCard(
    plan: PlanItem,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) FitLime.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) FitLimeDark else MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("plan_card_${plan.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plan.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (plan.isRecommended) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(FitLimeDark)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Geriátrico",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    if (plan.isCreatedByProfessional) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF2563EB))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Criado pelo Personal",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Icon(
                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = if (isSelected) "Selecionado" else "Não selecionado",
                    tint = if (isSelected) FitLimeDark else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plan.price,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = FitLimeDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plan.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )

            if (plan.features.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))

                plan.features.forEach { feat ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = FitLimeDark,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = feat,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodOptionRow(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) FitLime.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) FitLimeDark else MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("method_${method.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = method.iconEmoji,
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = method.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = method.subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isSelected) FitLimeDark else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun InvoiceHistoryRow(
    date: String,
    description: String,
    amount: String,
    status: String,
    onDownload: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Data: $date • $status",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = amount,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = FitLimeDark
            )
            IconButton(onClick = onDownload) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = "Ver Comprovante",
                    tint = FitLimeDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
