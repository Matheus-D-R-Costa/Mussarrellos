-- Adiciona índice composto para otimizar a busca por mensagens não processadas ordenadas por data
CREATE INDEX IF NOT EXISTS idx_outbox_messages_process_date_occurred_on
ON outbox_messages (processed_date, occurred_on)
WHERE processed_date IS NULL;

-- Adiciona índice para busca por tipo de evento
CREATE INDEX IF NOT EXISTS idx_outbox_messages_type
ON outbox_messages (type);

-- Cria uma tabela para armazenar configurações de retenção de mensagens
CREATE TABLE IF NOT EXISTS outbox_retention_config (
    id SERIAL PRIMARY KEY,
    retention_days INTEGER NOT NULL DEFAULT 30,
    enabled BOOLEAN NOT NULL DEFAULT true,
    last_cleanup_date TIMESTAMP
);

-- Insere configuração padrão
INSERT INTO outbox_retention_config (retention_days, enabled, last_cleanup_date)
VALUES (30, true, NULL)
ON CONFLICT DO NOTHING;

-- Cria função para limpar mensagens antigas
CREATE OR REPLACE FUNCTION cleanup_processed_outbox_messages()
RETURNS INTEGER AS $$
DECLARE
    config_record RECORD;
    deleted_count INTEGER;
BEGIN
    -- Obtém configuração atual
    SELECT * INTO config_record FROM outbox_retention_config LIMIT 1;
    
    -- Se limpeza estiver habilitada
    IF config_record.enabled THEN
        -- Remove mensagens processadas mais antigas que o período de retenção
        DELETE FROM outbox_messages
        WHERE processed_date IS NOT NULL
          AND processed_date < (CURRENT_TIMESTAMP - (config_record.retention_days || ' days')::INTERVAL);
          
        GET DIAGNOSTICS deleted_count = ROW_COUNT;
        
        -- Atualiza data da última limpeza
        UPDATE outbox_retention_config
        SET last_cleanup_date = CURRENT_TIMESTAMP
        WHERE id = config_record.id;
        
        RETURN deleted_count;
    ELSE
        RETURN 0;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Comentários nas novas estruturas
COMMENT ON INDEX idx_outbox_messages_process_date_occurred_on IS 'Índice otimizado para a consulta de processamento de mensagens pendentes';
COMMENT ON INDEX idx_outbox_messages_type IS 'Índice para busca de mensagens por tipo de evento';
COMMENT ON TABLE outbox_retention_config IS 'Configurações para limpeza automática da tabela de outbox';
COMMENT ON FUNCTION cleanup_processed_outbox_messages() IS 'Função para remover mensagens processadas antigas da tabela de outbox'; 