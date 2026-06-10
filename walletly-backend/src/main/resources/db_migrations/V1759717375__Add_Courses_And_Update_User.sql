-- Remover relacionamento antigo de roles
DROP TABLE IF EXISTS seguranca.usuario_roles;
DROP TABLE IF EXISTS seguranca.role;

-- Atualizar a tabela de usuários
ALTER TABLE seguranca.usuario ADD COLUMN role VARCHAR(255) NOT NULL DEFAULT 'FREE';
ALTER TABLE seguranca.usuario ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- Criar tabela de cursos
CREATE TABLE seguranca.course (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    is_premium BOOLEAN NOT NULL DEFAULT FALSE,
    creator_user_id BIGINT NOT NULL,
    CONSTRAINT fk_course_creator FOREIGN KEY (creator_user_id) REFERENCES seguranca.usuario(id)
);

-- Atualizar tabela de auditoria do usuário
ALTER TABLE auditoria.usuario_aud ADD COLUMN role VARCHAR(255);
ALTER TABLE auditoria.usuario_aud ADD COLUMN is_active BOOLEAN;

-- Tabela de auditoria para cursos (se necessário devido a @Audited)
CREATE TABLE auditoria.course_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    title VARCHAR(255),
    description TEXT,
    is_premium BOOLEAN,
    creator_user_id BIGINT,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_course_aud_rev FOREIGN KEY (rev) REFERENCES auditoria.revinfo(rev)
);
