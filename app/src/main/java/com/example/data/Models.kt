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
    val weightKg: String = "62",
    val heightM: String = "1,58",
    val bloodPressure: String = "120 / 80 mmHg",
    val heartRateBpm: Int = 72,
    val oxygenSaturation: Int = 98,
    val sitToStandReps: Int = 14,
    val walkDistanceMeters: Int = 420,
    val flexibilityRating: String = "Adequada para a faixa etária"
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
