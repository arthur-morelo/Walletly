CREATE TABLE IF NOT EXISTS geral.extrato_history (
    id BIGSERIAL PRIMARY KEY,
    fk_usuario BIGINT NOT NULL,
    filename VARCHAR(255) NOT NULL,
    upload_date TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL,
    log_message TEXT,
    FOREIGN KEY (fk_usuario) REFERENCES seguranca.usuario(id)
);

ALTER TABLE geral.transacao ADD COLUMN fk_extrato_history BIGINT;
ALTER TABLE geral.transacao ADD CONSTRAINT fk_transacao_extrato_history FOREIGN KEY (fk_extrato_history) REFERENCES geral.extrato_history(id) ON DELETE CASCADE;

ALTER TABLE auditoria.transacao_aud ADD COLUMN fk_extrato_history BIGINT;
