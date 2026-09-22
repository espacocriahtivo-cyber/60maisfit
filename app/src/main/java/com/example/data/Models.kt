package com.example.data

data class StudentProfile(
    val id: String = "student_001",
    val name: String = "Maria Silva",
    val birthDate: String = "15/03/1956",
    val gender: String = "Feminino",
    val phone: String = "(11) 98765-4321",
    val email: String = "maria.silva@email.com",
    val photoUrl: String = "",
    val emergencyContact: String = "Carlos Silva (Filho) - (11) 99876-5432",
    val mainGoal: String = "Ganhar força e autonomia",
    // Informações profissionais
    val responsibleProfessional: String = "Dra. Mariana Lima (CREF 045123)",
    val startDate: String = "10/01/2026",
    val contractedPlan: String = "60+fit Gerontológico (R$ 129,90/mês)",
    val weeklyFrequency: String = "3x por semana",
    val professionalNotes: String = "Foco em membros inferiores, equilíbrio e prevenção de quedas. Sem contraindicações agudas.",
    val cpf: String = "123.456.789-00",
    val weightKg: String = "62",
    val heightM: String = "1,58"
)

data class HealthConditions(
    // 1. Bloco Saúde
    val medicalDiagnoses: String = "Hipertensão controlada e osteoartrite em joelhos.",
    val surgeries: String = "Colecistectomia por videolaparoscopia (2015).",
    val hospitalizations: String = "Sem internações nos últimos 12 meses.",
    val medications: String = "Losartana 50mg (1x/dia), Vitamina D 2000UI.",
    val allergies: String = "Dipirona e poeira.",
    val fallHistory: String = "1 queda há 8 meses ao tropeçar no tapete (sem fratura).",

    // 2. Bloco Dor
    val painLocation: String = "Joelho direito e região lombar.",
    val painIntensity: Int = 3, // Escala EVA 0 a 10

    // 3. Bloco Estilo de Vida
    val sleepQuality: String = "Regular (6 a 7 horas/noite, acorda 1x)",
    val physicalActivityLevel: String = "Levemente ativo (caminhadas 2x na semana)",

    // 4. Bloco Doenças / Comorbidades Gerontológicas (17 itens)
    val hypertension: Boolean = true,
    val type2Diabetes: Boolean = false,
    val obesity: Boolean = false,
    val osteopenia: Boolean = false,
    val osteoporosis: Boolean = false,
    val arthrosis: Boolean = true,
    val arthritis: Boolean = false,
    val lowerBackPain: Boolean = true,
    val cardiovascularDiseases: Boolean = false,
    val respiratoryDiseases: Boolean = false,
    val parkinson: Boolean = false,
    val strokeSequelae: Boolean = false,
    val sarcopenia: Boolean = false,
    val frailty: Boolean = false,
    val balanceChanges: Boolean = true,
    val fallRisk: Boolean = true,
    val mobilityLimitations: Boolean = false,

    // Apoio à Decisão Profissional
    val notes: String = "Atenção a impacto articular e mudança brusca de decúbito. Aluna motivada.",
    val professionalValidationNotes: String = "Avaliado clinicamente. Treino liberado com adaptação de carga e foco proprioceptivo.",
    val isProfessionalValidated: Boolean = true,

    // Compatibilidade com código legado
    val diabetes: Boolean = false,
    val heartConditions: Boolean = false,
    val arthrosisArthritis: Boolean = true,
    val others: Boolean = false
)

data class PhysicalAssessment(
    val id: String = "eval_003",
    val date: String = "22/09/2026",
    val responsibleProfessional: String = "Dra. Mariana Lima (CREF 045123)",

    // Obrigatórios
    val weightKg: String = "62",
    val heightCm: String = "158",
    val heightM: String = "1,58",

    // Opcionais: Perimetria (cm)
    val armCircumferenceCm: String = "28.5",
    val waistCircumferenceCm: String = "78.0",
    val abdomenCircumferenceCm: String = "84.0",
    val hipCircumferenceCm: String = "98.0",
    val thighCircumferenceCm: String = "52.0",
    val calfCircumferenceCm: String = "34.5", // Alerta de sarcopenia < 31 cm

    // Opcionais: Dobras cutâneas (mm)
    val tricepsSkinfoldMm: String = "18.0",
    val subscapularSkinfoldMm: String = "16.5",
    val iliacCrestSkinfoldMm: String = "20.0",

    // Opcionais: Testes Biomecânicos & Funcionais
    val flexibilityCm: String = "24",
    val flexibilityRating: String = "Adequada para a faixa etária",
    val handgripStrengthKgf: String = "22.5", // Pressão palmar / força de preensão
    val gaitSpeed3mSeconds: String = "2.8", // Marcha 3 metros

    // Pressão arterial
    val systolicPressureMmHg: String = "120",
    val diastolicPressureMmHg: String = "80",
    val bloodPressure: String = "120 / 80 mmHg",

    // Saturação e Frequência Cardíaca
    val oxygenSaturation: Int = 98,
    val heartRateBpm: Int = 72,

    // Goniometria
    val goniometryNotes: String = "Joelho direito: flexão 125° / extensão 0°. Ombro direito: flexão 165°.",

    // Teste de equilíbrio
    val balanceTestResult: String = "Apoio unipodal: 14s (D) / 12s (E). Semi-tandem seguro e estável.",

    // Teste AGA — Avaliação Geriátrica Ampla (opcional)
    val agaScore: String = "Independente para ABVD e AIVD. Cognição e humor preservados. Sem sinais de fragilidade.",

    // Observações profissionais
    val professionalNotes: String = "Evolução clínica favorável. Ganho perceptível de força em membros inferiores e marcha mais segura.",

    // Compatibilidade com código legado
    val sitToStandReps: Int = 14,
    val walkDistanceMeters: Int = 420
) {
    fun calculateBmi(): Double {
        val w = weightKg.replace(",", ".").toDoubleOrNull() ?: return 0.0
        val hCm = heightCm.replace(",", ".").toDoubleOrNull()
        val h = if (hCm != null && hCm > 40.0) {
            hCm / 100.0
        } else {
            heightM.replace(",", ".").toDoubleOrNull() ?: 1.58
        }
        if (h <= 0.0) return 0.0
        return w / (h * h)
    }

    fun getBmiClassification(): String {
        val bmi = calculateBmi()
        if (bmi <= 0.0) return "Informe peso e altura"
        return when {
            bmi < 22.0 -> "Baixo peso (Risco de sarcopenia / desnutrição)"
            bmi <= 27.0 -> "Eutrófico / Peso adequado (Critério Lipschitz 60+)"
            bmi <= 30.0 -> "Sobrepeso (Atenção às articulações)"
            else -> "Obesidade (Sobrecarga osteoarticular)"
        }
    }

    fun getBmiStatusType(): String {
        val bmi = calculateBmi()
        return when {
            bmi <= 0.0 -> "NEUTRAL"
            bmi < 22.0 -> "WARNING"
            bmi <= 27.0 -> "SUCCESS"
            else -> "WARNING"
        }
    }

    fun calculateWaistToHipRatio(): String {
        val w = waistCircumferenceCm.replace(",", ".").toDoubleOrNull()
        val h = hipCircumferenceCm.replace(",", ".").toDoubleOrNull()
        if (w == null || h == null || h <= 0.0) return "--"
        val ratio = w / h
        return String.format(java.util.Locale.US, "%.2f", ratio)
    }

    fun calculateSkinfoldSum(): String {
        val t = tricepsSkinfoldMm.replace(",", ".").toDoubleOrNull() ?: 0.0
        val s = subscapularSkinfoldMm.replace(",", ".").toDoubleOrNull() ?: 0.0
        val c = iliacCrestSkinfoldMm.replace(",", ".").toDoubleOrNull() ?: 0.0
        val sum = t + s + c
        return if (sum > 0) String.format(java.util.Locale.US, "%.1f mm", sum) else "--"
    }

    fun calculateGaitVelocity(): String {
        val sec = gaitSpeed3mSeconds.replace(",", ".").toDoubleOrNull()
        if (sec == null || sec <= 0) return "--"
        val vel = 3.0 / sec
        return String.format(java.util.Locale.US, "%.2f m/s", vel)
    }
}

data class AssessmentComparison(
    val current: PhysicalAssessment,
    val previous: PhysicalAssessment?,
    val weightDeltaKg: Double?,
    val weightDeltaPercent: Double?,
    val bmiDelta: Double?,
    val handgripDeltaKgf: Double?,
    val gaitDeltaSec: Double?,
    val calfDeltaCm: Double?
)

data class Exercise(
    val id: String,
    val name: String,
    val sets: Int = 3,
    val reps: Int = 12,
    val restSeconds: Int = 30,
    val instruction: String,
    val completed: Boolean = false
)

data class Workout(
    val code: String = "Treino A",
    val title: String = "Força e funcionalidade",
    val exercises: List<Exercise> = emptyList()
)

data class ReminderItem(
    val id: String,
    val title: String,
    val timeOrDate: String,
    val iconType: String = "workout",
    val enabled: Boolean = true
)

enum class BillingPeriod(val title: String, val badge: String, val months: Int, val discountMultiplier: Double) {
    MENSAL("Mensal", "Sem compromisso", 1, 1.0),
    TRIMESTRAL("Trimestral", "10% de desconto", 3, 0.90),
    SEMESTRAL("Semestral", "15% de desconto", 6, 0.85),
    ANUAL("Anual", "25% de desconto", 12, 0.75)
}

enum class PaymentMethod(val title: String, val subtitle: String, val iconEmoji: String) {
    CREDIT_CARD("Cartão de Crédito", "Até 12x sem juros, renovação automática garantida", "💳"),
    DEBIT_CARD("Cartão de Débito", "Débito em conta à vista", "💳"),
    PIX("Pix", "Aprovação instantânea via QR Code", "⚡"),
    PIX_AUTOMATICO("Pix Automático", "Aprovação instantânea, débito automático sem bloquear limite", "⚡"),
    BOLETO_BANCARIO("Boleto Bancário", "Emissão mensal enviada por e-mail e WhatsApp", "📄")
}

data class PlanItem(
    val id: String,
    val name: String,
    val monthlyPrice: Double,
    val price: String,
    val description: String,
    val features: List<String>,
    val isRecommended: Boolean = false,
    val isCreatedByProfessional: Boolean = false,
    val authorName: String = "60+fit"
) {
    fun calculatePrice(period: BillingPeriod): Pair<Double, Double> {
        val monthlyEquivalent = monthlyPrice * period.discountMultiplier
        val totalAmount = monthlyEquivalent * period.months
        return Pair(totalAmount, monthlyEquivalent)
    }
}

data class UserSubscription(
    val planId: String,
    val planName: String,
    val monthlyPrice: Double,
    val billingPeriod: BillingPeriod,
    val paymentMethod: PaymentMethod,
    val status: String,
    val currentBillingAmount: String,
    val nextRenewalDate: String,
    val autoRenew: Boolean,
    val lastPaymentDate: String
)

data class FunctionalDimension(
    val id: String,
    val name: String, // FORÇA, MOBILIDADE, EQUILÍBRIO, MARCHA, FLEXIBILIDADE, RISCO DE QUEDAS, CAPACIDADE FUNCIONAL
    val icon: String,
    val primaryMetric: String, // Ex: Preensão palmar
    val initialValue: String,  // Ex: 21 kg
    val currentValue: String,  // Ex: 25 kg
    val evolutionLabel: String,// Ex: ↑ evolução (+4 kg / +19%)
    val isPositive: Boolean = true,
    val historyPoints: List<Pair<String, Float>> = emptyList(),
    val secondaryMetric: String? = null,
    val secondaryInitial: String? = null,
    val secondaryCurrent: String? = null,
    val secondaryEvolution: String? = null,
    val clinicalGuideline: String
)

data class StudentFunctionalProfile(
    val studentId: String,
    val studentName: String,
    val age: Int,
    val plan: String,
    val frequency: String,
    val lastAssessmentDate: String,
    val overallScore: String,
    val fallRiskLevel: String,
    val adherencePercent: Int,
    val dimensions: List<FunctionalDimension>
)

data class ProfessionalStudent(
    val id: String,
    val name: String,
    val age: Int,
    val program: String = "Força e Funcionalidade",
    val lastSession: String = "Hoje às 08:30",
    val completionRate: Int = 92,
    val plan: String = "60+fit Gerontológico",
    val status: String = "Ativo",
    val adherencePercent: Int = 92,
    val nextSession: String = "Amanhã às 08:30"
)

data class WeightHistoryPoint(
    val month: String,
    val weight: Float
)

data class AccessibilityState(
    val largeText: Boolean = false,
    val highContrast: Boolean = false,
    val screenReaderHints: Boolean = false
)

enum class UserType {
    STUDENT,
    PROFESSIONAL
}

data class PrescriptionExercise(
    val id: String,
    val name: String,
    val sequenceId: Int, // 1 a 6
    val isSelected: Boolean = true,
    val sets: Int = 2,
    val repsOrTime: String = "10 a 12 reps",
    val loadOrIntensity: String = "Peso corporal",
    val restSeconds: Int = 45,
    val notes: String = ""
)

data class WorkoutSequence(
    val id: Int,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val exercises: List<PrescriptionExercise>
)

data class WorkoutPrescription(
    val studentId: String = "student_001",
    val studentName: String = "Maria Silva",
    val workoutCode: String = "Treino A",
    val sequences: List<WorkoutSequence> = emptyList(),
    val professionalName: String = "Prof. Dra. Camila Rocha (CREF 098452-G/SP)",
    val date: String = "22/09/2026"
)
