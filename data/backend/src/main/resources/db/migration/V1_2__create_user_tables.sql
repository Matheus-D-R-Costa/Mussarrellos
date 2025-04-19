-- Cria o esquema 'users' se não existir
CREATE SCHEMA IF NOT EXISTS users;

-- Tabela de clientes
CREATE TABLE IF NOT EXISTS users.clients (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    email_updated_date TIMESTAMP,
    password_updated_date TIMESTAMP
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_clients_email
ON users.clients (email);

-- Comentários na tabela e colunas
COMMENT ON TABLE users.clients IS 'Armazena os dados de registro dos clientes';
COMMENT ON COLUMN users.clients.id IS 'Identificador único do cliente';
COMMENT ON COLUMN users.clients.email IS 'Email do cliente, usado para login';
COMMENT ON COLUMN users.clients.password IS 'Senha do cliente, armazenada de forma segura (hash)';
COMMENT ON COLUMN users.clients.registration_date IS 'Data e hora do registro do cliente';
COMMENT ON COLUMN users.clients.email_updated_date IS 'Data e hora da última atualização do email';
COMMENT ON COLUMN users.clients.password_updated_date IS 'Data e hora da última atualização da senha'; 