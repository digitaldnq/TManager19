CREATE TABLE IF NOT EXISTS categories (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS tasks (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     description TEXT,
                                     status VARCHAR(50) NOT NULL,
                                     scheduled_time BIGINT NOT NULL,
                                     retry_count INT DEFAULT 0,
                                     max_retries INT DEFAULT 3,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     error_message TEXT,
                                     category_id BIGINT NOT NULL,
                                     FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS workers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    worker_name VARCHAR(255) NOT NULL,
    status ENUM('QUEUED', 'IN_PROGRESS', 'COMPLETED', 'FAILED', 'CANCELLED') NOT NULL,
    active_tasks INT DEFAULT 0,
    last_heartbeat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    category_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);