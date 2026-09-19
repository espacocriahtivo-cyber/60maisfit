package com.example.data

enum class UserType {
    STUDENT,
    PROFESSIONAL
}

data class StudentProfile(
    val name: String = "Maria Silva",
    val birthDate: String = "15/03/1956",
    val gender: String = "Feminino",
    val phone: String = "(11) 98765-4321",
    val email: String = "maria.silva@email.com",
    val photoResId: Int? = null,
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
    val hypertension: Boolean = true,
    val diabetes: Boolean = false,
    val heartConditions: Boolean = false,
    val arthrosisArthritis: Boolean = true,
    val osteoporosis: Boolean = false,
    val obesity: Boolean = false,
    val others: Boolean = false,
    val notes: String = "Sinto um leve desconforto no joelho direito em dias frios."
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
    val id: String = "workout_a",
    val code: String = "Treino A",
    val title: String = "Força e funcionalidade",
    val exercises: List<Exercise>
)

data class WeightHistoryPoint(
    val month: String,
    val weight: Float
)

enum class BillingPeriod(
    val title: String,
    val months: Int,
    val discountPercent: Int,
    val badge: String
) {
    MENSAL("Mensal", 1, 0, "Padrão"),
    TRIMESTRAL("Trimestral", 3, 5, "5% OFF"),
    SEMESTRAL("Semestral", 6, 10, "10% OFF"),
    ANUAL("Anual", 12, 20, "20% OFF")
}

enum class PaymentMethod(
    val title: String,
    val iconEmoji: String,
    val subtitle: String
) {
    CREDIT_CARD("Cartão de crédito", "💳", "Faturamento recorrente em até 12x"),
    DEBIT_CARD("Cartão de débito", "💳", "Débito à vista na conta corrente"),
    PIX("Pix", "🟢", "Aprovação instantânea via QR Code"),
    PIX_AUTOMATICO("Pix automático / recorrente", "🔄", "Novo padrão BACEN para débito mensal automático")
}

data class PlanItem(
    val id: String,
    val name: String,
    val monthlyPrice: Double = 79.90,
    val price: String = "R$ 79,90/mês",
    val description: String = "",
    val features: List<String> = emptyList(),
    val isRecommended: Boolean = false,
    val isCreatedByProfessional: Boolean = false,
    val authorName: String? = null
) {
    fun calculatePrice(period: BillingPeriod): Pair<Double, Double> {
        val totalMonths = period.months
        val baseTotal = monthlyPrice * totalMonths
        val discountedTotal = baseTotal * (1.0 - period.discountPercent / 100.0)
        val equivalentMonthly = discountedTotal / totalMonths
        return Pair(discountedTotal, equivalentMonthly)
    }
}

data class UserSubscription(
    val planId: String = "plan_gerontologico",
    val planName: String = "60+fit Gerontológico",
    val monthlyPrice: Double = 129.90,
    val billingPeriod: BillingPeriod = BillingPeriod.MENSAL,
    val paymentMethod: PaymentMethod = PaymentMethod.PIX_AUTOMATICO,
    val status: String = "Ativa",
    val currentBillingAmount: String = "R$ 129,90/mês",
    val nextRenewalDate: String = "16/10/2026",
    val autoRenew: Boolean = true,
    val lastPaymentDate: String = "16/09/2026"
)

data class ReminderItem(
    val id: String,
    val title: String,
    val timeOrDate: String,
    val iconType: String, // "workout", "water", "pressure", "assessment", "doctor"
    val enabled: Boolean
)

data class ProfessionalStudent(
    val id: String,
    val name: String,
    val age: Int,
    val program: String,
    val lastActive: String,
    val completionRate: Int
)
