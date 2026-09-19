# Guia de Configuração do Banco de Dados 60+ FIT na Hostinger

Este guia ensina o passo a passo para hospedar o banco de dados MySQL e a API do aplicativo **60+ FIT** no painel da **Hostinger** (hPanel).

---

## 📁 Arquivos Inclusos na Pasta `hostinger_backend/`:
1. `database_schema.sql` - Script completo com todas as tabelas, índices e planos padrão do 60+fit.
2. `config.php` - Arquivo de conexão do banco de dados MySQL via PDO.
3. `api.php` - API REST JSON para comunicação com o aplicativo Android (possui endpoint `action=create_tables`).
4. `install.php` - Instalador web visual com 1 clique pelo navegador.

---

## 🚀 Passo a Passo no Painel da Hostinger (hPanel):

### 1️⃣ Passo 1: Criar o Banco de Dados MySQL na Hostinger
1. Acesse sua conta na **Hostinger** e abra o painel **hPanel** do seu domínio ou hospedagem.
2. No menu lateral esquerdo, procure e clique em **Bancos de Dados** -> **Bancos de Dados MySQL**.
3. Em **Criar Novo Banco de Dados e Usuário MySQL**, preencha:
   - **Nome do Banco de Dados**: ex: `fit60` *(a Hostinger adicionará um prefixo, ex: `u123456789_fit60`)*
   - **Nome de Usuário**: ex: `admin` *(ficará como `u123456789_admin`)*
   - **Senha**: crie uma senha forte e guarde-a com você.
4. Clique no botão **Criar**.

---

### 2️⃣ Passo 2: Como Criar as 10 Tabelas no MySQL (Escolha 1 das 3 Opções)

Você pode criar todas as tabelas de 3 formas muito fáceis:

#### 🟢 Opção A: Pelo Próprio Aplicativo 60+ FIT (Mais Rápido)
1. Envie os arquivos PHP para a pasta `public_html/api/` (veja o Passo 3 e 4).
2. No aplicativo, acesse **Área do Profissional** -> **Banco de Dados Hostinger**.
3. Digite sua URL (ex: `https://seusite.com.br/api/api.php`).
4. Toque no botão verde **"⚡ Criar Tabelas no MySQL da Hostinger"**.
5. O aplicativo enviará o comando e criará todas as 10 tabelas e os 3 planos comerciais automaticamente!

#### 🌐 Opção B: Pelo Navegador com o Instalador Web (1 Clique)
1. Após enviar os arquivos para a Hostinger, abra no navegador:
   `https://seusite.com.br/api/install.php`
2. Na página que abrir, confira os dados do banco e clique em:
   **"⚡ Criar Todas as 10 Tabelas no MySQL da Hostinger"**.
3. Você verá os 10 itens verdes confirmados.

#### 🗄️ Opção C: Pelo phpMyAdmin da Hostinger
1. No hPanel da Hostinger, clique no botão **Entrar no phpMyAdmin** ao lado do banco criado.
2. No menu superior do phpMyAdmin, clique na aba **Importar** (ou *Import*).
3. Clique em **Escolher arquivo** e selecione o arquivo:
   `hostinger_backend/database_schema.sql`
4. Role até o final da página e clique em **Importar**.

---

### 3️⃣ Passo 3: Configurar o arquivo `config.php`
1. Abra o arquivo `hostinger_backend/config.php` em qualquer editor de texto.
2. Preencha os campos com os dados criados no Passo 1:
   ```php
   define('DB_HOST', 'localhost'); // Mantenha 'localhost' na Hostinger
   define('DB_NAME', 'u123456789_fit60'); // O nome completo com prefixo
   define('DB_USER', 'u123456789_admin'); // O usuário completo com prefixo
   define('DB_PASS', 'SuaSenhaCriadaNoPasso1');
   define('API_SECRET_TOKEN', 'fit60_token_seguro_2026');
   ```
3. Salve o arquivo.

---

### 4️⃣ Passo 4: Enviar os Arquivos para a Hostinger
1. No hPanel da Hostinger, vá em **Arquivos** -> **Gerenciador de Arquivos**.
2. Abra a pasta pública do seu site: `public_html`.
3. Crie uma pasta chamada `api` (caminho: `public_html/api/`).
4. Faça o upload dos dois arquivos PHP para dentro dessa pasta:
   - `config.php`
   - `api.php`
5. Pronto! O endpoint da sua API estará acessível em:
   `https://seusite.com.br/api/api.php`

---

### 5️⃣ Passo 5: Testar no Navegador
Abra no seu navegador:
`https://seusite.com.br/api/api.php?action=test`

Deverá aparecer uma resposta JSON como:
```json
{
    "success": true,
    "message": "Conexão com Banco de Dados Hostinger realizada com sucesso!",
    "server_time": "2026-09-17 12:00:00",
    "mysql_version": "10.11.8-MariaDB",
    "students_count": 1,
    "plans_count": 3,
    "status": "ONLINE"
}
```

---

### 6️⃣ Passo 6: Conectar no Aplicativo 60+ FIT
1. No aplicativo Android, acesse a **Área do Profissional** (ou menu de navegação rápida).
2. Toque em **Configurar Banco de Dados Hostinger**.
3. Insira a URL da sua API: `https://seusite.com.br/api/api.php`
4. Toque em **Testar Conexão** e em seguida em **Sincronizar Dados**.
5. O aplicativo estará sincronizado em tempo real com o seu banco de dados MySQL na Hostinger!
