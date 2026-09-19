<?php
/**
 * 60+ FIT - Configuração do Banco de Dados MySQL na Hostinger
 * 
 * Preencha os dados abaixo com as informações fornecidas no hPanel da Hostinger:
 * 1. Acesse o hPanel -> Bancos de Dados -> Bancos de Dados MySQL
 * 2. Crie ou copie o nome do banco, usuário e senha.
 */

// Evitar exibição direta de erros HTML que quebrem JSON na resposta
error_reporting(E_ALL);
ini_set('display_errors', 0);

// Configurações de Conexão com o Banco de Dados Hostinger
define('DB_HOST', 'localhost'); // Na Hostinger, o host do MySQL normalmente é 'localhost'
define('DB_NAME', 'u123456789_fit60'); // Substitua pelo Nome do Banco criado no hPanel
define('DB_USER', 'u123456789_admin'); // Substitua pelo Usuário do Banco criado no hPanel
define('DB_PASS', 'SuaSenhaSeguraAqui123!'); // Substitua pela Senha configurada no hPanel
define('DB_CHARSET', 'utf8mb4');

// Chave de Segurança para autenticação da API (Token da Hostinger)
// O aplicativo envia o cabeçalho X-API-KEY ou o parâmetro ?token=
define('API_SECRET_TOKEN', 'rDEe8IwynGbuFLrNqRxcUZdVU5xcHdmSqp8VGxJQcec65c7d');

/**
 * Função para obter conexão PDO com o MySQL da Hostinger
 */
function getDbConnection() {
    static $pdo = null;
    if ($pdo !== null) {
        return $pdo;
    }

    $dsn = "mysql:host=" . DB_HOST . ";dbname=" . DB_NAME . ";charset=" . DB_CHARSET;
    $options = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    try {
        $pdo = new PDO($dsn, DB_USER, DB_PASS, $options);
        return $pdo;
    } catch (PDOException $e) {
        http_response_code(500);
        echo json_encode([
            'success' => false,
            'error' => 'Erro de conexão com o banco de dados Hostinger: ' . $e->getMessage()
        ], JSON_UNESCAPED_UNICODE);
        exit;
    }
}

/**
 * Resposta padrão em formato JSON
 */
function sendJsonResponse($data, $statusCode = 200) {
    http_response_code($statusCode);
    header('Content-Type: application/json; charset=utf-8');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
    header('Access-Control-Allow-Headers: Content-Type, Authorization, X-API-KEY');
    echo json_encode($data, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    exit;
}
