ALTER TABLE users ADD COLUMN phone VARCHAR(32);
ALTER TABLE users ADD COLUMN dni VARCHAR(32);

UPDATE users
SET name = LEFT(name, 6),
    phone = '0000000000',
    dni = '23454234W',
    role = 'admin';

ALTER TABLE users ALTER COLUMN email TYPE VARCHAR(254);
ALTER TABLE users ALTER COLUMN phone SET NOT NULL;
ALTER TABLE users ALTER COLUMN dni SET NOT NULL;
ALTER TABLE users ADD CONSTRAINT chk_users_role CHECK (role IN ('admin', 'superadmin'));

CREATE INDEX idx_users_room_id ON users(room_id);
