DROP DATABASE IF EXISTS noobit_db;

CREATE DATABASE noobit_db;
USE noobit_db;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255) NOT NULL,
    puntos_ranked INT DEFAULT 0,    -- Puntos de clasificación (0-1000)
    puntos_market INT DEFAULT 0,    -- Moneda para comprar skins
    es_admin BOOLEAN DEFAULT FALSE  -- TRUE = Admin, FALSE = Jugador Normal
);

CREATE TABLE recompensas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio INT NOT NULL
);

CREATE TABLE inventarios (
    usuario_id INT NOT NULL,
    recompensa_id INT NOT NULL,
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, recompensa_id),
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_recompensa FOREIGN KEY (recompensa_id) REFERENCES recompensas(id) ON DELETE CASCADE
);
-- A) CREAR EL SUPER-ADMINISTRADOR
-- Usuario: admin
-- Contraseña: 123
INSERT INTO usuarios (username, email, password, puntos_ranked, puntos_market, es_admin)
VALUES ('admin', 'admin@noobit.com', '123', 1000, 99999, TRUE);

-- B) CREAR UN JUGADOR NORMAL DE EJEMPLO
-- Usuario: player
-- Contraseña: 123
INSERT INTO usuarios (username, email, password, puntos_ranked, puntos_market, es_admin)
VALUES ('player', 'player@noobit.com', '123', 500, 100, FALSE);

INSERT INTO recompensas (nombre, descripcion, precio) VALUES
('Skin Dragón Neón', 'Skin legendaria que brilla en la oscuridad', 1000),
('Marco Dorado', 'Marco exclusivo para tu foto de perfil', 500),
('Boost XP x2', 'Duplica tus puntos en la siguiente partida', 200),
('Icono Noobit Pro', 'Insignia para veteranos', 150),
('Color de Chat Cian', 'Escribe en color cian en el chat global', 300);

SELECT * FROM usuarios;
