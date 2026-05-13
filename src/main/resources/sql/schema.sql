-- =====================
-- SEQUENCES
-- =====================
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE workspace_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE workspace_member_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE task_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE notification_seq START WITH 1 INCREMENT BY 1;

-- =====================
-- TABLES
-- =====================
CREATE TABLE users (
    id         NUMBER PRIMARY KEY,
    email      VARCHAR2(100) NOT NULL UNIQUE,
    password   VARCHAR2(255) NOT NULL,
    name       VARCHAR2(50)  NOT NULL,
    created_at DATE DEFAULT SYSDATE
);

CREATE TABLE workspace (
    id         NUMBER PRIMARY KEY,
    name       VARCHAR2(100) NOT NULL,
    created_by NUMBER        NOT NULL,
    created_at DATE DEFAULT SYSDATE,
    CONSTRAINT fk_workspace_user FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE workspace_member (
    id           NUMBER PRIMARY KEY,
    workspace_id NUMBER       NOT NULL,
    user_id      NUMBER       NOT NULL,
    role         VARCHAR2(20) NOT NULL,
    CONSTRAINT fk_wm_workspace FOREIGN KEY (workspace_id) REFERENCES workspace (id),
    CONSTRAINT fk_wm_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE task (
    id           NUMBER PRIMARY KEY,
    title        VARCHAR2(255) NOT NULL,
    content      CLOB,
    status       VARCHAR2(20)  NOT NULL,
    priority     VARCHAR2(20)  NOT NULL,
    category     VARCHAR2(100),
    due_date     DATE,
    workspace_id NUMBER        NOT NULL,
    assignee_id  NUMBER,
    created_by   NUMBER        NOT NULL,
    created_at   DATE DEFAULT SYSDATE,
    CONSTRAINT fk_task_workspace FOREIGN KEY (workspace_id) REFERENCES workspace (id),
    CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES users (id),
    CONSTRAINT fk_task_creator FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE notification (
    id         NUMBER PRIMARY KEY,
    user_id    NUMBER        NOT NULL,
    title      VARCHAR2(255) NOT NULL,
    message    CLOB,
    is_read    NUMBER(1) DEFAULT 0,
    created_at DATE DEFAULT SYSDATE,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users (id)
);