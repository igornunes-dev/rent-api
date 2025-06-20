CREATE TABLE contract (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    owner_id UUID NOT NULL,
    property_id UUID NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    monthly_value NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tenant FOREIGN KEY (tenant_id) REFERENCES tenant(id),
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES owner(id),
    CONSTRAINT fk_property FOREIGN KEY (property_id) REFERENCES property(id)
);

ALTER TABLE contract ADD CONSTRAINT chk_contract_status CHECK (status IN ('ACTIVE', 'EXPIRED', 'TERMINATED'));