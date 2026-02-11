# 🎮 NoobIt - Gestión de Usuarios & Marketplace

![Java](https://img.shields.io/badge/Java-17-orange)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)

---

## 📌 Descripción

NoobIt es una aplicación de escritorio desarrollada en Java con una interfaz inspirada en el mundo gaming.  
El sistema permite gestionar usuarios, simular partidas competitivas con sistema de puntos (Ranked Pts) y utilizar un marketplace donde los jugadores pueden comprar recompensas con monedas virtuales.

Este proyecto aplica conceptos como programación orientada a objetos, patrón DAO, conexión con base de datos MySQL y desarrollo de interfaces gráficas con Swing, diferenciando claramente entre roles de Administrador y Jugador.

---

## ✨ Características Principales

### 🔐 Sistema de Autenticación
- Login personalizado con ventana sin bordes.
- Registro de nuevos usuarios.
- Sistema de roles:
  - 👑 Administrador
  - 🎮 Jugador

### 🛠️ Panel de Administración (VistaAdmin)
- CRUD completo de usuarios.
- Modificación manual de puntos Ranked y monedas.
- Gestión de recompensas del marketplace.
- Eliminación de usuarios.

### 🕹️ Experiencia del Jugador (VistaJugador)
- Simulador de partidas (gana o pierde puntos aleatoriamente).
- Sistema automático de rangos (de LVL 1 hasta Pro Player).
- Marketplace para compra de objetos.
- Inventario personal.

---

## 🛠️ Tecnologías Utilizadas

- Lenguaje: Java 17+
- Interfaz gráfica: Java Swing & AWT
- Base de datos: MySQL
- Persistencia: Patrón DAO (UsuarioDAO)
- Diseño: Estética gaming con colores neón y ventanas personalizadas

---

## 🗄️ Base de Datos

Base de datos: `noobit_db`

```sql
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
```

---

## 📂 Estructura del Proyecto

```
src/
 ├── model/
 │    └── Usuario.java
 │
 ├── dao/
 │    └── UsuarioDAO.java
 │
 ├── view/
 │    ├── VistaLogin.java
 │    ├── VistaAdmin.java
 │    └── VistaJugador.java
 │
 └── AppNoobit.java
```

---

## 🚀 Instalación y Ejecución

1. Clonar el repositorio:

```bash
git clone https://github.com/tu-usuario/NoobItAED.git
```

2. Crear la base de datos ejecutando el script SQL anterior.

3. Configurar la conexión en `UsuarioDAO`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/noobit_db";
private static final String USER = "root";
private static final String PASSWORD = "tu_password";
```

4. Ejecutar la clase principal:

```java
AppNoobit.java
```

---

## 🎨 Detalle Técnico

La aplicación utiliza ventanas sin barra de título para dar un estilo más inmersivo:

```java
frame.setUndecorated(true);
frame.setResizable(false);
frame.setLocationRelativeTo(null);
```

---

## 👤 Autores

Pablo Caballero  
Néstor Larrea
