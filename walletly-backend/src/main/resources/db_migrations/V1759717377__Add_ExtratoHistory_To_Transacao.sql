ALTER TABLE geral.transacao ADD COLUMN fk_extrato_history BIGINT;
ALTER TABLE geral.transacao ADD CONSTRAINT fk_transacao_extrato FOREIGN KEY (fk_extrato_history) REFERENCES extrato_history(id) ON DELETE CASCADE;
