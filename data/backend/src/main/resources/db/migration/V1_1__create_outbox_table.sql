-- Tabela para armazenar mensagens de outbox
CREATE TABLE IF NOT EXISTS outbox_messages (
    id UUID PRIMARY KEY,
    occurred_on TIMESTAMP NOT NULL,
    type VARCHAR(255) NOT NULL,
    data TEXT NOT NULL,
    processed_date TIMESTAMP
);

-- Índice para buscar rapidamente mensagens não processadas
CREATE INDEX IF NOT EXISTS idx_outbox_messages_processed_date
ON outbox_messages (processed_date)
WHERE processed_date IS NULL;

-- Índice para ordenar por data de ocorrência
CREATE INDEX IF NOT EXISTS idx_outbox_messages_occurred_on
ON outbox_messages (occurred_on);

-- Comentários na tabela e colunas
COMMENT ON TABLE outbox_messages IS 'Armazena mensagens a serem publicadas, parte do padrão Outbox';
COMMENT ON COLUMN outbox_messages.id IS 'Identificador único da mensagem';
COMMENT ON COLUMN outbox_messages.occurred_on IS 'Data e hora em que o evento ocorreu';
COMMENT ON COLUMN outbox_messages.type IS 'Tipo/classe do evento';
COMMENT ON COLUMN outbox_messages.data IS 'Dados serializados do evento em formato JSON';
COMMENT ON COLUMN outbox_messages.processed_date IS 'Data e hora em que a mensagem foi processada, NULL se ainda não foi'; 