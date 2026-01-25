-- Add slug column to raffles with unique constraint
ALTER TABLE raffles ADD COLUMN slug VARCHAR(255);

-- Backfill existing raffles with a generated slug (title + random suffix)
UPDATE raffles SET slug =
    LOWER(REGEXP_REPLACE(COALESCE(title, 'raffle'), '[^a-z0-9]+', '-', 'g')) || '-' || SUBSTRING(MD5(RANDOM()::text), 1, 8)
WHERE slug IS NULL;

-- Enforce not null and unique
ALTER TABLE raffles ALTER COLUMN slug SET NOT NULL;
ALTER TABLE raffles ADD CONSTRAINT uk_raffles_slug UNIQUE (slug);

