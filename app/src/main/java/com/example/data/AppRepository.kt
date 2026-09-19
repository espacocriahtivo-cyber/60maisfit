package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppRepository {

    private val _currentUserType = MutableStateFlow(UserType.STUDENT)
    val currentUserType: StateFlow<UserType> = _currentUserType.asStateFlow()

    private val _studentProfile = MutableStateFlow(StudentProfile())
    val studentProfile: StateFlow<StudentProfile> = _studentProfile.asStateFlow()

    private val _healthConditions = MutableStateFlow(HealthConditions())
    val healthConditions: StateFlow<HealthConditions> = _healthConditions.asStateFlow()

    private val _physicalAssessment = MutableStateFlow(PhysicalAssessment())
    val physicalAssessment: StateFlow<PhysicalAssessment> = _physicalAssessment.asStateFlow()

    private val _currentWorkout = MutableStateFlow(
        Workout(
            code = "Treino A",
            title = "Força e funcionalidade",
            exercises = listOf(
                Exercise(
                    id = "ex_1",
                    name = "Aquecimento articular",
                    sets = 3,
                    reps = 12,
                    restSeconds = 30,
                    instruction = "Gire os ombros e os tornozelos suavemente para lubrificar as articulações."
                ),
                Exercise(
                    id = "ex_2",
                    name = "Elevação de joelho com elástico",
                    sets = 3,
                    reps = 12,
                    restSeconds = 30,
                    instruction = "Mantenha a postura ereta e realize o movimento de forma controlada."
                ),
                Exercise(
                    id = "ex_3",
                    name = "Sentar e levantar da cadeira",
                    sets = 3,
                    reps = 12,
                    restSeconds = 30,
                    instruction = "Mantenha os pés afastados na largura dos ombros e o abdômen contraído."
                ),
                Exercise(
                    id = "ex_4",
                    name = "Remada baixa com elástico",
                    sets = 3,
                    reps = 12,
                    restSeconds = 30,
                    instruction = "Puxe os cotovelos para trás aproximando as escápulas com as costas retas."
                ),
                Exercise(
                    id = "ex_5",
                    name = "Extensão de joelho sentado",
                    sets = 3,
                    reps = 12,
                    restSeconds = 30,
                    instruction = "Estenda o joelho lentamente sem forçar a articulação, segurando no topo por 1 segundo."
                )
            )
        )
    )
    val currentWorkout: StateFlow<Workout> = _currentWorkout.asStateFlow()

    private val _weightHistory = MutableStateFlow(
        listOf(
            WeightHistoryPoint("Jan", 70.0f),
            WeightHistoryPoint("Fev", 68.5f),
            WeightHistoryPoint("Mar", 66.0f),
            WeightHistoryPoint("Abr", 64.2f),
            WeightHistoryPoint("Mai", 63.0f),
            WeightHistoryPoint("Jun", 62.0f)
        )
    )
    val weightHistory: StateFlow<List<WeightHistoryPoint>> = _weightHistory.asStateFlow()

    private val _reminders = MutableStateFlow(
        listOf(
            ReminderItem("rem_1", "Treino de hoje", "08:00", "workout", true),
            ReminderItem("rem_2", "Tomar água", "10:00", "water", true),
            ReminderItem("rem_3", "Registrar pressão", "12:00", "pressure", true),
            ReminderItem("rem_4", "Avaliação mensal", "01/10", "assessment", false),
            ReminderItem("rem_5", "Consulta médica", "15/10", "doctor", true)
        )
    )
    val reminders: StateFlow<List<ReminderItem>> = _reminders.asStateFlow()

    private val _plans = MutableStateFlow(
        listOf(
            PlanItem(
                id = "plan_essencial",
                name = "60+fit Essencial",
                monthlyPrice = 79.90,
                price = "R$ 79,90/mês",
                description = "Plano base com treinos personalizados e acesso completo ao app.",
                features = listOf(
                    "Aplicativo 60+fit completo",
                    "Biblioteca de exercícios com vídeos e áudio",
                    "Treinos personalizados de força e funcionalidade",
                    "Acompanhamento básico",
                    "Evolução de peso e bem-estar"
                ),
                isRecommended = false
            ),
            PlanItem(
                id = "plan_gerontologico",
                name = "60+fit Gerontológico",
                monthlyPrice = 129.90,
                price = "R$ 129,90/mês",
                description = "Cuidado integral com avaliação funcional e protocolos geriátricos específicos.",
                features = listOf(
                    "Tudo do Essencial",
                    "Anamnese detalhada e histórico de saúde",
                    "Avaliação física completa",
                    "Avaliações funcionais (marcha, sentar e levantar)",
                    "Acompanhamento da evolução",
                    "Protocolos específicos para grupos especiais (artrose, osteoporose, hipertensão)"
                ),
                isRecommended = true
            ),
            PlanItem(
                id = "plan_premium",
                name = "60+fit Premium",
                monthlyPrice = 199.90,
                price = "R$ 199,90 – 249,90/mês",
                description = "Consultoria individual com acompanhamento próximo, relatórios e contato direto.",
                features = listOf(
                    "Tudo do Gerontológico",
                    "Acompanhamento mais próximo com ajustes frequentes",
                    "Avaliações periódicas",
                    "Vídeos personalizados gravados pelo profissional",
                    "Contato profissional direto",
                    "Relatórios de evolução para médicos e familiares"
                ),
                isRecommended = false
            )
        )
    )
    val plans: StateFlow<List<PlanItem>> = _plans.asStateFlow()

    private val _selectedPlanId = MutableStateFlow("plan_gerontologico")
    val selectedPlanId: StateFlow<String> = _selectedPlanId.asStateFlow()

    private val _userSubscription = MutableStateFlow(
        UserSubscription(
            planId = "plan_gerontologico",
            planName = "60+fit Gerontológico",
            monthlyPrice = 129.90,
            billingPeriod = BillingPeriod.MENSAL,
            paymentMethod = PaymentMethod.PIX_AUTOMATICO,
            status = "Ativa",
            currentBillingAmount = "R$ 129,90/mês",
            nextRenewalDate = "16/10/2026",
            autoRenew = true,
            lastPaymentDate = "16/09/2026"
        )
    )
    val userSubscription: StateFlow<UserSubscription> = _userSubscription.asStateFlow()

    private val _professionalStudents = MutableStateFlow(
        listOf(
            ProfessionalStudent("s1", "Maria Silva", 68, "Força e Funcionalidade", "Hoje às 08:30", 92),
            ProfessionalStudent("s2", "José de Alencar", 72, "Prevenção de Quedas", "Ontem às 15:00", 85),
            ProfessionalStudent("s3", "Antônia Santos", 65, "Mobilidade e Equilíbrio", "12/09 às 10:00", 78),
            ProfessionalStudent("s4", "Carlos Eduardo", 70, "Fortalecimento Geral", "10/09 às 09:15", 95)
        )
    )
    val professionalStudents: StateFlow<List<ProfessionalStudent>> = _professionalStudents.asStateFlow()

    private val _accessibilityState = MutableStateFlow(com.example.ui.theme.AccessibilityState())
    val accessibilityState: StateFlow<com.example.ui.theme.AccessibilityState> = _accessibilityState.asStateFlow()

    fun updateAccessibility(state: com.example.ui.theme.AccessibilityState) {
        _accessibilityState.value = state
    }

    fun setUserType(type: UserType) {
        _currentUserType.value = type
    }

    fun updateStudentProfile(profile: StudentProfile) {
        _studentProfile.value = profile
    }

    fun updateHealthConditions(conditions: HealthConditions) {
        _healthConditions.value = conditions
    }

    fun updatePhysicalAssessment(assessment: PhysicalAssessment) {
        _physicalAssessment.value = assessment
    }

    fun toggleReminder(id: String) {
        _reminders.update { list ->
            list.map { if (it.id == id) it.copy(enabled = !it.enabled) else it }
        }
    }

    fun addReminder(title: String, timeOrDate: String, iconType: String) {
        val newItem = ReminderItem(
            id = "rem_${System.currentTimeMillis()}",
            title = title,
            timeOrDate = timeOrDate,
            iconType = iconType,
            enabled = true
        )
        _reminders.update { it + newItem }
    }

    fun selectPlan(planId: String) {
        _selectedPlanId.value = planId
    }

    fun addProfessionalPlan(plan: PlanItem) {
        _plans.update { it + plan }
    }

    fun deleteProfessionalPlan(planId: String) {
        _plans.update { list -> list.filterNot { it.id == planId } }
        if (_selectedPlanId.value == planId) {
            _selectedPlanId.value = "plan_gerontologico"
        }
    }

    fun subscribeToPlan(
        planId: String,
        period: BillingPeriod,
        method: PaymentMethod
    ) {
        val selected = _plans.value.find { it.id == planId } ?: _plans.value.first()
        val (total, monthly) = selected.calculatePrice(period)
        val formattedAmount = "R$ ${String.format("%.2f", monthly)}/mês (${period.title} total: R$ ${String.format("%.2f", total)})"

        _userSubscription.value = UserSubscription(
            planId = selected.id,
            planName = selected.name,
            monthlyPrice = monthly,
            billingPeriod = period,
            paymentMethod = method,
            status = "Ativa",
            currentBillingAmount = formattedAmount,
            nextRenewalDate = when (period) {
                BillingPeriod.MENSAL -> "16/10/2026"
                BillingPeriod.TRIMESTRAL -> "16/12/2026"
                BillingPeriod.SEMESTRAL -> "16/03/2027"
                BillingPeriod.ANUAL -> "16/09/2027"
            },
            autoRenew = true,
            lastPaymentDate = "16/09/2026"
        )
        _selectedPlanId.value = planId
    }

    fun cancelSubscription() {
        _userSubscription.update {
            it.copy(status = "Cancelada (Válida até ${it.nextRenewalDate})", autoRenew = false)
        }
    }

    fun completeExercise(exerciseId: String) {
        _currentWorkout.update { workout ->
            val updated = workout.exercises.map {
                if (it.id == exerciseId) it.copy(completed = true) else it
            }
            workout.copy(exercises = updated)
        }
    }

    // --------------------------------------------------------------------------
    // CONFIGURAÇÃO E SINCRONIZAÇÃO BANCO DE DADOS HOSTINGER
    // --------------------------------------------------------------------------
    private val hostingerApiClient = HostingerApiClient()

    private val _hostingerConfig = MutableStateFlow(HostingerConfigData())
    val hostingerConfig: StateFlow<HostingerConfigData> = _hostingerConfig.asStateFlow()

    fun updateHostingerConfig(serverUrl: String, apiToken: String) {
        _hostingerConfig.update {
            it.copy(serverUrl = serverUrl, apiToken = apiToken)
        }
    }

    suspend fun testHostingerConnection(serverUrl: String, apiToken: String): Result<String> {
        _hostingerConfig.update { it.copy(isSyncing = true) }
        val result = hostingerApiClient.testConnection(serverUrl, apiToken)
        _hostingerConfig.update {
            it.copy(
                isSyncing = false,
                isConnected = result.isSuccess,
                serverUrl = serverUrl,
                apiToken = apiToken,
                serverInfo = if (result.isSuccess) result.getOrNull().orEmpty() else it.serverInfo,
                lastSyncMessage = if (result.isSuccess) "Conexão confirmada com sucesso!" else "Falha: ${result.exceptionOrNull()?.localizedMessage}"
            )
        }
        return result
    }

    suspend fun createHostingerTables(): Result<String> {
        val currentConfig = _hostingerConfig.value
        val result = hostingerApiClient.createTables(currentConfig.serverUrl, currentConfig.apiToken)
        if (result.isSuccess) {
            _hostingerConfig.update {
                it.copy(
                    isConnected = true,
                    serverInfo = result.getOrNull().orEmpty(),
                    lastSyncMessage = "Tabelas criadas com sucesso no MySQL!"
                )
            }
        }
        return result
    }

    suspend fun syncWithHostinger(): Result<String> {
        val currentConfig = _hostingerConfig.value
        _hostingerConfig.update { it.copy(isSyncing = true) }

        val syncResult = hostingerApiClient.syncAll(
            serverUrl = currentConfig.serverUrl,
            token = currentConfig.apiToken,
            student = _studentProfile.value,
            subscription = _userSubscription.value,
            plans = _plans.value
        )

        _hostingerConfig.update {
            it.copy(
                isSyncing = false,
                isConnected = syncResult.isSuccess,
                lastSyncTime = if (syncResult.isSuccess) "Agora" else it.lastSyncTime,
                lastSyncMessage = if (syncResult.isSuccess) {
                    syncResult.getOrNull()?.first ?: "Sincronizado com sucesso!"
                } else {
                    "Erro ao sincronizar: ${syncResult.exceptionOrNull()?.localizedMessage}"
                }
            )
        }

        if (syncResult.isSuccess) {
            val returnedPlans = syncResult.getOrNull()?.second.orEmpty()
            if (returnedPlans.isNotEmpty()) {
                _plans.value = returnedPlans
            }
            return Result.success(syncResult.getOrNull()?.first ?: "Dados sincronizados com o banco de dados Hostinger!")
        } else {
            return Result.failure(syncResult.exceptionOrNull() ?: Exception("Falha na sincronização"))
        }
    }

    companion object {
        val instance = AppRepository()
    }
}
