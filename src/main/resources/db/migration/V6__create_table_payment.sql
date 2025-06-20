CREATE TABLE payment (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    contract_id UUID NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    due_date DATE NOT NULL,
    payment_date DATE,
    status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_contract FOREIGN KEY (contract_id) REFERENCES contract(id)
);

ALTER TABLE payment ADD CONSTRAINT chk_payment_status CHECK (status IN ('PENDING', 'PAID', 'OVERDUE'));