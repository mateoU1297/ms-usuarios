CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE usuarios (
                          id SERIAL PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          apellido VARCHAR(100) NOT NULL,
                          email VARCHAR(150) UNIQUE NOT NULL,
                          telefono VARCHAR(20) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          activo BOOLEAN DEFAULT TRUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE usuario_roles (
                               usuario_id INT REFERENCES usuarios(id) ON DELETE CASCADE,
                               rol_id INT REFERENCES roles(id),
                               PRIMARY KEY (usuario_id, rol_id)
);

INSERT INTO roles(name) VALUES ('ADMIN');
INSERT INTO roles(name) VALUES ('OWNER');
INSERT INTO roles(name) VALUES ('EMPLOYEE');
INSERT INTO roles(name) VALUES ('CLIENT');