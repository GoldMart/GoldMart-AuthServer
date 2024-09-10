CREATE TABLE IF NOT EXISTS member
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(50) UNIQUE NOT NULL,
    password  VARCHAR(255)       NOT NULL
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    token       VARCHAR(1024)     NOT NULL,
    member_id   BIGINT            NOT NULL,
    CONSTRAINT fk_member_refresh_token FOREIGN KEY (member_id) REFERENCES member(id)
);