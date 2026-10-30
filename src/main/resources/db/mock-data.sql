-- Mock data for Expense Tracker (PostgreSQL)
-- Development/testing only. Run against an EMPTY database.
-- Explicit IDs are used, so do not run this against existing application data.

BEGIN;

INSERT INTO users (id, username, email, created_at, deleted_at) VALUES
(1, 'alice',   'alice@example.com',   '2026-01-01 08:00:00', NULL),
(2, 'bob',     'bob@example.com',     '2026-01-02 08:00:00', NULL),
(3, 'charlie', 'charlie@example.com', '2026-01-03 08:00:00', NULL),
(4, 'diana',   'diana@example.com',   '2026-01-04 08:00:00', NULL),
(5, 'ethan',   'ethan@example.com',   '2026-01-05 08:00:00', NULL);

INSERT INTO categories (id, name, description, created_at, deleted_at) VALUES
(1,  'Food',          'Meals, groceries, and drinks',            '2026-01-01 09:00:00', NULL),
(2,  'Transportation','Fuel, public transport, and ride-hailing','2026-01-01 09:00:00', NULL),
(3,  'Shopping',      'Clothing and personal purchases',         '2026-01-01 09:00:00', NULL),
(4,  'Education',     'Books, courses, and learning materials', '2026-01-01 09:00:00', NULL),
(5,  'Entertainment', 'Movies, games, and leisure activities',  '2026-01-01 09:00:00', NULL),
(6,  'Healthcare',    'Medicine and healthcare services',       '2026-01-01 09:00:00', NULL),
(7,  'Utilities',     'Electricity, water, and internet',        '2026-01-01 09:00:00', NULL),
(8,  'Housing',       'Rent and household expenses',             '2026-01-01 09:00:00', NULL),
(9,  'Travel',        'Trips and accommodation',                 '2026-01-01 09:00:00', NULL),
(10, 'Gifts',         'Gifts and donations',                      '2026-01-01 09:00:00', '2026-10-15 12:00:00');

-- Generate 200 deterministic expenses across 5 users and 10 categories.
-- Four expenses are soft-deleted. Some historical expenses still reference
-- category 10, which is soft-deleted but must remain available for history.
INSERT INTO expenses
    (id, amount, description, expense_date, created_at, updated_at, deleted_at, user_id, category_id)
SELECT
    n,
    (10000 + ((n * 7919) % 490000))::numeric(12,2),
    CASE (n % 8)
        WHEN 0 THEN 'Lunch with friends'
        WHEN 1 THEN 'Monthly grocery shopping'
        WHEN 2 THEN 'Bus and metro fare'
        WHEN 3 THEN 'Online purchase'
        WHEN 4 THEN 'Course or learning material'
        WHEN 5 THEN 'Coffee and snacks'
        WHEN 6 THEN 'Household payment'
        ELSE 'Personal expense'
    END,
    DATE '2026-01-01' + ((n * 37) % 303)::integer,
    TIMESTAMP '2026-01-01 10:00:00' + (n * INTERVAL '2 hours'),
    TIMESTAMP '2026-01-01 10:00:00' + (n * INTERVAL '2 hours'),
    CASE
        WHEN n IN (47, 94, 141, 188)
            THEN TIMESTAMP '2026-10-20 12:00:00'
        ELSE NULL
    END,
    ((n - 1) % 5) + 1,
    ((n - 1) % 10) + 1
FROM generate_series(1, 200) AS series(n);

-- Synchronize identity/serial sequences after inserting explicit IDs.
SELECT setval(
    pg_get_serial_sequence('users', 'id'),
    COALESCE((SELECT MAX(id) FROM users), 1),
    true
);

SELECT setval(
    pg_get_serial_sequence('categories', 'id'),
    COALESCE((SELECT MAX(id) FROM categories), 1),
    true
);

SELECT setval(
    pg_get_serial_sequence('expenses', 'id'),
    COALESCE((SELECT MAX(id) FROM expenses), 1),
    true
);

COMMIT;
