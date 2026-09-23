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

    private val assessmentBaseline = PhysicalAssessment(
        id = "eval_001",
        date = "10/01/2026",
        responsibleProfessional = "Dr. Roberto Santos (CREF 038910)",
        weightKg = "65.0",
        heightCm = "158",
        heightM = "1,58",
        armCircumferenceCm = "29.8",
        waistCircumferenceCm = "83.0",
        abdomenCircumferenceCm = "90.0",
        hipCircumferenceCm = "101.0",
        thighCircumferenceCm = "53.5",
        calfCircumferenceCm = "33.0",
        tricepsSkinfoldMm = "21.0",
        subscapularSkinfoldMm = "20.0",
        iliacCrestSkinfoldMm = "24.5",
        flexibilityCm = "16",
        flexibilityRating = "Reduzida",
        handgripStrengthKgf = "17.5",
        gaitSpeed3mSeconds = "3.7",
        systolicPressureMmHg = "130",
        diastolicPressureMmHg = "85",
        bloodPressure = "130 / 85 mmHg",
        oxygenSaturation = 96,
        heartRateBpm = 78,
        goniometryNotes = "Joelho D: flexão 110° / extensão -5°. Ombro D: flexão 150°.",
        balanceTestResult = "Apoio unipodal: 6s (D) / 5s (E). Risco leve de instabilidade.",
        agaScore = "Independente básico, porém com insegurança em escadas e velocidade de marcha limítrofe.",
        professionalNotes = "Início do programa 60+fit. Foco em marcha segura, força de quadríceps e propriocepção.",
        sitToStandReps = 10,
        walkDistanceMeters = 380
    )

    private val assessmentIntermediate = PhysicalAssessment(
        id = "eval_002",
        date = "15/06/2026",
        responsibleProfessional = "Dra. Mariana Lima (CREF 045123)",
        weightKg = "63.5",
        heightCm = "158",
        heightM = "1,58",
        armCircumferenceCm = "29.0",
        waistCircumferenceCm = "80.5",
        abdomenCircumferenceCm = "87.0",
        hipCircumferenceCm = "99.5",
        thighCircumferenceCm = "53.0",
        calfCircumferenceCm = "33.8",
        tricepsSkinfoldMm = "19.5",
        subscapularSkinfoldMm = "18.0",
        iliacCrestSkinfoldMm = "22.0",
        flexibilityCm = "20",
        flexibilityRating = "Em evolução",
        handgripStrengthKgf = "20.0",
        gaitSpeed3mSeconds = "3.2",
        systolicPressureMmHg = "125",
        diastolicPressureMmHg = "82",
        bloodPressure = "125 / 82 mmHg",
        oxygenSaturation = 97,
        heartRateBpm = 75,
        goniometryNotes = "Joelho D: flexão 118° / extensão -2°. Ombro D: flexão 160°.",
        balanceTestResult = "Apoio unipodal: 10s (D) / 8s (E). Semi-tandem estável.",
        agaScore = "Independente funcional. Sem histórico recente de desequilíbrio.",
        professionalNotes = "Evolução perceptível na força e equilíbrio. Aluna mais confiante.",
        sitToStandReps = 12,
        walkDistanceMeters = 405
    )

    private val assessmentLatest = PhysicalAssessment(
        id = "eval_003",
        date = "22/09/2026",
        responsibleProfessional = "Dra. Mariana Lima (CREF 045123)",
        weightKg = "62.0",
        heightCm = "158",
        heightM = "1,58",
        armCircumferenceCm = "28.5",
        waistCircumferenceCm = "78.0",
        abdomenCircumferenceCm = "84.0",
        hipCircumferenceCm = "98.0",
        thighCircumferenceCm = "52.0",
        calfCircumferenceCm = "34.5",
        tricepsSkinfoldMm = "18.0",
        subscapularSkinfoldMm = "16.5",
        iliacCrestSkinfoldMm = "20.0",
        flexibilityCm = "24",
        flexibilityRating = "Adequada para a faixa etária",
        handgripStrengthKgf = "22.5",
        gaitSpeed3mSeconds = "2.8",
        systolicPressureMmHg = "120",
        diastolicPressureMmHg = "80",
        bloodPressure = "120 / 80 mmHg",
        oxygenSaturation = 98,
        heartRateBpm = 72,
        goniometryNotes = "Joelho D: flexão 125° / extensão 0°. Ombro D: flexão 165°.",
        balanceTestResult = "Apoio unipodal: 14s (D) / 12s (E). Semi-tandem seguro e estável.",
        agaScore = "Independente para ABVD e AIVD. Cognição e humor preservados. Sem sinais de fragilidade.",
        professionalNotes = "Evolução clínica favorável. Ganho consistente de força e redução no tempo de marcha.",
        sitToStandReps = 14,
        walkDistanceMeters = 420
    )

    private val _assessmentHistory = MutableStateFlow<List<PhysicalAssessment>>(
        listOf(assessmentLatest, assessmentIntermediate, assessmentBaseline)
    )
    val assessmentHistory: StateFlow<List<PhysicalAssessment>> = _assessmentHistory.asStateFlow()

    private val _physicalAssessment = MutableStateFlow(assessmentLatest)
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
            ProfessionalStudent("s1", "Maria Silva", 68, "Força e Funcionalidade", "Hoje às 08:30", 92, "60+fit Gerontológico"),
            ProfessionalStudent("s2", "José de Alencar", 72, "Prevenção de Quedas", "Ontem às 15:00", 85, "60+fit Essencial"),
            ProfessionalStudent("s3", "Antônia Santos", 65, "Mobilidade e Equilíbrio", "12/09 às 10:00", 78, "60+fit Gerontológico"),
            ProfessionalStudent("s4", "Carlos Eduardo", 70, "Fortalecimento Geral", "10/09 às 09:15", 95, "60+fit Personal")
        )
    )
    val professionalStudents: StateFlow<List<ProfessionalStudent>> = _professionalStudents.asStateFlow()

    private val mariaFunctionalProfile = StudentFunctionalProfile(
        studentId = "s1",
        studentName = "Maria Silva",
        age = 68,
        plan = "60+fit Gerontológico",
        frequency = "3x por semana",
        lastAssessmentDate = "22/09/2026",
        overallScore = "11 / 12 (Robusto)",
        fallRiskLevel = "Baixo Risco (18%)",
        adherencePercent = 92,
        dimensions = listOf(
            FunctionalDimension(
                id = "dim_forca",
                name = "FORÇA",
                icon = "💪",
                primaryMetric = "Preensão palmar",
                initialValue = "21 kg",
                currentValue = "25 kg",
                evolutionLabel = "↑ evolução (+4 kg / +19%)",
                isPositive = true,
                historyPoints = listOf(
                    "Jan" to 21.0f,
                    "Mar" to 22.5f,
                    "Jun" to 23.8f,
                    "Set" to 25.0f
                ),
                secondaryMetric = "Sentar e Levantar 30s",
                secondaryInitial = "10 reps",
                secondaryCurrent = "14 reps",
                secondaryEvolution = "↑ evolução (+4 reps / +40%)",
                clinicalGuideline = "Massa muscular e dinamometria acima do ponto de corte EWGSOP2 (>16 kgf para mulheres). Proteção osteometabólica excelente."
            ),
            FunctionalDimension(
                id = "dim_mobilidade",
                name = "MOBILIDADE",
                icon = "🦵",
                primaryMetric = "Timed Up & Go (TUG)",
                initialValue = "12.4 s",
                currentValue = "9.1 s",
                evolutionLabel = "↑ evolução (-3.3 s / 26.6% mais ágil)",
                isPositive = true,
                historyPoints = listOf(
                    "Jan" to 12.4f,
                    "Mar" to 11.2f,
                    "Jun" to 10.0f,
                    "Set" to 9.1f
                ),
                secondaryMetric = "Flexão de Joelho / ADM",
                secondaryInitial = "105°",
                secondaryCurrent = "125°",
                secondaryEvolution = "↑ evolução (+20°)",
                clinicalGuideline = "Tempo TUG < 10 segundos indica mobilidade independente e segura para atividades da vida diária."
            ),
            FunctionalDimension(
                id = "dim_equilibrio",
                name = "EQUILÍBRIO",
                icon = "⚖️",
                primaryMetric = "Apoio Unipodal (Olhos abertos)",
                initialValue = "6 s",
                currentValue = "15 s",
                evolutionLabel = "↑ evolução (+9 s / +150% estabilidade)",
                isPositive = true,
                historyPoints = listOf(
                    "Jan" to 6.0f,
                    "Mar" to 9.0f,
                    "Jun" to 12.0f,
                    "Set" to 15.0f
                ),
                secondaryMetric = "Teste Semi-Tandem e Tandem",
                secondaryInitial = "4 s (instável)",
                secondaryCurrent = "10+ s (estável)",
                secondaryEvolution = "↑ evolução postural",
                clinicalGuideline = "Apoio unipodal > 10s é preditor independente de segurança postural e ausência de risco agudo de quedas."
            ),
            FunctionalDimension(
                id = "dim_marcha",
                name = "MARCHA",
                icon = "🚶",
                primaryMetric = "Velocidade de Marcha (3 metros)",
                initialValue = "0.81 m/s (3.7 s)",
                currentValue = "1.07 m/s (2.8 s)",
                evolutionLabel = "↑ evolução (+32% mais rápida)",
                isPositive = true,
                historyPoints = listOf(
                    "Jan" to 0.81f,
                    "Mar" to 0.90f,
                    "Jun" to 0.98f,
                    "Set" to 1.07f
                ),
                secondaryMetric = "Cadência de Marcha",
                secondaryInitial = "88 passos/min",
                secondaryCurrent = "104 passos/min",
                secondaryEvolution = "↑ evolução (+18%)",
                clinicalGuideline = "Velocidade > 1.0 m/s classifica a aluna como idosa comunitária de padrão robusto, com travessia de rua segura."
            ),
            FunctionalDimension(
                id = "dim_flexibilidade",
                name = "FLEXIBILIDADE",
                icon = "🧘",
                primaryMetric = "Sentar e Alcançar (Banco de Wells)",
                initialValue = "18 cm",
                currentValue = "26 cm",
                evolutionLabel = "↑ evolução (+8 cm / +44%)",
                isPositive = true,
                historyPoints = listOf(
                    "Jan" to 18.0f,
                    "Mar" to 20.0f,
                    "Jun" to 23.0f,
                    "Set" to 26.0f
                ),
                secondaryMetric = "Alcance das Mãos nas Costas",
                secondaryInitial = "-8 cm",
                secondaryCurrent = "-2 cm",
                secondaryEvolution = "↑ evolução (+6 cm)",
                clinicalGuideline = "Alongamento da cadeia posterior alivia sobrecarga lombar e otimiza amplitude da passada."
            ),
            FunctionalDimension(
                id = "dim_quedas",
                name = "RISCO DE QUEDAS",
                icon = "🛡️",
                primaryMetric = "Índice Clínico de Risco de Quedas",
                initialValue = "70% (Alto Risco)",
                currentValue = "18% (Baixo Risco)",
                evolutionLabel = "↓ evolução (Redução de 52% no risco)",
                isPositive = true,
                historyPoints = listOf(
                    "Jan" to 70.0f,
                    "Mar" to 52.0f,
                    "Jun" to 34.0f,
                    "Set" to 18.0f
                ),
                secondaryMetric = "Ocorrência de Quedas",
                secondaryInitial = "1 queda / semestre",
                secondaryCurrent = "0 quedas registradas",
                secondaryEvolution = "Zero quedas no período",
                clinicalGuideline = "Transição clínica de Alto Risco para Baixo Risco de Quedas devido ao fortalecimento de dorsiflexores e propriocepção."
            ),
            FunctionalDimension(
                id = "dim_capacidade",
                name = "CAPACIDADE FUNCIONAL",
                icon = "🏆",
                primaryMetric = "Escore SPPB / AGA Global",
                initialValue = "7 / 12 (Fragilidade moderada)",
                currentValue = "11 / 12 (Robusto / Independente)",
                evolutionLabel = "↑ evolução (+57% de capacidade)",
                isPositive = true,
                historyPoints = listOf(
                    "Jan" to 7.0f,
                    "Mar" to 9.0f,
                    "Jun" to 10.0f,
                    "Set" to 11.0f
                ),
                secondaryMetric = "Autonomia ABVD / AIVD",
                secondaryInitial = "Parcialmente dependente",
                secondaryCurrent = "100% Independente",
                secondaryEvolution = "Total autonomia",
                clinicalGuideline = "Escore SPPB 11/12 confirma autonomia plena para tarefas do dia a dia e excelente reserva fisiológica."
            )
        )
    )

    private val _selectedStudentId = MutableStateFlow("s1")
    val selectedStudentId: StateFlow<String> = _selectedStudentId.asStateFlow()

    fun selectStudent(studentId: String) {
        _selectedStudentId.value = studentId
    }

    fun getFunctionalProfileForStudent(studentId: String): StudentFunctionalProfile {
        return if (studentId == "s1") {
            mariaFunctionalProfile
        } else {
            val student = _professionalStudents.value.find { it.id == studentId }
                ?: _professionalStudents.value.first()
            mariaFunctionalProfile.copy(
                studentId = student.id,
                studentName = student.name,
                age = student.age,
                plan = student.plan,
                adherencePercent = student.adherencePercent
            )
        }
    }

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
        _assessmentHistory.update { list ->
            val idx = list.indexOfFirst { it.id == assessment.id }
            if (idx >= 0) {
                list.toMutableList().apply { set(idx, assessment) }
            } else {
                listOf(assessment) + list
            }
        }
        _studentProfile.update {
            it.copy(
                weightKg = assessment.weightKg,
                heightM = assessment.heightM
            )
        }
    }

    fun getComparisonWithPrevious(target: PhysicalAssessment): AssessmentComparison {
        val history = _assessmentHistory.value
        val currentIndex = history.indexOfFirst { it.id == target.id }
        val previous = if (currentIndex != -1 && currentIndex + 1 < history.size) {
            history[currentIndex + 1]
        } else if (history.size > 1 && history.first().id != target.id) {
            history.first()
        } else if (history.size > 1) {
            history[1]
        } else {
            null
        }

        val curW = target.weightKg.replace(",", ".").toDoubleOrNull()
        val prevW = previous?.weightKg?.replace(",", ".")?.toDoubleOrNull()

        val weightDeltaKg = if (curW != null && prevW != null) curW - prevW else null
        val weightDeltaPercent = if (weightDeltaKg != null && prevW != null && prevW > 0) {
            (weightDeltaKg / prevW) * 100.0
        } else null

        val curBmi = target.calculateBmi()
        val prevBmi = previous?.calculateBmi() ?: 0.0
        val bmiDelta = if (curBmi > 0 && prevBmi > 0) curBmi - prevBmi else null

        val curGrip = target.handgripStrengthKgf.replace(",", ".").toDoubleOrNull()
        val prevGrip = previous?.handgripStrengthKgf?.replace(",", ".")?.toDoubleOrNull()
        val gripDelta = if (curGrip != null && prevGrip != null) curGrip - prevGrip else null

        val curGait = target.gaitSpeed3mSeconds.replace(",", ".").toDoubleOrNull()
        val prevGait = previous?.gaitSpeed3mSeconds?.replace(",", ".")?.toDoubleOrNull()
        val gaitDelta = if (curGait != null && prevGait != null) curGait - prevGait else null

        val curCalf = target.calfCircumferenceCm.replace(",", ".").toDoubleOrNull()
        val prevCalf = previous?.calfCircumferenceCm?.replace(",", ".")?.toDoubleOrNull()
        val calfDelta = if (curCalf != null && prevCalf != null) curCalf - prevCalf else null

        return AssessmentComparison(
            current = target,
            previous = previous,
            weightDeltaKg = weightDeltaKg,
            weightDeltaPercent = weightDeltaPercent,
            bmiDelta = bmiDelta,
            handgripDeltaKgf = gripDelta,
            gaitDeltaSec = gaitDelta,
            calfDeltaCm = calfDelta
        )
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

    // ==========================================
    // SISTEMA DE PRESCRIÇÃO DE TREINO (TELA 6)
    // ==========================================
    fun getDefaultPrescription(studentId: String = "s1", studentName: String = "Maria Silva"): WorkoutPrescription {
        return WorkoutPrescription(
            studentId = studentId,
            studentName = studentName,
            workoutCode = "Treino A",
            professionalName = "Prof. Dra. Camila Rocha (CREF 098452-G/SP)",
            date = "22/09/2026",
            sequences = listOf(
                WorkoutSequence(
                    id = 1,
                    title = "Sequência 1 — Mobilidade",
                    subtitle = "Aquecimento articular e amplitude de movimento",
                    emoji = "🧘",
                    exercises = listOf(
                        PrescriptionExercise("seq1_1", "mobilidade cervical", 1, true, 2, "10 reps cada lado", "Peso corporal", 30, "Movimentos suaves de flexão, extensão e rotação"),
                        PrescriptionExercise("seq1_2", "mobilidade de ombros", 1, true, 2, "12 circunduções", "Bastão leve", 30, "Circunduções escapulares e elevações sem dor"),
                        PrescriptionExercise("seq1_3", "mobilidade torácica", 1, true, 2, "10 reps", "Sentado", 30, "Rotações torácicas sentadas com respiração ritmada"),
                        PrescriptionExercise("seq1_4", "mobilidade de quadril", 1, true, 2, "10 reps cada", "Apoio em cadeira", 30, "Círculos pélvicos e aberturas controladas"),
                        PrescriptionExercise("seq1_5", "mobilidade de tornozelo", 1, true, 2, "12 reps", "Sentado / Em pé", 30, "Dorsiflexão e flexão plantar para marcha segura")
                    )
                ),
                WorkoutSequence(
                    id = 2,
                    title = "Sequência 2 — Alongamento",
                    subtitle = "Flexibilidade miotendínea e relaxamento",
                    emoji = "🤸",
                    exercises = listOf(
                        PrescriptionExercise("seq2_1", "membros superiores", 2, true, 2, "20 a 30s", "Peso corporal", 30, "Alongamento de deltoides, peitoral e tríceps"),
                        PrescriptionExercise("seq2_2", "cadeia posterior", 2, true, 2, "25s cada", "Sentar e alcançar", 30, "Com auxílio de toalha/faixa se necessário"),
                        PrescriptionExercise("seq2_3", "quadríceps", 2, true, 2, "20s cada perna", "Apoio na parede/cadeira", 30, "Sem hiperextensão lombar, joelho apontando para baixo"),
                        PrescriptionExercise("seq2_4", "panturrilha", 2, true, 2, "25s cada lado", "Contra parede", 30, "Calcanhar firme no solo e tronco alinhado"),
                        PrescriptionExercise("seq2_5", "quadril", 2, true, 2, "20s cada lado", "Sentado (posição 4)", 30, "Alongamento suave de glúteos e rotadores")
                    )
                ),
                WorkoutSequence(
                    id = 3,
                    title = "Sequência 3 — Força",
                    subtitle = "Reserva muscular, combate à sarcopenia e massa óssea",
                    emoji = "💪",
                    exercises = listOf(
                        PrescriptionExercise("seq3_1", "agachamento", 3, true, 3, "10 a 12 reps", "Cadeira / Peso corporal", 45, "Agachamento em caixa com alinhamento de joelhos"),
                        PrescriptionExercise("seq3_2", "leg press", 3, false, 3, "10 reps", "Máquina / Carga leve-moderada", 60, "Sem travar os joelhos no final da extensão"),
                        PrescriptionExercise("seq3_3", "extensão de joelho", 3, true, 3, "12 reps", "Caneleira 1-2kg", 45, "Pausa isométrica de 1s no topo do movimento"),
                        PrescriptionExercise("seq3_4", "flexão de joelho", 3, true, 3, "10 reps cada", "Caneleira 1kg", 45, "Em pé com apoio na cadeira, foco em isquiotibiais"),
                        PrescriptionExercise("seq3_5", "remada", 3, true, 3, "12 reps", "Elástico médio", 45, "Aproximação consciente das escápulas"),
                        PrescriptionExercise("seq3_6", "puxada", 3, false, 3, "10 reps", "Elástico superior", 45, "Puxada alta adaptada com controle"),
                        PrescriptionExercise("seq3_7", "supino", 3, true, 2, "10 a 12 reps", "Halteres de 1kg", 45, "No colchonete ou bola suíça com segurança"),
                        PrescriptionExercise("seq3_8", "exercícios de braços", 3, true, 2, "12 reps", "Halteres de 1.5kg", 45, "Rosca bíceps alternada e tríceps testa adaptado"),
                        PrescriptionExercise("seq3_9", "panturrilha", 3, true, 3, "15 reps", "Degrau com apoio", 45, "Elevação plantar completa e descida lenta")
                    )
                ),
                WorkoutSequence(
                    id = 4,
                    title = "Sequência 4 — Funcional",
                    subtitle = "Autonomia nas atividades cotidianas e independência",
                    emoji = "⚡",
                    exercises = listOf(
                        PrescriptionExercise("seq4_1", "sentar e levantar", 4, true, 3, "12 reps", "Cadeira padrão", 45, "Braços cruzados no peito sem impulso dos braços"),
                        PrescriptionExercise("seq4_2", "subir degrau", 4, true, 2, "10 reps cada perna", "Step 10-15cm", 45, "Com apoio próximo de segurança"),
                        PrescriptionExercise("seq4_3", "carregar objetos", 4, true, 2, "30 metros", "2kg em cada mão", 45, "Postura ereta e passos firmes"),
                        PrescriptionExercise("seq4_4", "levantar objetos", 4, true, 2, "8 reps", "Caixa de 2kg", 45, "Flexionando joelhos e quadril, mantendo a coluna ereta"),
                        PrescriptionExercise("seq4_5", "deslocamentos", 4, true, 3, "20 metros", "Livre", 45, "Ida de frente e retorno com desaceleração segura"),
                        PrescriptionExercise("seq4_6", "exercícios de dupla tarefa", 4, true, 2, "1 min", "Cognitivo-motor", 45, "Caminhada nomeando animais, frutas ou cidades")
                    )
                ),
                WorkoutSequence(
                    id = 5,
                    title = "Sequência 5 — Equilíbrio e marcha",
                    subtitle = "Estabilidade postural, propriocepção e passada segura",
                    emoji = "⚖️",
                    exercises = listOf(
                        PrescriptionExercise("seq5_1", "apoio bipodal", 5, true, 3, "30s", "Pés juntos", 30, "Olhos abertos e depois fechados com apoio ao lado"),
                        PrescriptionExercise("seq5_2", "apoio unipodal adaptado", 5, true, 3, "15s cada perna", "Apoio de 1 dedo", 30, "Manter olhar em ponto fixo horizontal"),
                        PrescriptionExercise("seq5_3", "deslocamento lateral", 5, true, 3, "10 passos cada lado", "Pista segura", 30, "Passadas laterais sem cruzar os pés"),
                        PrescriptionExercise("seq5_4", "caminhada", 5, true, 1, "5 minutos", "Cadência ritmada", 45, "Passos firmes de calcanhar para a ponta"),
                        PrescriptionExercise("seq5_5", "obstáculos baixos", 5, true, 2, "6 ultrapassagens", "Obstáculos de 10cm", 45, "Elevação adequada do pé ao transpor cones baixos"),
                        PrescriptionExercise("seq5_6", "mudanças de direção", 5, true, 2, "Percurso em 8", "Cones baixos", 45, "Curvas suaves com passos curtos e apoio firme"),
                        PrescriptionExercise("seq5_7", "marcha com dupla tarefa", 5, true, 2, "2 minutos", "Contagem regressiva", 45, "Caminhar subtraindo 2 em 2 ou segurando copo")
                    )
                ),
                WorkoutSequence(
                    id = 6,
                    title = "Sequência 6 — Prevenção de quedas",
                    subtitle = "Treinos específicos para proteção e autonomia",
                    emoji = "🛡️",
                    exercises = listOf(
                        PrescriptionExercise("seq6_1", "força de membros inferiores", 6, true, 3, "10 reps", "Peso corporal / Cadeira", 45, "Fortalecimento de dorsiflexores e extensores do quadril"),
                        PrescriptionExercise("seq6_2", "equilíbrio", 6, true, 3, "20s cada base", "Tandem / Semi-tandem", 30, "Postura alinhada e base de suporte desafiada"),
                        PrescriptionExercise("seq6_3", "reação postural", 6, true, 2, "8 reps", "Passo corretivo", 30, "Treino de passo rápido para recuperação do equilíbrio"),
                        PrescriptionExercise("seq6_4", "mobilidade", 6, true, 2, "10 reps", "Giro 360° controlado", 30, "Girar em torno de si mesmo contando os passos"),
                        PrescriptionExercise("seq6_5", "marcha", 6, true, 2, "3 minutos", "Fita no chão", 45, "Aceleração, desaceleração e parada ao comando"),
                        PrescriptionExercise("seq6_6", "transferência sentado/em pé", 6, true, 3, "8 reps perfeitas", "Cadeira firme", 45, "Treino biomecânico da inclinação anterior de tronco")
                    )
                )
            )
        )
    }

    private val _currentPrescription = MutableStateFlow<WorkoutPrescription>(getDefaultPrescription())
    val currentPrescription: StateFlow<WorkoutPrescription> = _currentPrescription.asStateFlow()

    fun updatePrescription(prescription: WorkoutPrescription) {
        _currentPrescription.value = prescription

        // Converte os exercícios selecionados nas 6 sequências em exercícios ativos do treino do aluno
        val activeExercises = prescription.sequences.flatMap { sequence ->
            sequence.exercises.filter { it.isSelected }.map { presEx ->
                val repNumber = presEx.repsOrTime.filter { it.isDigit() }.toIntOrNull() ?: 12
                Exercise(
                    id = presEx.id,
                    name = "${sequence.emoji} ${presEx.name.replaceFirstChar { it.uppercase() }}",
                    sets = presEx.sets,
                    reps = repNumber,
                    restSeconds = presEx.restSeconds,
                    instruction = if (presEx.notes.isNotBlank()) presEx.notes else "${sequence.title}: ${presEx.loadOrIntensity} - ${presEx.repsOrTime}"
                )
            }
        }

        if (activeExercises.isNotEmpty()) {
            _currentWorkout.value = Workout(
                code = prescription.workoutCode,
                title = "Prescrição Personalizada 60+ (${prescription.studentName})",
                exercises = activeExercises
            )
        }
    }

    // ==========================================
    // BIBLIOTECA DE VÍDEOS (TELA 7)
    // ==========================================
    private val _videoExercises = MutableStateFlow(VideoLibraryData.defaultVideos)
    val videoExercises: StateFlow<List<VideoExercise>> = _videoExercises.asStateFlow()

    fun toggleFavoriteVideoExercise(id: String) {
        _videoExercises.update { list ->
            list.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    fun addVideoExerciseToActiveWorkout(videoExercise: VideoExercise) {
        val repCount = videoExercise.reps.filter { it.isDigit() }.toIntOrNull() ?: 10
        val newEx = Exercise(
            id = "ex_${videoExercise.id}_${System.currentTimeMillis()}",
            name = "${videoExercise.emoji} ${videoExercise.name}",
            sets = videoExercise.sets,
            reps = repCount,
            restSeconds = videoExercise.restSeconds,
            instruction = videoExercise.instruction
        )
        val currentExercises = _currentWorkout.value.exercises
        _currentWorkout.update {
            it.copy(exercises = currentExercises + newEx)
        }
    }

    // ==========================================
    // TREINOS POR CONDIÇÃO / TREINO DIRECIONADO (TELA 8)
    // ==========================================
    private val _conditionProtocols = MutableStateFlow(ConditionWorkoutsData.protocols)
    val conditionProtocols: StateFlow<List<ConditionWorkoutProtocol>> = _conditionProtocols.asStateFlow()

    fun prescribeConditionProtocol(protocol: ConditionWorkoutProtocol) {
        val convertedExercises = protocol.exercises.mapIndexed { index, directedEx ->
            val repCount = directedEx.repsOrTime.filter { it.isDigit() }.toIntOrNull() ?: 10
            Exercise(
                id = "dir_${protocol.id}_${index}_${System.currentTimeMillis()}",
                name = "${directedEx.emoji} ${directedEx.name}",
                sets = directedEx.sets,
                reps = repCount,
                restSeconds = directedEx.restSeconds,
                instruction = "${directedEx.instruction} (Diretriz: ${directedEx.clinicalGuideline})"
            )
        }

        _currentWorkout.update {
            it.copy(
                code = "Treino Direcionado: ${protocol.conditionName}",
                title = "Protocolo Clínico (${protocol.title})",
                exercises = convertedExercises
            )
        }
    }

    // ==========================================
    // SISTEMA DE SEGURANÇA: ATENÇÃO ANTES DO TREINO (TELA 9)
    // ==========================================
    private val _safetySymptoms = MutableStateFlow(SafetySystemData.symptomsList)
    val safetySymptoms: StateFlow<List<PreWorkoutSymptom>> = _safetySymptoms.asStateFlow()

    private val _safetyRecords = MutableStateFlow(SafetySystemData.defaultRecords)
    val safetyRecords: StateFlow<List<SafetyCheckRecord>> = _safetyRecords.asStateFlow()

    private val _lastSafetyCheck = MutableStateFlow<SafetyCheckRecord?>(SafetySystemData.defaultRecords.firstOrNull())
    val lastSafetyCheck: StateFlow<SafetyCheckRecord?> = _lastSafetyCheck.asStateFlow()

    fun recordSafetyCheck(record: SafetyCheckRecord) {
        _safetyRecords.update { listOf(record) + it }
        _lastSafetyCheck.value = record
    }

    // ==========================================
    // EVOLUÇÃO LONGITUDINAL DO ALUNO (TELA 10)
    // ==========================================
    private val defaultEvolutionMetrics = listOf(
        EvolutionMetricItem(
            id = "evo_peso",
            category = EvolutionMetricCategory.WEIGHT,
            title = "Peso Corporal e IMC",
            unit = "kg",
            firstAssessmentValue = "65.0 kg (IMC 26.0)",
            currentAssessmentValue = "62.0 kg (IMC 24.8)",
            deltaValue = "-3.0 kg",
            deltaPercentage = "-4.6%",
            isPositiveImprovement = true,
            clinicalInterpretation = "Faixa eutrófica Lipschitz para 60+ (22 a 27 kg/m²). Redução gradual e segura com preservação de massa muscular magra.",
            practicalBenefitForElderly = "Alívio expressivo do impacto e sobrecarga nas articulações dos joelhos, quadris e coluna lombar.",
            historyPoints = listOf("Jan" to 65.0f, "Mar" to 64.2f, "Mai" to 63.0f, "Jul" to 62.4f, "Set" to 62.0f)
        ),
        EvolutionMetricItem(
            id = "evo_forca",
            category = EvolutionMetricCategory.STRENGTH,
            title = "Força Muscular (Sentar e Levantar 30s)",
            unit = "repetições",
            firstAssessmentValue = "10 repetições",
            currentAssessmentValue = "14 repetições",
            deltaValue = "+4 reps",
            deltaPercentage = "+40.0%",
            isPositiveImprovement = true,
            clinicalInterpretation = "Avançou de limítrofe para nível Bom/Forte pelos critérios de Rikli & Jones (Senior Fitness Test).",
            practicalBenefitForElderly = "Facilidade para levantar de cadeiras baixas, vaso sanitário e subir escadas com autonomia sem apoio dos braços.",
            historyPoints = listOf("Jan" to 10f, "Mar" to 11f, "Mai" to 12f, "Jul" to 13f, "Set" to 14f)
        ),
        EvolutionMetricItem(
            id = "evo_preensao",
            category = EvolutionMetricCategory.HANDGRIP,
            title = "Preensão Palmar (Dinamometria Manual)",
            unit = "kgf",
            firstAssessmentValue = "17.5 kgf",
            currentAssessmentValue = "22.5 kgf",
            deltaValue = "+5.0 kgf",
            deltaPercentage = "+28.6%",
            isPositiveImprovement = true,
            clinicalInterpretation = "Superou com folga a linha de corte internacional de sarcopenia feminina (EWGSOP2 < 16 kgf). Alta reserva motora.",
            practicalBenefitForElderly = "Firmeza para abrir potes e garrafas, segurar corrimãos com agilidade e carregar compras sem fadiga nas mãos.",
            historyPoints = listOf("Jan" to 17.5f, "Mar" to 18.5f, "Mai" to 20.0f, "Jul" to 21.2f, "Set" to 22.5f)
        ),
        EvolutionMetricItem(
            id = "evo_equilibrio",
            category = EvolutionMetricCategory.BALANCE,
            title = "Equilíbrio Estático (Apoio Unipodal)",
            unit = "segundos",
            firstAssessmentValue = "6.0 segundos",
            currentAssessmentValue = "14.0 segundos",
            deltaValue = "+8.0 s",
            deltaPercentage = "+133.3%",
            isPositiveImprovement = true,
            clinicalInterpretation = "Mais que dobrou o tempo sustentado em uma perna só. Indicador protetor com risco de quedas expressivamente reduzido.",
            practicalBenefitForElderly = "Segurança ao calçar calçados em pé, passar por desníveis de calçadas e evitar tropeços inesperados em casa.",
            historyPoints = listOf("Jan" to 6.0f, "Mar" to 8.0f, "Mai" to 10.0f, "Jul" to 12.0f, "Set" to 14.0f)
        ),
        EvolutionMetricItem(
            id = "evo_marcha",
            category = EvolutionMetricCategory.GAIT,
            title = "Velocidade da Marcha (Teste de 3 metros)",
            unit = "m/s",
            firstAssessmentValue = "0.81 m/s (3.7s)",
            currentAssessmentValue = "1.07 m/s (2.8s)",
            deltaValue = "+0.26 m/s",
            deltaPercentage = "+32.1%",
            isPositiveImprovement = true,
            clinicalInterpretation = "Superou a marca gerontológica protetora de 1.0 m/s. Excelente cadência e coordenação neuromotora na locomoção.",
            practicalBenefitForElderly = "Passada mais rápida e confiante para atravessar faixas de pedestre com folga no semáforo.",
            historyPoints = listOf("Jan" to 0.81f, "Mar" to 0.88f, "Mai" to 0.94f, "Jul" to 1.01f, "Set" to 1.07f)
        ),
        EvolutionMetricItem(
            id = "evo_flexibilidade",
            category = EvolutionMetricCategory.FLEXIBILITY,
            title = "Flexibilidade Posterior (Sentar e Alcançar)",
            unit = "cm",
            firstAssessmentValue = "16.0 cm",
            currentAssessmentValue = "24.0 cm",
            deltaValue = "+8.0 cm",
            deltaPercentage = "+50.0%",
            isPositiveImprovement = true,
            clinicalInterpretation = "Ganho de 8 cm de alcance. Arco articular do joelho atingiu 125° de flexão e recuperação total de extensão neutra (0°).",
            practicalBenefitForElderly = "Alívio de tensões nas costas e agilidade para pegar sapatos, amarrar cadarços e limpar prateleiras baixas.",
            historyPoints = listOf("Jan" to 16.0f, "Mar" to 18.0f, "Mai" to 20.0f, "Jul" to 22.0f, "Set" to 24.0f)
        ),
        EvolutionMetricItem(
            id = "evo_medidas",
            category = EvolutionMetricCategory.BODY_MEASUREMENTS,
            title = "Medidas Corporais (Panturrilha e Cintura)",
            unit = "cm",
            firstAssessmentValue = "Panturrilha 33.0 / Cintura 83.0 cm",
            currentAssessmentValue = "Panturrilha 34.5 / Cintura 78.0 cm",
            deltaValue = "+1.5 cm pant. / -5 cm cint.",
            deltaPercentage = "+4.5% massa magra",
            isPositiveImprovement = true,
            clinicalInterpretation = "Ganho de trofismo na panturrilha (marcador padrão ouro de massa muscular no idoso) somado a perda de 5 cm de gordura abdominal.",
            practicalBenefitForElderly = "Melhora no retorno venoso das pernas, redução do inchaço e menor risco cardiovascular metabólico.",
            historyPoints = listOf("Jan" to 33.0f, "Mar" to 33.4f, "Mai" to 33.8f, "Jul" to 34.2f, "Set" to 34.5f)
        ),
        EvolutionMetricItem(
            id = "evo_frequencia",
            category = EvolutionMetricCategory.FREQUENCY,
            title = "Frequência de Treino e Adesão Mensal",
            unit = "treinos/mês",
            firstAssessmentValue = "11 treinos (68%)",
            currentAssessmentValue = "18 treinos (92%)",
            deltaValue = "+7 treinos",
            deltaPercentage = "+35.3%",
            isPositiveImprovement = true,
            clinicalInterpretation = "Excelente regularidade de 3 a 4 sessões semanais. Sequência ativa de 7 semanas ininterruptas no programa.",
            practicalBenefitForElderly = "Consolidação de hábitos de vida ativos, energia renovada pela manhã e melhora expressiva do sono.",
            historyPoints = listOf("Jan" to 11f, "Mar" to 14f, "Mai" to 16f, "Jul" to 17f, "Set" to 18f)
        ),
        EvolutionMetricItem(
            id = "evo_exercicios",
            category = EvolutionMetricCategory.COMPLETED_EXERCISES,
            title = "Exercícios Realizados Acumulados",
            unit = "exercícios",
            firstAssessmentValue = "30 realizados",
            currentAssessmentValue = "142 acumulados",
            deltaValue = "+112 exercícios",
            deltaPercentage = "+373%",
            isPositiveImprovement = true,
            clinicalInterpretation = "426 séries concluídas com sucesso. Distribuição balanceada entre força (34%), equilíbrio (25%), mobilidade (21%) e função (20%).",
            practicalBenefitForElderly = "Adaptação neuromuscular consolidada com manutenção da independência física e vitalidade.",
            historyPoints = listOf("Jan" to 30f, "Mar" to 62f, "Mai" to 95f, "Jul" to 120f, "Set" to 142f)
        )
    )

    private val _evolutionMetrics = MutableStateFlow(defaultEvolutionMetrics)
    val evolutionMetrics: StateFlow<List<EvolutionMetricItem>> = _evolutionMetrics.asStateFlow()

    private val defaultTrophies = listOf(
        EvolutionTrophy("tr_1", "Super Força 60+", "+40% de força nas pernas conquistados", "💪", "Set/2026"),
        EvolutionTrophy("tr_2", "Escudo Anti-Quedas", "Mais que dobrou o equilíbrio unipodal (14s)", "🛡️", "Ago/2026"),
        EvolutionTrophy("tr_3", "Pegada Forte", "Preensão palmar protetora contra sarcopenia (22.5 kgf)", "✊", "Set/2026"),
        EvolutionTrophy("tr_4", "Constância de Ouro", "7 semanas consecutivas com 92% de frequência", "⭐", "Set/2026")
    )

    private val _evolutionTrophies = MutableStateFlow(defaultTrophies)
    val evolutionTrophies: StateFlow<List<EvolutionTrophy>> = _evolutionTrophies.asStateFlow()

    companion object {
        val instance = AppRepository()
    }
}
