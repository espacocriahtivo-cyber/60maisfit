-- ==============================================================================
-- 60+ FIT - ESQUEMA DE BANCO DE DADOS MYSQL PARA HOSTINGER
-- Compatível com: MySQL 5.7+, MySQL 8.0+, MariaDB 10.3+
-- Suporte completo a UTF-8mb4 (emojis e acentuação da língua portuguesa)
-- ==============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------------------------
-- 1. TABELA DE ALUNOS (STUDENTS)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `students` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 2. TABELA DE CONDIÇÕES DE SAÚDE / ANAMNESE (HEALTH_CONDITIONS)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `health_conditions` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 3. TABELA DE AVALIAÇÃO FÍSICA E GERONTOLÓGICA (PHYSICAL_ASSESSMENTS)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `physical_assessments` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 4. TABELA DE PLANOS DE ASSINATURA (PLANS)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `plans` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Inserir os 3 Planos Oficiais 60+fit
INSERT INTO `plans` (`id`, `name`, `monthly_price`, `price_label`, `description`, `features_json`, `is_recommended`, `is_created_by_professional`, `author_name`)
VALUES
(
  'plan_essencial',
  '60+fit Essencial',
  79.90,
  'R$ 79,90/mês',
  'Plano base com treinos personalizados e acesso completo ao app.',
  '["Aplicativo 60+fit completo", "Biblioteca de exercícios com vídeos e áudio", "Treinos personalizados de força e funcionalidade", "Acompanhamento básico", "Evolução de peso e bem-estar"]',
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
  '["Tudo do Essencial", "Anamnese detalhada e histórico de saúde", "Avaliação física completa", "Avaliações funcionais (marcha, sentar e levantar)", "Acompanhamento da evolução", "Protocolos específicos para grupos especiais (artrose, osteoporose, hipertensão)"]',
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
  '["Tudo do Gerontológico", "Acompanhamento mais próximo com ajustes frequentes", "Avaliações periódicas", "Vídeos personalizados gravados pelo profissional", "Contato profissional direto", "Relatórios de evolução para médicos e familiares"]',
  0,
  0,
  '60+fit'
)
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);

-- ------------------------------------------------------------------------------
-- 5. TABELA DE ASSINATURAS DE ALUNOS (SUBSCRIPTIONS)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `subscriptions` (
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
  CONSTRAINT `fk_sub_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_sub_plan` FOREIGN KEY (`plan_id`) REFERENCES `plans` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 6. TABELA DE TRANSAÇÕES E PAGAMENTOS (PAYMENTS)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `payments` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 7. TABELA DE TREINOS E EXERCÍCIOS (WORKOUTS & EXERCISES)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `workouts` (
  `id` VARCHAR(50) NOT NULL PRIMARY KEY,
  `student_id` VARCHAR(50) NOT NULL,
  `code` VARCHAR(50) DEFAULT 'Treino A',
  `title` VARCHAR(150) DEFAULT 'Força e funcionalidade',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_workout_student` (`student_id`),
  CONSTRAINT `fk_workout_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `exercises` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 8. TABELA DE LEMBRETES E ROTINAS (REMINDERS)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `reminders` (
  `id` VARCHAR(50) NOT NULL PRIMARY KEY,
  `student_id` VARCHAR(50) NOT NULL,
  `title` VARCHAR(150) NOT NULL,
  `time_or_date` VARCHAR(50) NOT NULL,
  `icon_type` VARCHAR(30) DEFAULT 'workout',
  `enabled` TINYINT(1) DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_reminder_student` (`student_id`),
  CONSTRAINT `fk_reminder_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- 9. TABELA DE HISTÓRICO DE PESO E EVOLUÇÃO (WEIGHT_HISTORY)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `weight_history` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `student_id` VARCHAR(50) NOT NULL,
  `month_label` VARCHAR(30) NOT NULL,
  `weight_kg` FLOAT NOT NULL,
  `recorded_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_weight_student` (`student_id`),
  CONSTRAINT `fk_weight_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Inserir Aluno Padrão (Maria Silva)
INSERT INTO `students` (
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
) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);

INSERT INTO `health_conditions` (
  `student_id`, `hypertension`, `diabetes`, `heart_conditions`,
  `arthrosis_arthritis`, `osteoporosis`, `obesity`, `others`, `notes`
) VALUES (
  'student_default', 1, 0, 0, 1, 0, 0, 0,
  'Sinto um leve desconforto no joelho direito em dias frios.'
) ON DUPLICATE KEY UPDATE `notes`=VALUES(`notes`);

INSERT INTO `physical_assessments` (
  `student_id`, `weight_kg`, `height_m`, `blood_pressure`, `heart_rate_bpm`,
  `oxygen_saturation`, `sit_to_stand_reps`, `walk_distance_meters`, `flexibility_rating`
) VALUES (
  'student_default', '62', '1,58', '120 / 80 mmHg', 72, 98, 14, 420, 'Adequada para a faixa etária'
);

INSERT INTO `subscriptions` (
  `student_id`, `plan_id`, `plan_name`, `monthly_price`, `billing_period`,
  `payment_method`, `status`, `current_billing_amount`, `next_renewal_date`, `auto_renew`, `last_payment_date`
) VALUES (
  'student_default', 'plan_gerontologico', '60+fit Gerontológico', 129.90, 'MENSAL',
  'PIX_AUTOMATICO', 'Ativa', 'R$ 129,90/mês', '16/10/2026', 1, '16/09/2026'
);

SET FOREIGN_KEY_CHECKS = 1;
