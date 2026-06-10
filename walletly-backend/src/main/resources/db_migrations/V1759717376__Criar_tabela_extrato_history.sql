CREATE TABLE if not exists extrato_history (
    id BIGSERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    upload_date TIMESTAMP NOT NULL,
    status VARCHAR(255) NOT NULL,
    log_message TEXT,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES seguranca.usuario(id)
);

CREATE TABLE if not exists auditoria.extrato_history_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    filename VARCHAR(255),
    upload_date TIMESTAMP,
    status VARCHAR(255),
    log_message TEXT,
    user_id BIGINT,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES auditoria.revinfo(rev)
);
