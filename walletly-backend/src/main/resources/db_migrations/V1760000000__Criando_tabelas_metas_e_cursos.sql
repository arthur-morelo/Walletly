CREATE TABLE geral.curso (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    text_left VARCHAR(255),
    text_right VARCHAR(255),
    course_description TEXT
);

CREATE TABLE geral.meta (
    id SERIAL PRIMARY KEY,
    fk_usuario BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    goal_value NUMERIC(15, 2) NOT NULL,
    saved_value NUMERIC(15, 2) DEFAULT 0,
    CONSTRAINT fk_meta_usuario FOREIGN KEY (fk_usuario) REFERENCES geral.usuarios (id) ON DELETE CASCADE
);

-- Envers Audit Tables
CREATE TABLE auditoria.curso_aud (
    id INTEGER NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    title VARCHAR(255),
    text_left VARCHAR(255),
    text_right VARCHAR(255),
    course_description TEXT,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_curso_aud_revinfo FOREIGN KEY (rev) REFERENCES auditoria.revinfo (rev)
);

CREATE TABLE auditoria.meta_aud (
    id INTEGER NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    fk_usuario BIGINT,
    name VARCHAR(255),
    goal_value NUMERIC(15, 2),
    saved_value NUMERIC(15, 2),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_meta_aud_revinfo FOREIGN KEY (rev) REFERENCES auditoria.revinfo (rev)
);
