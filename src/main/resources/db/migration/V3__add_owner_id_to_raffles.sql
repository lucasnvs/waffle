-- Add owner_id column to raffles table
ALTER TABLE raffles ADD COLUMN owner_id VARCHAR(255);

-- Set a default owner_id for existing raffles (can be updated later)
UPDATE raffles SET owner_id = 'system' WHERE owner_id IS NULL;

-- Make owner_id NOT NULL after setting defaults
ALTER TABLE raffles ALTER COLUMN owner_id SET NOT NULL;

