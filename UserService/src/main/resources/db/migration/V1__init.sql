CREATE TABLE auth_user
(
    id       BINARY(16)   NOT NULL,
    name     VARCHAR(255) NULL,
    email    VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    CONSTRAINT pk_auth_user PRIMARY KEY (id)
);

CREATE TABLE auth_user_roles
(
    auth_user_id BINARY(16) NOT NULL,
    roles_id     BINARY(16) NOT NULL,
    CONSTRAINT pk_auth_user_roles PRIMARY KEY (auth_user_id, roles_id)
);

CREATE TABLE `role`
(
    id   BINARY(16)   NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE session
(
    id             BINARY(16)   NOT NULL,
    token          VARCHAR(255) NULL,
    issued_at      datetime NULL,
    last_login_at  datetime NULL,
    expiring_at    datetime NULL,
    user_id        BINARY(16)   NULL,
    session_status ENUM('ACTIVE', 'ENDED') NULL,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

ALTER TABLE session
    ADD CONSTRAINT FK_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES auth_user (id);

ALTER TABLE auth_user_roles
    ADD CONSTRAINT fk_autuserol_on_role FOREIGN KEY (roles_id) REFERENCES `role` (id);

ALTER TABLE auth_user_roles
    ADD CONSTRAINT fk_autuserol_on_user FOREIGN KEY (auth_user_id) REFERENCES auth_user (id);