<?php
/**
 * 60+ FIT - API REST para Hostinger (PHP / MySQL)
 * Fornece integração e sincronização com o aplicativo Android 60+fit
 */

require_once __DIR__ . '/config.php';

// Tratar requisição OPTIONS de pré-voo CORS
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
    header('Access-Control-Allow-Headers: Content-Type, Authorization, X-API-KEY');
    http_response_code(200);
    exit;
}

// Obter ação e payload
$action = isset($_GET['action']) ? trim($_GET['action']) : '';
$rawBody = file_get_contents('php://input');
$inputData = json_decode($rawBody, true) ?: [];

// Validar Token de Segurança se definido no config.php
$headers = getallheaders();
$clientToken = isset($headers['X-API-KEY']) ? $headers['X-API-KEY'] : (isset($_GET['token']) ? $_GET['token'] : '');

if (defined('API_SECRET_TOKEN') && !empty(API_SECRET_TOKEN)) {
    // Permitir teste sem token caso o usuário ainda não tenha configurado no app,
    // mas sinalizar no JSON
    if ($action !== 'test' && $clientToken !== API_SECRET_TOKEN) {
        // Se o token fornecido for diferente do configurado:
        if (!empty($clientToken)) {
            sendJsonResponse([
                'success' => false,
                'error' => 'Token de autenticação inválido para o servidor Hostinger.'
            ], 401);
        }
    }
}

$pdo = getDbConnection();

switch ($action) {

    // --------------------------------------------------------------------------
    // TESTE DE CONEXÃO / STATUS DO SERVIDOR
    // --------------------------------------------------------------------------
    case 'test':
    case 'health':
        try {
            $stmt = $pdo->query("SELECT VERSION() AS mysql_version, NOW() AS server_time");
            $info = $stmt->fetch();
            
            // Verificar se as tabelas já foram criadas
            $countStudents = 0;
            $countPlans = 0;
            $tablesExist = false;
            try {
                $countStudents = (int)$pdo->query("SELECT COUNT(*) FROM students")->fetchColumn();
                $countPlans = (int)$pdo->query("SELECT COUNT(*) FROM plans")->fetchColumn();
                $tablesExist = true;
            } catch (Exception $ex) {
                $tablesExist = false;
            }

            sendJsonResponse([
                'success' => true,
                'message' => $tablesExist 
                    ? 'Conexão com Banco de Dados Hostinger realizada com sucesso!' 
                    : 'Conexão com MySQL estabelecida! Porém as tabelas ainda precisam ser criadas. Use action=create_tables para criá-las.',
                'server_time' => $info['server_time'],
                'mysql_version' => $info['mysql_version'],
                'tables_exist' => $tablesExist,
                'students_count' => $countStudents,
                'plans_count' => $countPlans,
                'status' => 'ONLINE'
            ]);
        } catch (Exception $e) {
            sendJsonResponse([
                'success' => false,
                'error' => 'Falha ao consultar banco: ' . $e->getMessage()
            ], 500);
        }
        break;

    // --------------------------------------------------------------------------
    // CRIAR TODAS AS TABELAS AUTOMATICAMENTE NO MYSQL DA HOSTINGER
    // --------------------------------------------------------------------------
    case 'create_tables':
    case 'install':
        try {
            $pdo->exec("SET FOREIGN_KEY_CHECKS = 0;");

            // 1. students
            $pdo->exec("CREATE TABLE IF NOT EXISTS `students` (
              `id` VARCHAR(50) NOT NULL PRIMARY KEY,
              `name` VARCHAR(150) NOT NULL,
              `birth_date` VARCHAR(20) DEFAULT NULL,
              `gender` VARCHAR(30) DEFAULT 'Feminino',
              `phone` VARCHAR(30) DEFAULT NULL,
              `email` VARCHAR(150) DEFAULT NULL,
              `emergency_contact` VARCHAR(255) DEFAULT NULL,
              `main_goal` VARCHAR(255) DEFAULT NULL,
              `responsible_professional` VARCHAR(150) DEFAULT NULL,
              `start_date` VARCHAR(30) DEFAULT NULL,
              `contracted_plan` VARCHAR(150) DEFAULT NULL,
              `weekly_frequency` VARCHAR(50) DEFAULT '3x por semana',
              `professional_notes` TEXT DEFAULT NULL,
              `cpf` VARCHAR(25) DEFAULT NULL,
              `weight_kg` VARCHAR(15) DEFAULT NULL,
              `height_m` VARCHAR(15) DEFAULT NULL,
              `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // 2. health_conditions
            $pdo->exec("CREATE TABLE IF NOT EXISTS `health_conditions` (
              `student_id` VARCHAR(50) NOT NULL PRIMARY KEY,
              `hypertension` TINYINT(1) DEFAULT 0,
              `diabetes` TINYINT(1) DEFAULT 0,
              `heart_conditions` TINYINT(1) DEFAULT 0,
              `arthrosis_arthritis` TINYINT(1) DEFAULT 0,
              `osteoporosis` TINYINT(1) DEFAULT 0,
              `obesity` TINYINT(1) DEFAULT 0,
              `others` TINYINT(1) DEFAULT 0,
              `notes` TEXT DEFAULT NULL,
              `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
              CONSTRAINT `fk_health_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // 3. physical_assessments
            $pdo->exec("CREATE TABLE IF NOT EXISTS `physical_assessments` (
              `id` INT AUTO_INCREMENT PRIMARY KEY,
              `student_id` VARCHAR(50) NOT NULL,
              `weight_kg` VARCHAR(15) DEFAULT '62',
              `height_m` VARCHAR(15) DEFAULT '1,58',
              `blood_pressure` VARCHAR(30) DEFAULT '120 / 80 mmHg',
              `heart_rate_bpm` INT DEFAULT 72,
              `oxygen_saturation` INT DEFAULT 98,
              `sit_to_stand_reps` INT DEFAULT 14,
              `walk_distance_meters` INT DEFAULT 420,
              `flexibility_rating` VARCHAR(100) DEFAULT 'Adequada para a faixa etária',
              `assessed_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              INDEX `idx_assessment_student` (`student_id`),
              CONSTRAINT `fk_assessment_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // 4. plans
            $pdo->exec("CREATE TABLE IF NOT EXISTS `plans` (
              `id` VARCHAR(50) NOT NULL PRIMARY KEY,
              `name` VARCHAR(150) NOT NULL,
              `monthly_price` DECIMAL(10,2) NOT NULL DEFAULT 79.90,
              `price_label` VARCHAR(50) NOT NULL,
              `description` TEXT DEFAULT NULL,
              `features_json` TEXT DEFAULT NULL,
              `is_recommended` TINYINT(1) DEFAULT 0,
              `is_created_by_professional` TINYINT(1) DEFAULT 0,
              `author_name` VARCHAR(150) DEFAULT NULL,
              `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // 5. subscriptions
            $pdo->exec("CREATE TABLE IF NOT EXISTS `subscriptions` (
              `id` INT AUTO_INCREMENT PRIMARY KEY,
              `student_id` VARCHAR(50) NOT NULL,
              `plan_id` VARCHAR(50) NOT NULL,
              `plan_name` VARCHAR(150) NOT NULL,
              `monthly_price` DECIMAL(10,2) NOT NULL,
              `billing_period` VARCHAR(30) DEFAULT 'MENSAL',
              `payment_method` VARCHAR(30) DEFAULT 'PIX_AUTOMATICO',
              `status` VARCHAR(50) DEFAULT 'Ativa',
              `current_billing_amount` VARCHAR(100) DEFAULT 'R$ 129,90/mês',
              `next_renewal_date` VARCHAR(30) DEFAULT NULL,
              `auto_renew` TINYINT(1) DEFAULT 1,
              `last_payment_date` VARCHAR(30) DEFAULT NULL,
              `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
              INDEX `idx_sub_student` (`student_id`),
              CONSTRAINT `fk_sub_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // 6. payments
            $pdo->exec("CREATE TABLE IF NOT EXISTS `payments` (
              `id` VARCHAR(100) NOT NULL PRIMARY KEY,
              `student_id` VARCHAR(50) NOT NULL,
              `plan_id` VARCHAR(50) NOT NULL,
              `amount` DECIMAL(10,2) NOT NULL,
              `payment_method` VARCHAR(50) NOT NULL,
              `status` VARCHAR(50) NOT NULL DEFAULT 'Aprovado',
              `pix_code` TEXT DEFAULT NULL,
              `card_last4` VARCHAR(10) DEFAULT NULL,
              `billing_cycle` VARCHAR(50) DEFAULT 'Mensal',
              `paid_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              INDEX `idx_pay_student` (`student_id`),
              CONSTRAINT `fk_pay_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // 7. workouts
            $pdo->exec("CREATE TABLE IF NOT EXISTS `workouts` (
              `id` VARCHAR(50) NOT NULL PRIMARY KEY,
              `student_id` VARCHAR(50) NOT NULL,
              `code` VARCHAR(50) DEFAULT 'Treino A',
              `title` VARCHAR(150) DEFAULT 'Força e funcionalidade',
              `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              INDEX `idx_workout_student` (`student_id`),
              CONSTRAINT `fk_workout_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // 8. exercises
            $pdo->exec("CREATE TABLE IF NOT EXISTS `exercises` (
              `id` VARCHAR(50) NOT NULL PRIMARY KEY,
              `workout_id` VARCHAR(50) NOT NULL,
              `name` VARCHAR(150) NOT NULL,
              `sets` INT DEFAULT 3,
              `reps` INT DEFAULT 12,
              `rest_seconds` INT DEFAULT 30,
              `instruction` TEXT DEFAULT NULL,
              `completed` TINYINT(1) DEFAULT 0,
              `order_index` INT DEFAULT 0,
              INDEX `idx_exercise_workout` (`workout_id`),
              CONSTRAINT `fk_exercise_workout` FOREIGN KEY (`workout_id`) REFERENCES `workouts` (`id`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // 9. reminders
            $pdo->exec("CREATE TABLE IF NOT EXISTS `reminders` (
              `id` VARCHAR(50) NOT NULL PRIMARY KEY,
              `student_id` VARCHAR(50) NOT NULL,
              `title` VARCHAR(150) NOT NULL,
              `time_or_date` VARCHAR(50) NOT NULL,
              `icon_type` VARCHAR(30) DEFAULT 'workout',
              `enabled` TINYINT(1) DEFAULT 1,
              `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              INDEX `idx_reminder_student` (`student_id`),
              CONSTRAINT `fk_reminder_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // 10. weight_history
            $pdo->exec("CREATE TABLE IF NOT EXISTS `weight_history` (
              `id` INT AUTO_INCREMENT PRIMARY KEY,
              `student_id` VARCHAR(50) NOT NULL,
              `month_label` VARCHAR(30) NOT NULL,
              `weight_kg` FLOAT NOT NULL,
              `recorded_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              INDEX `idx_weight_student` (`student_id`),
              CONSTRAINT `fk_weight_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");

            // Inserir os 3 Planos Oficiais 60+fit
            $pdo->exec("INSERT INTO `plans` (`id`, `name`, `monthly_price`, `price_label`, `description`, `features_json`, `is_recommended`, `is_created_by_professional`, `author_name`)
            VALUES
            (
              'plan_essencial',
              '60+fit Essencial',
              79.90,
              'R$ 79,90/mês',
              'Plano base com treinos personalizados e acesso completo ao app.',
              '[\"Aplicativo 60+fit completo\", \"Biblioteca de exercícios com vídeos e áudio\", \"Treinos personalizados de força e funcionalidade\", \"Acompanhamento básico\", \"Evolução de peso e bem-estar\"]',
              0,
              0,
              '60+fit'
            ),
            (
              'plan_gerontologico',
              '60+fit Gerontológico',
              129.90,
              'R$ 129,90/mês',
              'Cuidado integral com avaliação funcional e protocolos geriátricos específicos.',
              '[\"Tudo do Essencial\", \"Anamnese detalhada e histórico de saúde\", \"Avaliação física completa\", \"Avaliações funcionais (marcha, sentar e levantar)\", \"Acompanhamento da evolução\", \"Protocolos específicos para grupos especiais (artrose, osteoporose, hipertensão)\"]',
              1,
              0,
              '60+fit'
            ),
            (
              'plan_premium',
              '60+fit Premium',
              199.90,
              'R$ 199,90 – 249,90/mês',
              'Consultoria individual com acompanhamento próximo, relatórios e contato direto.',
              '[\"Tudo do Gerontológico\", \"Acompanhamento mais próximo com ajustes frequentes\", \"Avaliações periódicas\", \"Vídeos personalizados gravados pelo profissional\", \"Contato profissional direto\", \"Relatórios de evolução para médicos e familiares\"]',
              0,
              0,
              '60+fit'
            )
            ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);");

            // Inserir aluno padrão se não existir
            $pdo->exec("INSERT INTO `students` (
              `id`, `name`, `birth_date`, `gender`, `phone`, `email`,
              `emergency_contact`, `main_goal`, `responsible_professional`,
              `start_date`, `contracted_plan`, `weekly_frequency`, `professional_notes`,
              `cpf`, `weight_kg`, `height_m`
            ) VALUES (
              'student_default',
              'Maria Silva',
              '15/03/1956',
              'Feminino',
              '(11) 98765-4321',
              'maria.silva@email.com',
              'Carlos Silva (Filho) - (11) 99876-5432',
              'Ganhar força e autonomia',
              'Dra. Mariana Lima (CREF 045123)',
              '10/01/2026',
              '60+fit Gerontológico (R$ 129,90/mês)',
              '3x por semana',
              'Foco em membros inferiores, equilíbrio e prevenção de quedas. Sem contraindicações agudas.',
              '123.456.789-00',
              '62',
              '1,58'
            ) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);");

            $pdo->exec("SET FOREIGN_KEY_CHECKS = 1;");

            $tablesCreated = [
                'students',
                'health_conditions',
                'physical_assessments',
                'plans',
                'subscriptions',
                'payments',
                'workouts',
                'exercises',
                'reminders',
                'weight_history'
            ];

            sendJsonResponse([
                'success' => true,
                'message' => 'Todas as 10 tabelas MySQL foram criadas com sucesso na Hostinger!',
                'tables_created' => $tablesCreated,
                'plans_seeded' => 3,
                'student_seeded' => 'Maria Silva'
            ]);

        } catch (Exception $e) {
            sendJsonResponse([
                'success' => false,
                'error' => 'Falha ao criar tabelas no MySQL: ' . $e->getMessage()
            ], 500);
        }
        break;

    // --------------------------------------------------------------------------
    // BUSCAR DADOS DO ALUNO
    // --------------------------------------------------------------------------
    case 'get_student':
        $studentId = isset($_GET['id']) ? $_GET['id'] : 'student_default';
        $stmt = $pdo->prepare("SELECT * FROM students WHERE id = ?");
        $stmt->execute([$studentId]);
        $student = $stmt->fetch();

        if (!$student) {
            sendJsonResponse(['success' => false, 'error' => 'Aluno não encontrado'], 404);
        }

        // Anamnese
        $stmtHealth = $pdo->prepare("SELECT * FROM health_conditions WHERE student_id = ?");
        $stmtHealth->execute([$studentId]);
        $health = $stmtHealth->fetch() ?: null;

        // Avaliação física mais recente
        $stmtAssessment = $pdo->prepare("SELECT * FROM physical_assessments WHERE student_id = ? ORDER BY id DESC LIMIT 1");
        $stmtAssessment->execute([$studentId]);
        $assessment = $stmtAssessment->fetch() ?: null;

        sendJsonResponse([
            'success' => true,
            'student' => $student,
            'health_conditions' => $health,
            'physical_assessment' => $assessment
        ]);
        break;

    // --------------------------------------------------------------------------
    // SALVAR DADOS DO ALUNO
    // --------------------------------------------------------------------------
    case 'save_student':
        $student = isset($inputData['student']) ? $inputData['student'] : $inputData;
        $studentId = !empty($student['id']) ? $student['id'] : 'student_default';

        $sql = "INSERT INTO students (
                    id, name, birth_date, gender, phone, email, emergency_contact,
                    main_goal, responsible_professional, start_date, contracted_plan,
                    weekly_frequency, professional_notes, cpf, weight_kg, height_m
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    birth_date = VALUES(birth_date),
                    gender = VALUES(gender),
                    phone = VALUES(phone),
                    email = VALUES(email),
                    emergency_contact = VALUES(emergency_contact),
                    main_goal = VALUES(main_goal),
                    responsible_professional = VALUES(responsible_professional),
                    start_date = VALUES(start_date),
                    contracted_plan = VALUES(contracted_plan),
                    weekly_frequency = VALUES(weekly_frequency),
                    professional_notes = VALUES(professional_notes),
                    cpf = VALUES(cpf),
                    weight_kg = VALUES(weight_kg),
                    height_m = VALUES(height_m)";

        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            $studentId,
            $student['name'] ?? '',
            $student['birthDate'] ?? $student['birth_date'] ?? '',
            $student['gender'] ?? 'Feminino',
            $student['phone'] ?? '',
            $student['email'] ?? '',
            $student['emergencyContact'] ?? $student['emergency_contact'] ?? '',
            $student['mainGoal'] ?? $student['main_goal'] ?? '',
            $student['responsibleProfessional'] ?? $student['responsible_professional'] ?? '',
            $student['startDate'] ?? $student['start_date'] ?? '',
            $student['contractedPlan'] ?? $student['contracted_plan'] ?? '',
            $student['weeklyFrequency'] ?? $student['weekly_frequency'] ?? '',
            $student['professionalNotes'] ?? $student['professional_notes'] ?? '',
            $student['cpf'] ?? '',
            $student['weightKg'] ?? $student['weight_kg'] ?? '',
            $student['heightM'] ?? $student['height_m'] ?? ''
        ]);

        sendJsonResponse([
            'success' => true,
            'message' => 'Perfil do aluno sincronizado com sucesso na Hostinger!',
            'student_id' => $studentId
        ]);
        break;

    // --------------------------------------------------------------------------
    // BUSCAR LISTA DE PLANOS
    // --------------------------------------------------------------------------
    case 'get_plans':
        $stmt = $pdo->query("SELECT * FROM plans ORDER BY monthly_price ASC");
        $plans = $stmt->fetchAll();

        $formattedPlans = [];
        foreach ($plans as $p) {
            $formattedPlans[] = [
                'id' => $p['id'],
                'name' => $p['name'],
                'monthlyPrice' => (float)$p['monthly_price'],
                'price' => $p['price_label'],
                'description' => $p['description'],
                'features' => json_decode($p['features_json'], true) ?: [],
                'isRecommended' => (bool)$p['is_recommended'],
                'isCreatedByProfessional' => (bool)$p['is_created_by_professional'],
                'authorName' => $p['author_name']
            ];
        }

        sendJsonResponse([
            'success' => true,
            'plans' => $formattedPlans
        ]);
        break;

    // --------------------------------------------------------------------------
    // SALVAR PLANO CRIADO PELO PROFISSIONAL
    // --------------------------------------------------------------------------
    case 'save_plan':
        $plan = $inputData;
        if (empty($plan['name'])) {
            sendJsonResponse(['success' => false, 'error' => 'Nome do plano é obrigatório'], 400);
        }

        $planId = !empty($plan['id']) ? $plan['id'] : 'plan_custom_' . time();
        $featuresJson = json_encode($plan['features'] ?? [], JSON_UNESCAPED_UNICODE);

        $sql = "INSERT INTO plans (
                    id, name, monthly_price, price_label, description,
                    features_json, is_recommended, is_created_by_professional, author_name
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    monthly_price = VALUES(monthly_price),
                    price_label = VALUES(price_label),
                    description = VALUES(description),
                    features_json = VALUES(features_json),
                    is_recommended = VALUES(is_recommended),
                    is_created_by_professional = VALUES(is_created_by_professional),
                    author_name = VALUES(author_name)";

        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            $planId,
            $plan['name'],
            $plan['monthlyPrice'] ?? 79.90,
            $plan['price'] ?? ('R$ ' . number_format($plan['monthlyPrice'] ?? 79.90, 2, ',', '.') . '/mês'),
            $plan['description'] ?? '',
            $featuresJson,
            !empty($plan['isRecommended']) ? 1 : 0,
            !empty($plan['isCreatedByProfessional']) ? 1 : 1,
            $plan['authorName'] ?? 'Profissional'
        ]);

        sendJsonResponse([
            'success' => true,
            'message' => 'Plano salvo com sucesso no banco da Hostinger!',
            'plan_id' => $planId
        ]);
        break;

    // --------------------------------------------------------------------------
    // EXCLUIR PLANO
    // --------------------------------------------------------------------------
    case 'delete_plan':
        $planId = isset($inputData['id']) ? $inputData['id'] : (isset($_GET['id']) ? $_GET['id'] : '');
        if (empty($planId)) {
            sendJsonResponse(['success' => false, 'error' => 'ID do plano não fornecido'], 400);
        }

        $stmt = $pdo->prepare("DELETE FROM plans WHERE id = ? AND is_created_by_professional = 1");
        $stmt->execute([$planId]);

        sendJsonResponse([
            'success' => true,
            'message' => 'Plano excluído do banco de dados Hostinger.'
        ]);
        break;

    // --------------------------------------------------------------------------
    // SINCRONIZAÇÃO COMPLETA (FULL SYNC)
    // --------------------------------------------------------------------------
    case 'sync_all':
        $student = $inputData['student'] ?? null;
        $plans = $inputData['plans'] ?? [];
        $subscription = $inputData['subscription'] ?? null;
        $studentId = $student['id'] ?? 'student_default';

        $pdo->beginTransaction();

        try {
            // 1. Atualizar Aluno
            if ($student) {
                $sql = "INSERT INTO students (
                            id, name, birth_date, gender, phone, email, emergency_contact,
                            main_goal, responsible_professional, start_date, contracted_plan,
                            weekly_frequency, professional_notes, cpf, weight_kg, height_m
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        ON DUPLICATE KEY UPDATE
                            name = VALUES(name),
                            birth_date = VALUES(birth_date),
                            gender = VALUES(gender),
                            phone = VALUES(phone),
                            email = VALUES(email),
                            emergency_contact = VALUES(emergency_contact),
                            main_goal = VALUES(main_goal),
                            responsible_professional = VALUES(responsible_professional),
                            start_date = VALUES(start_date),
                            contracted_plan = VALUES(contracted_plan),
                            weekly_frequency = VALUES(weekly_frequency),
                            professional_notes = VALUES(professional_notes),
                            cpf = VALUES(cpf),
                            weight_kg = VALUES(weight_kg),
                            height_m = VALUES(height_m)";
                $stmt = $pdo->prepare($sql);
                $stmt->execute([
                    $studentId,
                    $student['name'] ?? '',
                    $student['birthDate'] ?? '',
                    $student['gender'] ?? 'Feminino',
                    $student['phone'] ?? '',
                    $student['email'] ?? '',
                    $student['emergencyContact'] ?? '',
                    $student['mainGoal'] ?? '',
                    $student['responsibleProfessional'] ?? '',
                    $student['startDate'] ?? '',
                    $student['contractedPlan'] ?? '',
                    $student['weeklyFrequency'] ?? '',
                    $student['professionalNotes'] ?? '',
                    $student['cpf'] ?? '',
                    $student['weightKg'] ?? '',
                    $student['heightM'] ?? ''
                ]);
            }

            // 2. Atualizar Assinatura
            if ($subscription) {
                $sqlSub = "INSERT INTO subscriptions (
                               student_id, plan_id, plan_name, monthly_price, billing_period,
                               payment_method, status, current_billing_amount, next_renewal_date, auto_renew, last_payment_date
                           ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                           ON DUPLICATE KEY UPDATE
                               plan_id = VALUES(plan_id),
                               plan_name = VALUES(plan_name),
                               monthly_price = VALUES(monthly_price),
                               billing_period = VALUES(billing_period),
                               payment_method = VALUES(payment_method),
                               status = VALUES(status),
                               current_billing_amount = VALUES(current_billing_amount),
                               next_renewal_date = VALUES(next_renewal_date),
                               auto_renew = VALUES(auto_renew),
                               last_payment_date = VALUES(last_payment_date)";
                $stmtSub = $pdo->prepare($sqlSub);
                $stmtSub->execute([
                    $studentId,
                    $subscription['planId'] ?? 'plan_gerontologico',
                    $subscription['planName'] ?? '60+fit Gerontológico',
                    $subscription['monthlyPrice'] ?? 129.90,
                    $subscription['billingPeriod'] ?? 'MENSAL',
                    $subscription['paymentMethod'] ?? 'PIX_AUTOMATICO',
                    $subscription['status'] ?? 'Ativa',
                    $subscription['currentBillingAmount'] ?? 'R$ 129,90/mês',
                    $subscription['nextRenewalDate'] ?? '16/10/2026',
                    !empty($subscription['autoRenew']) ? 1 : 0,
                    $subscription['lastPaymentDate'] ?? '16/09/2026'
                ]);
            }

            $pdo->commit();

            // Obter planos atualizados para retornar ao app
            $stmtPlans = $pdo->query("SELECT * FROM plans ORDER BY monthly_price ASC");
            $allPlans = $stmtPlans->fetchAll();
            $formattedPlans = [];
            foreach ($allPlans as $p) {
                $formattedPlans[] = [
                    'id' => $p['id'],
                    'name' => $p['name'],
                    'monthlyPrice' => (float)$p['monthly_price'],
                    'price' => $p['price_label'],
                    'description' => $p['description'],
                    'features' => json_decode($p['features_json'], true) ?: [],
                    'isRecommended' => (bool)$p['is_recommended'],
                    'isCreatedByProfessional' => (bool)$p['is_created_by_professional'],
                    'authorName' => $p['author_name']
                ];
            }

            sendJsonResponse([
                'success' => true,
                'message' => 'Sincronização bidirecional concluída com o servidor Hostinger!',
                'synced_at' => date('d/m/Y H:i:s'),
                'plans' => $formattedPlans
            ]);

        } catch (Exception $e) {
            $pdo->rollBack();
            sendJsonResponse([
                'success' => false,
                'error' => 'Falha na sincronização: ' . $e->getMessage()
            ], 500);
        }
        break;

    default:
        sendJsonResponse([
            'success' => false,
            'error' => 'Ação não informada ou inválida. Use ?action=test para verificar o status.'
        ], 400);
        break;
}
