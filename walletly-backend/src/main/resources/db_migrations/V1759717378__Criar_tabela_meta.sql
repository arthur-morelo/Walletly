CREATE TABLE geral.meta (
    id BIGSERIAL PRIMARY KEY,
    fk_usuario BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    valor_meta DECIMAL(15, 2) NOT NULL,
    valor_atual DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_meta_usuario FOREIGN KEY (fk_usuario) REFERENCES seguranca.usuario(id) ON DELETE CASCADE
);
