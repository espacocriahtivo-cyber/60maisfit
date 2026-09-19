<?php
/**
 * 60+ FIT - Instalador Automático de Tabelas MySQL (Hostinger)
 * 
 * Acesse este arquivo pelo navegador:
 * https://seusite.com.br/api/install.php
 */

require_once __DIR__ . '/config.php';

$message = '';
$status = '';
$tablesCreated = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'install') {
    try {
        $pdo = getDbConnection();
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

        $status = 'success';
        $message = 'Todas as 10 tabelas MySQL foram criadas com sucesso no seu banco da Hostinger!';
        $tablesCreated = [
            'students' => 'Dados cadastrais, anamnese e objetivos dos alunos 60+',
            'health_conditions' => 'Histórico de saúde (pressão alta, diabetes, artrose, etc.)',
            'physical_assessments' => 'Avaliações físicas e testes de marcha / sentar e levantar',
            'plans' => 'Planos de assinatura (Essencial, Gerontológico, Premium e personalizados)',
            'subscriptions' => 'Assinaturas ativas, datas de renovação e recorrência',
            'payments' => 'Histórico de pagamentos (Pix automático, cartões, etc.)',
            'workouts' => 'Fichas de treino personalizadas (Treino A, B, etc.)',
            'exercises' => 'Exercícios com séries, repetições e instruções ergonômicas',
            'reminders' => 'Lembretes de horários (água, treino, medicação)',
            'weight_history' => 'Evolução de peso e composição corporal'
        ];

    } catch (Exception $e) {
        $status = 'error';
        $message = 'Erro ao conectar ou criar tabelas no MySQL: ' . $e->getMessage();
    }
}
?>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>60+ FIT - Instalador de Tabelas MySQL Hostinger</title>
    <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><rect width='100' height='100' rx='20' fill='%234d7c0f'/><text x='50%25' y='68%25' font-family='Arial,sans-serif' font-weight='900' font-size='50' fill='white' text-anchor='middle'>60+</text></svg>">
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
            background-color: #f1f5f9;
            color: #1e293b;
            margin: 0;
            padding: 30px 15px;
            display: flex;
            justify-content: center;
        }
        .container {
            max-width: 720px;
            width: 100%;
            background: #ffffff;
            border-radius: 16px;
            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.08);
            padding: 32px;
        }
        .header {
            display: flex;
            align-items: center;
            gap: 16px;
            border-bottom: 2px solid #e2e8f0;
            padding-bottom: 20px;
            margin-bottom: 24px;
        }
        .logo {
            background: #4d7c0f;
            color: #ffffff;
            font-weight: 800;
            font-size: 20px;
            padding: 12px 18px;
            border-radius: 12px;
        }
        h1 {
            font-size: 24px;
            margin: 0 0 4px 0;
            color: #0f172a;
        }
        p {
            color: #64748b;
            margin: 0;
            font-size: 15px;
            line-height: 1.5;
        }
        .config-box {
            background: #f8fafc;
            border: 1px solid #cbd5e1;
            border-radius: 12px;
            padding: 18px;
            margin-bottom: 24px;
        }
        .config-item {
            display: flex;
            justify-content: space-between;
            padding: 6px 0;
            font-size: 14px;
        }
        .config-label {
            color: #64748b;
            font-weight: 500;
        }
        .config-value {
            font-family: monospace;
            font-weight: 600;
            color: #0f172a;
        }
        .btn-install {
            background: #4d7c0f;
            color: white;
            border: none;
            border-radius: 12px;
            padding: 16px 24px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            width: 100%;
            transition: background 0.2s;
            box-shadow: 0 4px 12px rgba(77, 124, 15, 0.25);
        }
        .btn-install:hover {
            background: #3f6212;
        }
        .alert {
            padding: 16px 20px;
            border-radius: 12px;
            margin-bottom: 24px;
            font-size: 15px;
            font-weight: 500;
        }
        .alert-success {
            background: #ecfdf5;
            color: #065f46;
            border: 1.5px solid #10b981;
        }
        .alert-error {
            background: #fef2f2;
            color: #991b1b;
            border: 1.5px solid #ef4444;
        }
        .table-list {
            margin-top: 24px;
            display: grid;
            gap: 10px;
        }
        .table-item {
            background: #f8fafc;
            border: 1px solid #e2e8f0;
            border-radius: 10px;
            padding: 12px 16px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .table-name {
            font-family: monospace;
            font-weight: bold;
            color: #047857;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .table-desc {
            font-size: 13px;
            color: #64748b;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div class="logo">60+ FIT</div>
            <div>
                <h1>Instalador de Tabelas MySQL</h1>
                <p>Configuração do Banco de Dados para Hospedagem Hostinger</p>
            </div>
        </div>

        <?php if (!empty($message)): ?>
            <div class="alert alert-<?php echo $status; ?>">
                <?php echo htmlspecialchars($message); ?>
            </div>
        <?php endif; ?>

        <div class="config-box">
            <div class="config-item">
                <span class="config-label">Host do MySQL:</span>
                <span class="config-value"><?php echo htmlspecialchars(DB_HOST); ?></span>
            </div>
            <div class="config-item">
                <span class="config-label">Nome do Banco de Dados:</span>
                <span class="config-value"><?php echo htmlspecialchars(DB_NAME); ?></span>
            </div>
            <div class="config-item">
                <span class="config-label">Usuário do Banco:</span>
                <span class="config-value"><?php echo htmlspecialchars(DB_USER); ?></span>
            </div>
            <div class="config-item">
                <span class="config-label">Conjunto de Caracteres:</span>
                <span class="config-value">utf8mb4 (Acentos e Emojis)</span>
            </div>
        </div>

        <form method="POST">
            <input type="hidden" name="action" value="install">
            <button type="submit" class="btn-install">
                ⚡ Criar Todas as 10 Tabelas no MySQL da Hostinger
            </button>
        </form>

        <?php if (!empty($tablesCreated)): ?>
            <div class="table-list">
                <h3 style="margin: 16px 0 8px 0;">Tabelas Criadas e Prontas para Uso:</h3>
                <?php foreach ($tablesCreated as $tbl => $desc): ?>
                    <div class="table-item">
                        <div class="table-name">
                            <span>✅</span> <?php echo $tbl; ?>
                        </div>
                        <div class="table-desc">
                            <?php echo $desc; ?>
                        </div>
                    </div>
                <?php endforeach; ?>
            </div>
        <?php endif; ?>

        <div style="margin-top: 30px; border-top: 1px solid #e2e8f0; padding-top: 16px; font-size: 13px; color: #94a3b8; text-align: center;">
            Após criar as tabelas, abra o aplicativo 60+ FIT e conecte informando a URL da sua API (ex: <code>https://seusite.com.br/api/api.php</code>).
        </div>
    </div>
</body>
</html>
