package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppRepository
import com.example.data.StudentProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("60+ FIT", appName)
    }

    @Test
    fun `app repository initialized with 60+ exercises and student profile`() {
        val repository = AppRepository()
        val workout = repository.currentWorkout.value
        assertEquals("Treino A", workout.code)
        assertEquals(5, workout.exercises.size)

        // Verify exercise completion
        val firstExId = workout.exercises.first().id
        repository.completeExercise(firstExId)
        assertTrue(repository.currentWorkout.value.exercises.first { it.id == firstExId }.completed)

        // Verify reminders
        val initialReminder = repository.reminders.value.first()
        val initialStatus = initialReminder.enabled
        repository.toggleReminder(initialReminder.id)
        assertEquals(!initialStatus, repository.reminders.value.first().enabled)

        // Verify student profile update
        val updatedProfile = StudentProfile(
            name = "Maria Silva Santos",
            birthDate = "15/03/1956",
            gender = "Feminino",
            phone = "(11) 98765-4321",
            email = "maria.silva@email.com",
            emergencyContact = "Carlos Silva (Filho) - (11) 99876-5432",
            mainGoal = "Ganhar força e autonomia",
            responsibleProfessional = "Dra. Mariana Lima (CREF 045123)",
            startDate = "10/01/2026",
            contractedPlan = "Plano Premium 60+",
            weeklyFrequency = "3x por semana",
            professionalNotes = "Foco em membros inferiores e equilíbrio.",
            weightKg = "61.5"
        )
        repository.updateStudentProfile(updatedProfile)
        assertEquals("Maria Silva Santos", repository.studentProfile.value.name)
        assertEquals("Feminino", repository.studentProfile.value.gender)
        assertEquals("Carlos Silva (Filho) - (11) 99876-5432", repository.studentProfile.value.emergencyContact)
        assertEquals("Dra. Mariana Lima (CREF 045123)", repository.studentProfile.value.responsibleProfessional)
        assertEquals("Plano Premium 60+", repository.studentProfile.value.contractedPlan)
        assertEquals("3x por semana", repository.studentProfile.value.weeklyFrequency)
    }

    @Test
    fun `plans repository has the 3 60+fit products with pricing and features`() {
        val repository = AppRepository()
        val plans = repository.plans.value
        assertEquals(3, plans.size)

        val essencial = plans.find { it.id == "plan_essencial" }
        assertNotNull(essencial)
        assertEquals("60+fit Essencial", essencial?.name)
        assertEquals(79.90, essencial?.monthlyPrice ?: 0.0, 0.01)
        assertTrue(essencial?.features?.contains("Biblioteca de exercícios com vídeos e áudio") == true)

        val gerontologico = plans.find { it.id == "plan_gerontologico" }
        assertNotNull(gerontologico)
        assertEquals("60+fit Gerontológico", gerontologico?.name)
        assertEquals(129.90, gerontologico?.monthlyPrice ?: 0.0, 0.01)
        assertTrue(gerontologico?.isRecommended == true)
        assertTrue(gerontologico?.features?.any { it.contains("anamnese", ignoreCase = true) } == true)
        assertTrue(gerontologico?.features?.any { it.contains("protocolos específicos", ignoreCase = true) } == true)

        val premium = plans.find { it.id == "plan_premium" }
        assertNotNull(premium)
        assertEquals("60+fit Premium", premium?.name)
        assertTrue(premium?.features?.any { it.contains("vídeos personalizados", ignoreCase = true) } == true)
        assertTrue(premium?.features?.any { it.contains("contato profissional", ignoreCase = true) } == true)
    }

    @Test
    fun `subscribe to plan with billing periods and payment methods updates user subscription`() {
        val repository = AppRepository()

        // Subscribe to Gerontologico with PIX_AUTOMATICO and TRIMESTRAL
        repository.subscribeToPlan(
            planId = "plan_gerontologico",
            period = com.example.data.BillingPeriod.TRIMESTRAL,
            method = com.example.data.PaymentMethod.PIX_AUTOMATICO
        )

        val sub = repository.userSubscription.value
        assertEquals("plan_gerontologico", sub.planId)
        assertEquals("60+fit Gerontológico", sub.planName)
        assertEquals(com.example.data.BillingPeriod.TRIMESTRAL, sub.billingPeriod)
        assertEquals(com.example.data.PaymentMethod.PIX_AUTOMATICO, sub.paymentMethod)
        assertEquals("Ativa", sub.status)
        assertTrue(sub.autoRenew)

        // Cancel subscription
        repository.cancelSubscription()
        assertTrue(repository.userSubscription.value.status.startsWith("Cancelada"))
        assertEquals(false, repository.userSubscription.value.autoRenew)
    }

    @Test
    fun `professional can create and delete custom plans`() {
        val repository = AppRepository()
        val initialCount = repository.plans.value.size

        val customPlan = com.example.data.PlanItem(
            id = "plan_fisio_vip",
            name = "60+fit Fisioterapia Individual",
            monthlyPrice = 249.90,
            price = "R$ 249,90/mês",
            description = "Atendimento presencial e online com fisioterapeuta",
            features = listOf("2 sessões semanais", "Atendimento emergencial"),
            isCreatedByProfessional = true,
            authorName = "Prof. Dra. Camila Rocha"
        )

        repository.addProfessionalPlan(customPlan)
        assertEquals(initialCount + 1, repository.plans.value.size)
        assertTrue(repository.plans.value.any { it.id == "plan_fisio_vip" && it.isCreatedByProfessional })

        // Delete plan
        repository.deleteProfessionalPlan("plan_fisio_vip")
        assertEquals(initialCount, repository.plans.value.size)
    }

    @Test
    fun `hostinger database configuration initializes with defaults and updates`() {
        val repository = AppRepository()
        val config = repository.hostingerConfig.value
        assertNotNull(config)
        assertTrue(config.serverUrl.contains("api.php"))
        assertEquals(false, config.isConnected)

        // Update config
        repository.updateHostingerConfig("https://meudominio.com.br/api/api.php", "custom_secret_123")
        val updated = repository.hostingerConfig.value
        assertEquals("https://meudominio.com.br/api/api.php", updated.serverUrl)
        assertEquals("custom_secret_123", updated.apiToken)
    }
}
