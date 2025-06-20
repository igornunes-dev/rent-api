CREATE TABLE property (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(500) NOT NULL,
    price NUMERIC(12,2) NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'AVAILABLE',
    owner_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES owner(id)
);

ALTER TABLE property ADD CONSTRAINT chk_property_status CHECK (status IN ('AVAILABLE', 'RENTED', 'UNAVAILABLE'));