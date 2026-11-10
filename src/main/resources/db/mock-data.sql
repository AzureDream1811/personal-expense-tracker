
-- =========================================
-- PERSONAL EXPENSE TRACKER - MOCK DATA
-- PostgreSQL
-- =========================================

-- Enable PostgreSQL cryptographic functions
CREATE EXTENSION IF NOT EXISTS pgcrypto;


-- =========================================
-- 1. USERS
-- All demo accounts use password: DemoPass123!
-- Password is hashed using BCrypt.
-- =========================================

INSERT INTO users (
    username,
    password,
    email,
    created_at
)
VALUES
(
    'demo_alex',
    crypt('DemoPass123!', gen_salt('bf', 10)),
    'alex.demo@example.com',
    CURRENT_TIMESTAMP
),
(
    'demo_mia',
    crypt('DemoPass123!', gen_salt('bf', 10)),
    'mia.demo@example.com',
    CURRENT_TIMESTAMP
),
(
    'demo_noah',
    crypt('DemoPass123!', gen_salt('bf', 10)),
    'noah.demo@example.com',
    CURRENT_TIMESTAMP
)
ON CONFLICT DO NOTHING;


-- =========================================
-- 2. CATEGORIES
-- =========================================

INSERT INTO categories (
    name,
    description,
    created_at,
    user_id
)
SELECT
    c.name,
    c.description,
    CURRENT_TIMESTAMP,
    u.id
FROM (
    VALUES
    ('demo_alex', 'Food', 'Food and dining'),
    ('demo_alex', 'Transportation', 'Travel and commuting'),
    ('demo_alex', 'Shopping', 'Personal shopping'),
    ('demo_alex', 'Entertainment', 'Movies and leisure'),

    ('demo_mia', 'Food', 'Food and dining'),
    ('demo_mia', 'Transportation', 'Travel and commuting'),
    ('demo_mia', 'Healthcare', 'Medical and healthcare'),
    ('demo_mia', 'Education', 'Courses and learning'),

    ('demo_noah', 'Food', 'Food and dining'),
    ('demo_noah', 'Transportation', 'Travel and commuting'),
    ('demo_noah', 'Shopping', 'Personal shopping'),
    ('demo_noah', 'Bills', 'Monthly bills and utilities')
) AS c(username, name, description)
JOIN users u ON u.username = c.username
WHERE NOT EXISTS (
    SELECT 1
    FROM categories existing
    WHERE existing.user_id = u.id
      AND existing.name = c.name
      AND existing.deleted_at IS NULL
);


-- =========================================
-- 3. EXPENSES
-- =========================================

INSERT INTO expenses (
    amount,
    description,
    expense_date,
    created_at,
    updated_at,
    user_id,
    category_id
)
SELECT
    e.amount,
    e.description,
    CURRENT_DATE - e.days_ago,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    u.id,
    c.id
FROM (
    VALUES
    -- ALEX
    ('demo_alex', 'Food', 85.00, 'Lunch at restaurant', 1),
    ('demo_alex', 'Food', 45.00, 'Coffee and breakfast', 2),
    ('demo_alex', 'Transportation', 30.00, 'Grab ride', 3),
    ('demo_alex', 'Shopping', 550.00, 'New backpack', 5),
    ('demo_alex', 'Entertainment', 120.00, 'Movie tickets', 7),
    ('demo_alex', 'Food', 150.00, 'Dinner with friends', 10),

    -- MIA
    ('demo_mia', 'Food', 65.00, 'Breakfast', 1),
    ('demo_mia', 'Transportation', 25.00, 'Bus and taxi', 2),
    ('demo_mia', 'Healthcare', 350.00, 'Medicine', 4),
    ('demo_mia', 'Education', 1200.00, 'Online programming course', 6),
    ('demo_mia', 'Food', 95.00, 'Lunch with colleagues', 8),
    ('demo_mia', 'Transportation', 40.00, 'Grab ride', 11),

    -- NOAH
    ('demo_noah', 'Food', 110.00, 'Dinner', 1),
    ('demo_noah', 'Transportation', 35.00, 'Motorbike fuel', 3),
    ('demo_noah', 'Shopping', 890.00, 'New shoes', 5),
    ('demo_noah', 'Bills', 450.00, 'Internet bill', 7),
    ('demo_noah', 'Food', 75.00, 'Coffee and snacks', 9),
    ('demo_noah', 'Bills', 1200.00, 'Electricity bill', 12)

) AS e(username, category_name, amount, description, days_ago)

JOIN users u
    ON u.username = e.username

JOIN categories c
    ON c.user_id = u.id
    AND c.name = e.category_name
    AND c.deleted_at IS NULL

WHERE NOT EXISTS (
    SELECT 1
    FROM expenses existing
    WHERE existing.user_id = u.id
      AND existing.description = e.description
      AND existing.expense_date = CURRENT_DATE - e.days_ago
      AND existing.deleted_at IS NULL
);


-- =========================================
-- 4. RESET ID SEQUENCES
-- =========================================

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
