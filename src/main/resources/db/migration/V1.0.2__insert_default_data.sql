INSERT INTO roles (name, description) VALUES
                                          ('ADMIN', 'System administrator with full access to all functionalities'),
                                          ('OWNER', 'Restaurant owner who can manage their restaurant and employees'),
                                          ('EMPLOYEE', 'Restaurant employee who handles orders'),
                                          ('CLIENT', 'Regular customer who can place orders')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO users (
    first_name,
    last_name,
    document_id,
    phone_number,
    birth_date,
    email,
    password
) VALUES (
             'System',
             'Administrator',
             '123456789',
             '+573001234567',
             '1990-01-01',
             'admin@plazacomidas.com',
             '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2' -- Admin123!
         ) ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'admin@plazacomidas.com'
  AND r.name = 'ADMIN'
    ON CONFLICT DO NOTHING;