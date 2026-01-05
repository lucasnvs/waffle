-- Create raffles table
CREATE TABLE IF NOT EXISTS raffles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    total_tickets INT NOT NULL,
    ticket_price DOUBLE PRECISION NOT NULL,
    description TEXT,
    has_draw_date BOOLEAN NOT NULL DEFAULT FALSE,
    draw_date TIMESTAMP,
    draw_time TIME,
    cover_image VARCHAR(500),
    contact_phone_number VARCHAR(50),
    is_public BOOLEAN NOT NULL DEFAULT TRUE,
    show_winner_publicly BOOLEAN NOT NULL DEFAULT TRUE,
    draw_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create raffle payment methods table
CREATE TABLE IF NOT EXISTS raffle_payment_methods (
    raffle_id BIGINT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    PRIMARY KEY (raffle_id, payment_method),
    FOREIGN KEY (raffle_id) REFERENCES raffles(id) ON DELETE CASCADE
);

-- Create tickets table
CREATE TABLE IF NOT EXISTS tickets (
    id BIGSERIAL PRIMARY KEY,
    raffle_id BIGINT NOT NULL,
    number INT NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'RESERVED',
    FOREIGN KEY (raffle_id) REFERENCES raffles(id) ON DELETE CASCADE,
    UNIQUE (raffle_id, number)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_raffles_status ON raffles(status);
CREATE INDEX IF NOT EXISTS idx_tickets_raffle_id ON tickets(raffle_id);
CREATE INDEX IF NOT EXISTS idx_tickets_user_id ON tickets(user_id);
CREATE INDEX IF NOT EXISTS idx_tickets_status ON tickets(status);

