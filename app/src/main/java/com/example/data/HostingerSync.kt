package com.example.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class HostingerConfigData(
    val serverUrl: String = "https://seusite.com.br/api/api.php",
    val apiToken: String = "rDEe8IwynGbuFLrNqRxcUZdVU5xcHdmSqp8VGxJQcec65c7d",
    val isConnected: Boolean = false,
    val lastSyncTime: String = "Não sincronizado",
    val lastSyncMessage: String = "Configure sua URL da Hostinger abaixo para conectar o banco de dados MySQL.",
    val isSyncing: Boolean = false,
    val serverInfo: String = ""
)

class HostingerApiClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun testConnection(serverUrl: String, token: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val cleanUrl = cleanServerUrl(serverUrl)
            val testUrl = if (cleanUrl.contains("?")) "$cleanUrl&action=test" else "$cleanUrl?action=test"

            val requestBuilder = Request.Builder()
                .url(testUrl)
                .get()

            if (token.isNotBlank()) {
                requestBuilder.header("X-API-KEY", token)
            }

            val response = client.newCall(requestBuilder.build()).execute()
            val body = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Servidor respondeu com código HTTP ${response.code}: $body")
                )
            }

            val json = JSONObject(body)
            val isSuccess = json.optBoolean("success", false)
            if (isSuccess) {
                val msg = json.optString("message", "Conexão bem-sucedida!")
                val mysqlVer = json.optString("mysql_version", "MySQL")
                val studentsCount = json.optInt("students_count", 0)
                val plansCount = json.optInt("plans_count", 0)
                Result.success("$msg (Versão $mysqlVer • $studentsCount alunos • $plansCount planos cadastrados)")
            } else {
                val err = json.optString("error", "Erro retornado pela API Hostinger")
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Falha ao comunicar com a Hostinger: ${e.localizedMessage}"))
        }
    }

    suspend fun createTables(serverUrl: String, token: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val cleanUrl = cleanServerUrl(serverUrl)
            val createUrl = if (cleanUrl.contains("?")) "$cleanUrl&action=create_tables" else "$cleanUrl?action=create_tables"

            val requestBuilder = Request.Builder()
                .url(createUrl)
                .get()

            if (token.isNotBlank()) {
                requestBuilder.header("X-API-KEY", token)
            }

            val response = client.newCall(requestBuilder.build()).execute()
            val body = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Erro do servidor (HTTP ${response.code}): $body")
                )
            }

            val json = JSONObject(body)
            val isSuccess = json.optBoolean("success", false)
            if (isSuccess) {
                val msg = json.optString("message", "Tabelas criadas com sucesso!")
                val tablesArr = json.optJSONArray("tables_created")
                val count = tablesArr?.length() ?: 10
                Result.success("$msg ($count tabelas prontas no MySQL)")
            } else {
                val err = json.optString("error", "Erro ao criar tabelas no MySQL")
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Falha ao criar tabelas na Hostinger: ${e.localizedMessage}"))
        }
    }

    suspend fun syncAll(
        serverUrl: String,
        token: String,
        student: StudentProfile,
        subscription: UserSubscription,
        plans: List<PlanItem>
    ): Result<Pair<String, List<PlanItem>>> = withContext(Dispatchers.IO) {
        try {
            val cleanUrl = cleanServerUrl(serverUrl)
            val syncUrl = if (cleanUrl.contains("?")) "$cleanUrl&action=sync_all" else "$cleanUrl?action=sync_all"

            val payload = JSONObject().apply {
                // Aluno
                val studentObj = JSONObject().apply {
                    put("id", "student_default")
                    put("name", student.name)
                    put("birthDate", student.birthDate)
                    put("gender", student.gender)
                    put("phone", student.phone)
                    put("email", student.email)
                    put("emergencyContact", student.emergencyContact)
                    put("mainGoal", student.mainGoal)
                    put("responsibleProfessional", student.responsibleProfessional)
                    put("startDate", student.startDate)
                    put("contractedPlan", student.contractedPlan)
                    put("weeklyFrequency", student.weeklyFrequency)
                    put("professionalNotes", student.professionalNotes)
                    put("cpf", student.cpf)
                    put("weightKg", student.weightKg)
                    put("heightM", student.heightM)
                }
                put("student", studentObj)

                // Assinatura
                val subObj = JSONObject().apply {
                    put("planId", subscription.planId)
                    put("planName", subscription.planName)
                    put("monthlyPrice", subscription.monthlyPrice)
                    put("billingPeriod", subscription.billingPeriod.name)
                    put("paymentMethod", subscription.paymentMethod.name)
                    put("status", subscription.status)
                    put("currentBillingAmount", subscription.currentBillingAmount)
                    put("nextRenewalDate", subscription.nextRenewalDate)
                    put("autoRenew", subscription.autoRenew)
                    put("lastPaymentDate", subscription.lastPaymentDate)
                }
                put("subscription", subObj)

                // Planos
                val plansArray = JSONArray()
                plans.forEach { p ->
                    val pObj = JSONObject().apply {
                        put("id", p.id)
                        put("name", p.name)
                        put("monthlyPrice", p.monthlyPrice)
                        put("price", p.price)
                        put("description", p.description)
                        put("isRecommended", p.isRecommended)
                        put("isCreatedByProfessional", p.isCreatedByProfessional)
                        put("authorName", p.authorName ?: "")
                    }
                    plansArray.put(pObj)
                }
                put("plans", plansArray)
            }

            val requestBody = payload.toString().toRequestBody(jsonMediaType)
            val requestBuilder = Request.Builder()
                .url(syncUrl)
                .post(requestBody)

            if (token.isNotBlank()) {
                requestBuilder.header("X-API-KEY", token)
            }

            val response = client.newCall(requestBuilder.build()).execute()
            val body = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Falha na sincronização (HTTP ${response.code}): $body")
                )
            }

            val json = JSONObject(body)
            val isSuccess = json.optBoolean("success", false)
            if (isSuccess) {
                val msg = json.optString("message", "Sincronizado com sucesso!")
                val syncedAt = json.optString("synced_at", "")

                // Extrair planos retornados
                val returnedPlans = mutableListOf<PlanItem>()
                val plansArr = json.optJSONArray("plans")
                if (plansArr != null) {
                    for (i in 0 until plansArr.length()) {
                        val item = plansArr.getJSONObject(i)
                        val featArr = item.optJSONArray("features")
                        val feats = mutableListOf<String>()
                        if (featArr != null) {
                            for (j in 0 until featArr.length()) {
                                feats.add(featArr.getString(j))
                            }
                        }
                        returnedPlans.add(
                            PlanItem(
                                id = item.optString("id"),
                                name = item.optString("name"),
                                monthlyPrice = item.optDouble("monthlyPrice", 79.90),
                                price = item.optString("price", "R$ 79,90/mês"),
                                description = item.optString("description", ""),
                                features = feats,
                                isRecommended = item.optBoolean("isRecommended", false),
                                isCreatedByProfessional = item.optBoolean("isCreatedByProfessional", false),
                                authorName = item.optString("authorName", null)
                            )
                        )
                    }
                }

                Result.success(Pair("$msg às $syncedAt", returnedPlans))
            } else {
                val err = json.optString("error", "Erro retornado na sincronização")
                Result.failure(Exception(err))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao sincronizar com banco Hostinger: ${e.localizedMessage}"))
        }
    }

    private fun cleanServerUrl(url: String): String {
        var clean = url.trim()
        if (!clean.startsWith("http://") && !clean.startsWith("https://")) {
            clean = "https://$clean"
        }
        return clean
    }
}
