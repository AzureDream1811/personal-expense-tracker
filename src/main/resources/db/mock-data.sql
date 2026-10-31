-- =============================================
-- 1. USERS
-- =============================================
-- Mock users
-- Passwords are BCrypt hashes of the development password: password123
INSERT INTO users (username, email, password, created_at)
VALUES (
        'john_doe',
        'john@example.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        CURRENT_TIMESTAMP
    ),
    (
        'jane_doe',
        'jane@example.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        CURRENT_TIMESTAMP
    ),
    (
        'alex_smith',
        'alex@example.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        CURRENT_TIMESTAMP
    );
-- =============================================
-- 2. CATEGORIES
-- =============================================
INSERT INTO categories (
        name,
        description,
        created_at,
        deleted_at,
        user_id
    )
SELECT c.name,
    c.description,
    CURRENT_TIMESTAMP,
    NULL,
    u.id
FROM users u
    CROSS JOIN (
        VALUES ('Food', 'Food and groceries'),
            ('Transportation', 'Travel and transportation'),
            ('Shopping', 'Personal and household purchases'),
            ('Entertainment', 'Movies, games and leisure'),
            ('Bills', 'Utilities and recurring bills')
    ) AS c(name, description)
WHERE u.username IN ('alice', 'bob', 'charlie', 'david', 'emma');
-- =============================================
-- 3. EXPENSES
-- =============================================
WITH expense_data AS (
    SELECT u.id AS user_id,
        n AS expense_number,
        ((n - 1) % 5) + 1 AS category_number
    FROM users u
        CROSS JOIN generate_series(1, 40) AS gs(n)
    WHERE u.username IN ('alice', 'bob', 'charlie', 'david', 'emma')
)
INSERT INTO expenses (
        amount,
        description,
        expense_date,
        created_at,
        updated_at,
        deleted_at,
        user_id,
        category_id
    )
SELECT (10000 + (e.expense_number * 1379) % 490000)::NUMERIC(12, 2),
    CASE
        e.category_number
        WHEN 1 THEN 'Food expense'
        WHEN 2 THEN 'Transportation expense'
        WHEN 3 THEN 'Shopping expense'
        WHEN 4 THEN 'Entertainment expense'
        ELSE 'Monthly bill'
    END,
    DATE '2026-01-01' + ((e.expense_number * 7) % 303),
    CURRENT_TIMESTAMP,
    NULL,
    NULL,
    e.user_id,
    c.id
FROM expense_data e
    JOIN categories c ON c.user_id = e.user_id
    AND c.name = CASE
        e.category_number
        WHEN 1 THEN 'Food'
        WHEN 2 THEN 'Transportation'
        WHEN 3 THEN 'Shopping'
        WHEN 4 THEN 'Entertainment'
        ELSE 'Bills'
    END;