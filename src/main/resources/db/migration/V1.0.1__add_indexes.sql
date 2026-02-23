CREATE INDEX idx_usuario_email ON usuarios(email);
CREATE INDEX idx_usuario_telefono ON usuarios(telefono);
CREATE INDEX idx_usuario_roles_usuario ON usuario_roles(usuario_id);