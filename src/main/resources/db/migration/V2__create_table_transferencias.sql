CREATE TABLE IF NOT EXISTS transferencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    origem VARCHAR(100) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    valor BIGINT NOT NULL,
    criado_em DATETIME(6) NOT NULL,
    INDEX idx_dup_check (origem, destino, valor, criado_em)
);
