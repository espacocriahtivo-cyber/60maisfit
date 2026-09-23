package com.example

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.AppRepository
import com.example.data.BillingPeriod
import com.example.data.PaymentMethod
import com.example.data.PlanItem
import com.example.data.UserSubscription
import com.example.data.UserType
import com.example.ui.components.AccessibilityDialog
import com.example.ui.screens.AnamneseScreen
import com.example.ui.screens.ConditionWorkoutsScreen
import com.example.ui.screens.EvolutionScreen
import com.example.ui.screens.ExerciseActiveScreen
import com.example.ui.screens.HealthScreen
import com.example.ui.screens.HostingerConfigScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MainMenuScreen
import com.example.ui.screens.PhysicalAssessmentScreen
import com.example.ui.screens.PlansScreen
import com.example.ui.screens.ProfessionalDashboardScreen
import com.example.ui.screens.ProfessionalScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.RoleSelectionScreen
import com.example.ui.screens.SafetyCheckScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StudentHomeScreen
import com.example.ui.screens.StudentProfileScreen
import com.example.ui.screens.VideoLibraryScreen
import com.example.ui.screens.WorkoutPrescriptionScreen
import com.example.ui.screens.WorkoutScreen
import com.example.ui.theme.FitLime
import com.example.ui.theme.FitLimeDark
import com.example.ui.theme.MyApplicationTheme
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            tts = TextToSpeech(this, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setContent {
            val repository = remember { AppRepository() }
            val accessibilityState by repository.accessibilityState.collectAsState()

            MyApplicationTheme(accessibilityState = accessibilityState) {
                MainAppNavigation(
                    repository = repository,
                    onSpeak = { text -> speakInstruction(text) }
                )
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("pt", "BR"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.getDefault())
            }
            isTtsReady = true
        }
    }

    private fun speakInstruction(text: String) {
        if (isTtsReady && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "instruction_speech")
        } else {
            Toast.makeText(this, "Áudio: $text", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}

data class ScreenCatalogItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppNavigation(
    repository: AppRepository,
    onSpeak: (String) -> Unit
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    val currentStudent by repository.studentProfile.collectAsState()
    val workout by repository.currentWorkout.collectAsState()
    val assessment by repository.physicalAssessment.collectAsState()
    val assessmentHistory by repository.assessmentHistory.collectAsState()
    val conditions by repository.healthConditions.collectAsState()
    val reminders by repository.reminders.collectAsState()
    val plans by repository.plans.collectAsState()
    val selectedPlanId by repository.selectedPlanId.collectAsState()
    val userSubscription by repository.userSubscription.collectAsState()
    val professionalStudents by repository.professionalStudents.collectAsState()
    val accessibilityState by repository.accessibilityState.collectAsState()
    val weightHistory by repository.weightHistory.collectAsState()
    val selectedStudentId by repository.selectedStudentId.collectAsState()
    val currentPrescription by repository.currentPrescription.collectAsState()
    val videoExercises by repository.videoExercises.collectAsState()
    val conditionProtocols by repository.conditionProtocols.collectAsState()
    val safetySymptoms by repository.safetySymptoms.collectAsState()
    val safetyRecords by repository.safetyRecords.collectAsState()
    val evolutionMetrics by repository.evolutionMetrics.collectAsState()
    val evolutionTrophies by repository.evolutionTrophies.collectAsState()

    var showAccessibilityDialog by remember { mutableStateOf(false) }
    var showCatalogSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val allScreensList = listOf(
        ScreenCatalogItem("1", "Splash / Início", "Banner heroico, logo 60+ FIT e 'Começar'", "splash"),
        ScreenCatalogItem("2", "Menu Principal", "Acesso rápido às funcionalidades", "main_menu"),
        ScreenCatalogItem("2b", "Login", "E-mail, senha, entrar, esqueceu e perfis", "login"),
        ScreenCatalogItem("3", "Anamnese Gerontológica", "Saúde, cirurgias, medicamentos, dor, sono e comorbidades", "anamnese"),
        ScreenCatalogItem("4", "Avaliação Física", "Obrigatórios, IMC, Perimetria, Dobras, AGA e Comparativo", "physical_assessment"),
        ScreenCatalogItem("4b", "Escolha de Papel", "Aluno 60+ ou Profissional com avatares", "role_selection"),
        ScreenCatalogItem("5", "Dashboard do Profissional", "Status Funcional: Força, Mobilidade, Equilíbrio, Marcha, Quedas e Gráficos", "professional_dashboard"),
        ScreenCatalogItem("5b", "Cadastro do Aluno", "Dados pessoais, profissionais, plano e frequência", "student_profile"),
        ScreenCatalogItem("6", "Prescrição de Treino", "Sequências 1 a 6: Mobilidade, Alongamento, Força, Funcional, Equilíbrio e Prevenção", "workout_prescription"),
        ScreenCatalogItem("7", "Biblioteca de Vídeos (10s)", "Diferencial 60+fit: Vídeos de 10s com filtros por objetivo, região, comorbidade e nível", "video_library"),
        ScreenCatalogItem("8", "Treinos por Condição", "Área 'Treino direcionado': Hipertensão, Diabetes, Parkinson, AVC, etc.", "condition_workouts"),
        ScreenCatalogItem("8b", "Início do Aluno (60+ FIT)", "Musculação e Funcionalidade • Painel principal", "student_home"),
        ScreenCatalogItem("9", "Sistema de Segurança", "Atenção antes do treino: Tontura, dor no peito, falta de ar, mal-estar e quedas", "safety_check"),
        ScreenCatalogItem("9b", "Treino de Hoje", "Treino A, lista de exercícios e 'Iniciar Treino'", "workout_overview"),
        ScreenCatalogItem("10", "Evolução do Aluno", "Sua evolução desde a primeira avaliação: Peso, Força, Preensão, Equilíbrio, Marcha, Flexibilidade e Medidas", "evolution"),
        ScreenCatalogItem("10b", "Execução Ativa do Exercício", "Player de vídeo, áudio-guia e descanso", "exercise_active/ex_2"),
        ScreenCatalogItem("11", "Minha Saúde", "Sinais vitais: batimentos, pressão arterial e saturação", "health"),
        ScreenCatalogItem("12", "Lembretes e Hábitos", "Horários para água, treino e remédios", "reminders"),
        ScreenCatalogItem("13", "Planos e Pagamento", "60+fit Essencial, Gerontológico e Premium", "plans"),
        ScreenCatalogItem("14", "Área do Profissional", "Gestão de alunos, avaliações e prescrição", "professional_area"),
        ScreenCatalogItem("15", "Banco Hostinger (MySQL)", "Configurar API PHP, testar conexão e sincronizar", "hostinger_config")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            // Tela 1: Splash (Como estava originalmente, com hero banner e botão Começar)
            composable("splash") {
                SplashScreen(
                    onStartClick = { navController.navigate("student_home") },
                    onAccessibilityClick = { showAccessibilityDialog = true }
                )
            }

            // Tela 2: Menu Principal (Com as 9 opções e slogan solicitado)
            composable("main_menu") {
                MainMenuScreen(
                    onNavigate = { route ->
                        if (route == "contact_professional") {
                            Toast.makeText(context, "Fale com o Profissional: Envie uma mensagem no suporte ou fale com seu instrutor.", Toast.LENGTH_LONG).show()
                        } else {
                            navController.navigate(route)
                        }
                    },
                    onBackToStart = { navController.popBackStack() }
                )
            }

            // Login
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { userType ->
                        if (userType == UserType.STUDENT) {
                            navController.navigate("student_home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            navController.navigate("professional_area") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    onCreateAccountClick = { navController.navigate("register") },
                    onRoleSelectionClick = { navController.navigate("role_selection") },
                    onAccessibilityClick = { showAccessibilityDialog = true }
                )
            }

            // Tela 3: Cadastro
            composable("register") {
                RegisterScreen(
                    onBackClick = { navController.popBackStack() },
                    onContinueClick = { newProfile ->
                        repository.updateStudentProfile(newProfile)
                        navController.navigate("role_selection")
                    },
                    onLoginClick = { navController.navigate("login") }
                )
            }

            // Tela 4: Escolha de Papel
            composable("role_selection") {
                RoleSelectionScreen(
                    onBackClick = { navController.popBackStack() },
                    onRoleSelected = { role ->
                        if (role == UserType.STUDENT) {
                            navController.navigate("student_profile")
                        } else {
                            navController.navigate("professional_dashboard")
                        }
                    }
                )
            }

            // Tela 5: Dashboard do Profissional (Status Funcional Completo: Força, Mobilidade, Equilíbrio, Marcha, Quedas, etc)
            composable("professional_dashboard") {
                ProfessionalDashboardScreen(
                    students = professionalStudents,
                    selectedStudentId = selectedStudentId,
                    onSelectStudent = { repository.selectStudent(it) },
                    getFunctionalProfile = { repository.getFunctionalProfileForStudent(it) },
                    onNavigateToAnamnese = { navController.navigate("anamnese") },
                    onNavigateToAssessment = { navController.navigate("physical_assessment") },
                    onNavigateToWorkout = { navController.navigate("workout_prescription") },
                    onNavigateToConditionWorkouts = { navController.navigate("condition_workouts") },
                    onNavigateToSafetyCheck = { navController.navigate("safety_check") },
                    onNavigateToVideoLibrary = { navController.navigate("video_library") },
                    onBackClick = { navController.popBackStack() },
                    onNavigateBottom = { route ->
                        if (route == "profile") {
                            navController.navigate("student_profile")
                        } else {
                            navController.navigate("professional_dashboard")
                        }
                    }
                )
            }
            composable("tela_5") {
                ProfessionalDashboardScreen(
                    students = professionalStudents,
                    selectedStudentId = selectedStudentId,
                    onSelectStudent = { repository.selectStudent(it) },
                    getFunctionalProfile = { repository.getFunctionalProfileForStudent(it) },
                    onNavigateToAnamnese = { navController.navigate("anamnese") },
                    onNavigateToAssessment = { navController.navigate("physical_assessment") },
                    onNavigateToWorkout = { navController.navigate("workout_prescription") },
                    onNavigateToConditionWorkouts = { navController.navigate("condition_workouts") },
                    onNavigateToSafetyCheck = { navController.navigate("safety_check") },
                    onNavigateToVideoLibrary = { navController.navigate("video_library") },
                    onBackClick = { navController.popBackStack() },
                    onNavigateBottom = { route ->
                        if (route == "profile") {
                            navController.navigate("student_profile")
                        } else {
                            navController.navigate("professional_dashboard")
                        }
                    }
                )
            }
            composable("pro_dashboard") {
                ProfessionalDashboardScreen(
                    students = professionalStudents,
                    selectedStudentId = selectedStudentId,
                    onSelectStudent = { repository.selectStudent(it) },
                    getFunctionalProfile = { repository.getFunctionalProfileForStudent(it) },
                    onNavigateToAnamnese = { navController.navigate("anamnese") },
                    onNavigateToAssessment = { navController.navigate("physical_assessment") },
                    onNavigateToWorkout = { navController.navigate("workout_prescription") },
                    onNavigateToConditionWorkouts = { navController.navigate("condition_workouts") },
                    onNavigateToSafetyCheck = { navController.navigate("safety_check") },
                    onNavigateToVideoLibrary = { navController.navigate("video_library") },
                    onBackClick = { navController.popBackStack() },
                    onNavigateBottom = { route ->
                        if (route == "profile") {
                            navController.navigate("student_profile")
                        } else {
                            navController.navigate("professional_dashboard")
                        }
                    }
                )
            }

            // Tela 5b: Cadastro / Perfil do Aluno
            composable("student_profile") {
                StudentProfileScreen(
                    profile = currentStudent,
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { updated ->
                        repository.updateStudentProfile(updated)
                        navController.navigate("anamnese")
                    },
                    onManagePlansClick = { navController.navigate("plans") }
                )
            }

            // Tela 6: Prescrição de Treino por Sequências
            composable("workout_prescription") {
                WorkoutPrescriptionScreen(
                    initialPrescription = currentPrescription,
                    students = professionalStudents,
                    onBackClick = { navController.popBackStack() },
                    onSavePrescription = { updatedPrescription ->
                        repository.updatePrescription(updatedPrescription)
                    },
                    onPreviewWorkout = { navController.navigate("workout_overview") },
                    onOpenVideoLibrary = { navController.navigate("video_library") },
                    onOpenConditionWorkouts = { navController.navigate("condition_workouts") }
                )
            }
            composable("tela_6") {
                WorkoutPrescriptionScreen(
                    initialPrescription = currentPrescription,
                    students = professionalStudents,
                    onBackClick = { navController.popBackStack() },
                    onSavePrescription = { updatedPrescription ->
                        repository.updatePrescription(updatedPrescription)
                    },
                    onPreviewWorkout = { navController.navigate("workout_overview") },
                    onOpenVideoLibrary = { navController.navigate("video_library") },
                    onOpenConditionWorkouts = { navController.navigate("condition_workouts") }
                )
            }

            // Tela 3: Anamnese Gerontológica
            composable("anamnese") {
                AnamneseScreen(
                    conditions = conditions,
                    onBackClick = { navController.popBackStack() },
                    onNextClick = { updatedConditions ->
                        repository.updateHealthConditions(updatedConditions)
                        Toast.makeText(context, "Anamnese salva com sucesso!", Toast.LENGTH_SHORT).show()
                        navController.navigate("physical_assessment")
                    }
                )
            }
            composable("tela_3") {
                AnamneseScreen(
                    conditions = conditions,
                    onBackClick = { navController.popBackStack() },
                    onNextClick = { updatedConditions ->
                        repository.updateHealthConditions(updatedConditions)
                        Toast.makeText(context, "Anamnese salva com sucesso!", Toast.LENGTH_SHORT).show()
                        navController.navigate("physical_assessment")
                    }
                )
            }

            // Tela 4: Avaliação Física
            composable("physical_assessment") {
                PhysicalAssessmentScreen(
                    assessment = assessment,
                    history = assessmentHistory,
                    comparison = repository.getComparisonWithPrevious(assessment),
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { updatedAssessment ->
                        repository.updatePhysicalAssessment(updatedAssessment)
                        Toast.makeText(context, "Avaliação física salva com sucesso!", Toast.LENGTH_SHORT).show()
                        navController.navigate("student_home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            composable("tela_4") {
                PhysicalAssessmentScreen(
                    assessment = assessment,
                    history = assessmentHistory,
                    comparison = repository.getComparisonWithPrevious(assessment),
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { updatedAssessment ->
                        repository.updatePhysicalAssessment(updatedAssessment)
                        Toast.makeText(context, "Avaliação física salva com sucesso!", Toast.LENGTH_SHORT).show()
                        navController.navigate("student_home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            // Tela 7: Biblioteca de Vídeos (10 segundos)
            composable("video_library") {
                VideoLibraryScreen(
                    videos = videoExercises,
                    onBackClick = { navController.popBackStack() },
                    onAddExerciseToWorkout = { ex ->
                        repository.addVideoExerciseToActiveWorkout(ex)
                    },
                    onToggleFavorite = { id ->
                        repository.toggleFavoriteVideoExercise(id)
                    }
                )
            }

            composable("tela_7") {
                VideoLibraryScreen(
                    videos = videoExercises,
                    onBackClick = { navController.popBackStack() },
                    onAddExerciseToWorkout = { ex ->
                        repository.addVideoExerciseToActiveWorkout(ex)
                    },
                    onToggleFavorite = { id ->
                        repository.toggleFavoriteVideoExercise(id)
                    }
                )
            }

            // Tela 8: Treinos por Condição (Treino Direcionado: Hipertensão, Diabetes tipo 2, Osteoporose, etc.)
            composable("condition_workouts") {
                ConditionWorkoutsScreen(
                    protocols = conditionProtocols,
                    onBackClick = { navController.popBackStack() },
                    onStartWorkout = { protocol ->
                        repository.prescribeConditionProtocol(protocol)
                        navController.navigate("workout_overview")
                    },
                    onNavigateToVideoLibrary = { _ ->
                        navController.navigate("video_library")
                    }
                )
            }

            composable("tela_8") {
                ConditionWorkoutsScreen(
                    protocols = conditionProtocols,
                    onBackClick = { navController.popBackStack() },
                    onStartWorkout = { protocol ->
                        repository.prescribeConditionProtocol(protocol)
                        navController.navigate("workout_overview")
                    },
                    onNavigateToVideoLibrary = { _ ->
                        navController.navigate("video_library")
                    }
                )
            }

            // Tela Inicial do Aluno (60+fit)
            composable("student_home") {
                StudentHomeScreen(
                    profile = currentStudent,
                    onProfileClick = { navController.navigate("student_profile") },
                    onAssessmentClick = { navController.navigate("physical_assessment") },
                    onWorkoutClick = { navController.navigate("safety_check") },
                    onConditionWorkoutsClick = { navController.navigate("condition_workouts") },
                    onSafetyCheckClick = { navController.navigate("safety_check") },
                    onExercisesClick = { navController.navigate("video_library") },
                    onEvolutionClick = { navController.navigate("evolution") },
                    onHealthClick = { navController.navigate("health") },
                    onPlansClick = { navController.navigate("plans") },
                    onNotificationsClick = { navController.navigate("reminders") },
                    onStartWorkoutClick = { navController.navigate("safety_check") },
                    onProgressClick = { navController.navigate("evolution") },
                    onRemindersClick = { navController.navigate("reminders") },
                    onAccessibilityClick = { showAccessibilityDialog = true },
                    onNavigateBottom = { route ->
                        if (route != "student_home") {
                            navController.navigate(route)
                        }
                    }
                )
            }

            // Tela 9: Sistema de Segurança — Atenção antes do treino
            composable("safety_check") {
                SafetyCheckScreen(
                    symptoms = safetySymptoms,
                    records = safetyRecords,
                    studentProfile = currentStudent,
                    onBackClick = { navController.popBackStack() },
                    onProceedToWorkout = { navController.navigate("workout_overview") },
                    onSaveRecord = { record ->
                        repository.recordSafetyCheck(record)
                    }
                )
            }

            composable("tela_9") {
                SafetyCheckScreen(
                    symptoms = safetySymptoms,
                    records = safetyRecords,
                    studentProfile = currentStudent,
                    onBackClick = { navController.popBackStack() },
                    onProceedToWorkout = { navController.navigate("workout_overview") },
                    onSaveRecord = { record ->
                        repository.recordSafetyCheck(record)
                    }
                )
            }

            // Tela 9b / Treino de Hoje
            composable("workout_overview") {
                WorkoutScreen(
                    workout = workout,
                    onBackClick = { navController.popBackStack() },
                    onExerciseClick = { exercise ->
                        navController.navigate("exercise_active/${exercise.id}")
                    },
                    onStartWorkoutClick = {
                        val firstExerciseId = workout.exercises.firstOrNull()?.id ?: "ex_1"
                        navController.navigate("exercise_active/$firstExerciseId")
                    },
                    onSafetyCheckClick = { navController.navigate("safety_check") }
                )
            }

            // Tela 10: Execução Ativa do Exercício
            composable(
                route = "exercise_active/{exerciseId}",
                arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
            ) { backStackEntry ->
                val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: "ex_2"
                val exercise = workout.exercises.find { it.id == exerciseId }
                    ?: workout.exercises.first()

                ExerciseActiveScreen(
                    exercise = exercise,
                    onBackClick = { navController.popBackStack() },
                    onCompleteExercise = { id ->
                        repository.completeExercise(id)
                        navController.popBackStack()
                    },
                    onSpeakInstruction = { instruction ->
                        onSpeak(instruction)
                    }
                )
            }

            // Tela 11: Minha Saúde
            composable("health") {
                HealthScreen(
                    assessment = assessment,
                    onBackClick = { navController.popBackStack() },
                    onUpdateMetrics = { updated ->
                        repository.updatePhysicalAssessment(updated)
                    }
                )
            }

            // Tela 10: Evolução (Sua evolução desde a primeira avaliação)
            composable("evolution") {
                EvolutionScreen(
                    metrics = evolutionMetrics,
                    trophies = evolutionTrophies,
                    history = weightHistory,
                    currentWeight = assessment.weightKg,
                    studentName = currentStudent.name,
                    onBackClick = { navController.popBackStack() },
                    onNavigateBottom = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable("tela_10") {
                EvolutionScreen(
                    metrics = evolutionMetrics,
                    trophies = evolutionTrophies,
                    history = weightHistory,
                    currentWeight = assessment.weightKg,
                    studentName = currentStudent.name,
                    onBackClick = { navController.popBackStack() },
                    onNavigateBottom = { route ->
                        navController.navigate(route)
                    }
                )
            }

            // Tela 13: Lembretes
            composable("reminders") {
                RemindersScreen(
                    reminders = reminders,
                    onBackClick = { navController.popBackStack() },
                    onToggleReminder = { id -> repository.toggleReminder(id) },
                    onAddReminder = { title, time, type -> repository.addReminder(title, time, type) }
                )
            }

            // Tela 14: Planos e Pagamento
            composable("plans") {
                PlansScreen(
                    plans = plans,
                    selectedPlanId = selectedPlanId,
                    userSubscription = userSubscription,
                    onBackClick = { navController.popBackStack() },
                    onSelectPlan = { id -> repository.selectPlan(id) },
                    onConfirmSubscription = { planId, period, method ->
                        repository.subscribeToPlan(planId, period, method)
                    },
                    onCancelSubscription = {
                        repository.cancelSubscription()
                    }
                )
            }

            // Tela 15: Área do Profissional
            composable("professional_area") {
                ProfessionalScreen(
                    students = professionalStudents,
                    plans = plans,
                    onCreatePlan = { newPlan -> repository.addProfessionalPlan(newPlan) },
                    onDeletePlan = { planId -> repository.deleteProfessionalPlan(planId) },
                    onNavigateToHostingerConfig = { navController.navigate("hostinger_config") },
                    onNavigateToDashboard = { navController.navigate("professional_dashboard") },
                    onNavigateToPrescription = { navController.navigate("workout_prescription") },
                    onBackClick = { navController.popBackStack() },
                    onNavigateBottom = { route ->
                        if (route == "profile") {
                            navController.navigate("student_profile")
                        } else {
                            navController.navigate("professional_area")
                        }
                    }
                )
            }

            // Tela 16: Banco de Dados Hostinger (MySQL)
            composable("hostinger_config") {
                HostingerConfigScreen(
                    repository = repository,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Bottom Nav Alias for Profile
            composable("profile") {
                StudentProfileScreen(
                    profile = currentStudent,
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { updated ->
                        repository.updateStudentProfile(updated)
                        navController.popBackStack()
                    },
                    onManagePlansClick = { navController.navigate("plans") }
                )
            }
        }

        // Floating Screen Navigator Quick Switcher
        FloatingActionButton(
            onClick = { showCatalogSheet = true },
            containerColor = Color(0xFF1E293B),
            contentColor = FitLime,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .systemBarsPadding()
                .padding(top = 10.dp, end = 12.dp)
                .size(44.dp)
                .testTag("all_screens_catalog_fab")
        ) {
            Icon(
                imageVector = Icons.Default.GridView,
                contentDescription = "Menu com todas as 16 telas",
                modifier = Modifier.size(20.dp)
            )
        }

        // Accessibility Settings Modal Dialog
        if (showAccessibilityDialog) {
            AccessibilityDialog(
                state = accessibilityState,
                onStateChange = { repository.updateAccessibility(it) },
                onDismiss = { showAccessibilityDialog = false }
            )
        }

        // 16 Screens Catalog Bottom Sheet
        if (showCatalogSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCatalogSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Navegador de Telas (1 a 16)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { showCatalogSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar")
                        }
                    }

                    Text(
                        text = "Toque em qualquer tela para visualizar a interface correspondente:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(allScreensList) { item ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showCatalogSheet = false
                                        navController.navigate(item.route)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(FitLimeDark),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = item.id,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = item.subtitle,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
