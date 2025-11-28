-- ====================================
-- Tabla: Persona
-- ====================================
CREATE TABLE IF NOT EXISTS persona (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    genero VARCHAR(50),
    edad INTEGER,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    direccion VARCHAR(500),
    telefono VARCHAR(50)
);

-- ====================================
-- Tabla: Cliente (Hereda de Persona)
-- ====================================
CREATE TABLE IF NOT EXISTS cliente (
    cliente_id BIGSERIAL PRIMARY KEY,
    persona_id BIGINT NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT fk_cliente_persona FOREIGN KEY (persona_id) 
        REFERENCES persona(id) 
        ON DELETE CASCADE
);

-- ====================================
-- Tabla: Cuenta
-- ====================================
CREATE TABLE IF NOT EXISTS cuenta (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(50) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(15,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT true,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_cuenta_cliente FOREIGN KEY (cliente_id) 
        REFERENCES cliente(cliente_id) 
        ON DELETE CASCADE
);

-- ====================================
-- Tabla: Movimientos
-- ====================================
CREATE TABLE IF NOT EXISTS movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT true,
    cuenta_id BIGINT NOT NULL,
    CONSTRAINT fk_movimientos_cuenta FOREIGN KEY (cuenta_id) 
        REFERENCES cuenta(id) 
        ON DELETE CASCADE
);

-- ====================================
-- Índices para mejorar rendimiento
-- ====================================
CREATE INDEX idx_cliente_persona ON cliente(persona_id);
CREATE INDEX idx_cuenta_cliente ON cuenta(cliente_id);
CREATE INDEX idx_movimientos_cuenta ON movimientos(cuenta_id);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha);

-- ====================================
-- Datos de ejemplo
-- ====================================
-- Insertar persona de ejemplo
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) 
VALUES ('Juan Pérez', 'Masculino', 30, '1234567890', 'Calle 123 #45-67', '3001234567');

-- Insertar cliente de ejemplo
INSERT INTO cliente (persona_id, contrasena, estado) 
VALUES (1, 'password123', true);
