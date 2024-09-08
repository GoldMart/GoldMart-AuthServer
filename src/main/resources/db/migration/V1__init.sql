CREATE TABLE IF NOT EXISTS member
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(50)       NOT NULL,
    password  VARCHAR(255)       NOT NULL
);