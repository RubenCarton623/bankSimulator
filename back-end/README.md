# Bank Simulator - Prueba Técnica Sofka

API REST para simular operaciones bancarias con gestión de clientes, cuentas y movimientos.

## 📋 Requisitos Previos

- Java 17 o superior
- PostgreSQL 12 o superior
- Maven 3.6+

## 🚀 Tecnologías Utilizadas

- **Spring Boot 4.0.0**
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL** - Base de datos
- **Flyway** - Sistema de migraciones de base de datos
- **Lombok** - Reducción de código boilerplate

## 📦 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/rvera/sofka/banksimulator/
│   │   ├── controller/      # Controladores REST
│   │   ├── entity/          # Entidades JPA
│   │   ├── repository/      # Repositorios JPA
│   │   ├── service/         # Lógica de negocio
│   │   └── BanksimulatorApplication.java
│   └── resources/
│       ├── application.properties
│       └── db/migration/    # Scripts de migración Flyway
└── test/
```

## 🗄️ Modelo de Datos

### Entidades Principales

1. **Persona**: Datos básicos (nombre, género, edad, identificación, dirección, teléfono)
2. **Cliente**: Extiende Persona con clienteId, contraseña y estado
3. **Cuenta**: Número de cuenta, tipo, saldo inicial, estado
4. **Movimientos**: Fecha, tipo de movimiento, valor, saldo

## ⚙️ Configuración

### 1. Configurar Base de Datos

Asegúrate de tener PostgreSQL ejecutándose en `localhost:5432` con las siguientes credenciales:

```properties
Usuario: postgres
Contraseña: Lokoloko21
```

### 2. Configuración de Flyway

El proyecto utiliza **Flyway** para gestionar las migraciones de base de datos de forma automática y controlada.

#### ¿Qué es Flyway?

Flyway es una herramienta de migración de bases de datos que:
- Versiona los cambios en el esquema de la base de datos
- Ejecuta scripts SQL de forma ordenada y controlada
- Mantiene un historial de migraciones aplicadas
- Permite rollback y control de versiones del esquema

#### Ubicación de Scripts de Migración

Los scripts de migración se encuentran en:
```
src/main/resources/db/migration/
```

#### Nomenclatura de Scripts

Los archivos de migración deben seguir el patrón:
```
V{version}__{descripcion}.sql

Ejemplos:
- V1__init_database.sql
- V2__add_new_table.sql
- V3__modify_column.sql
```

**Reglas importantes:**
- `V` mayúscula al inicio
- Número de versión secuencial
- Doble guión bajo `__` separando versión y descripción
- Descripción en snake_case
- Extensión `.sql`

#### Script Inicial (V1__init_database.sql)

El script inicial incluye:
- **DROP DATABASE IF EXISTS**: Permite reutilizar el script eliminando la BD existente
- Creación de la base de datos `banksimulator`
- Creación de todas las tablas (persona, cliente, cuenta, movimientos)
- Índices para optimizar consultas
- Datos de ejemplo para testing

#### Ejecución de Migraciones

Las migraciones se ejecutan **automáticamente** al iniciar la aplicación:

```bash
mvn spring-boot:run
```

Flyway detectará automáticamente:
1. Scripts no ejecutados en `db/migration/`
2. Los ejecutará en orden secuencial
3. Registrará la ejecución en la tabla `flyway_schema_history`

#### Verificar Migraciones Aplicadas

Puedes consultar la tabla de historial de Flyway:

```sql
SELECT * FROM flyway_schema_history;
```

Esta tabla muestra:
- Versión de la migración
- Descripción
- Fecha de ejecución
- Estado (success/failed)
- Checksum del script

#### Configuración en application.properties

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

- `enabled=true`: Activa Flyway
- `baseline-on-migrate=true`: Permite migrar bases de datos existentes
- `locations`: Ubicación de los scripts de migración

### 3. Ejecutar el Script Inicial Manualmente (Opcional)

Si prefieres crear la base de datos manualmente antes de iniciar la aplicación:

```bash
# Conectar a PostgreSQL
psql -U postgres

# Ejecutar el script
\i src/main/resources/db/migration/V1__init_database.sql
```

## 🏃 Ejecutar la Aplicación

### Usando Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Usando Java

```bash
mvn clean package
java -jar target/banksimulator-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## 📡 Endpoints de la API

### Personas (`/api/personas`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/personas` | Listar todas las personas |
| GET | `/api/personas/{id}` | Obtener persona por ID |
| GET | `/api/personas/identificacion/{identificacion}` | Obtener persona por identificación |
| POST | `/api/personas` | Crear nueva persona |
| PUT | `/api/personas/{id}` | Actualizar persona completa |
| PATCH | `/api/personas/{id}` | Actualización parcial de persona |
| DELETE | `/api/personas/{id}` | Eliminar persona |

### Clientes (`/api/clientes`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/clientes` | Listar todos los clientes |
| GET | `/api/clientes/{id}` | Obtener cliente por ID |
| GET | `/api/clientes/identificacion/{identificacion}` | Obtener cliente por identificación |
| POST | `/api/clientes` | Crear nuevo cliente |
| PUT | `/api/clientes/{id}` | Actualizar cliente completo |
| PATCH | `/api/clientes/{id}` | Actualización parcial de cliente |
| DELETE | `/api/clientes/{id}` | Eliminar cliente |

### Cuentas (`/api/cuentas`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/cuentas` | Listar todas las cuentas |
| GET | `/api/cuentas/{id}` | Obtener cuenta por ID |
| GET | `/api/cuentas/numero/{numeroCuenta}` | Obtener cuenta por número |
| GET | `/api/cuentas/cliente/{clienteId}` | Obtener cuentas de un cliente |
| POST | `/api/cuentas` | Crear nueva cuenta |
| PUT | `/api/cuentas/{id}` | Actualizar cuenta completa |
| PATCH | `/api/cuentas/{id}` | Actualización parcial de cuenta |
| DELETE | `/api/cuentas/{id}` | Eliminar cuenta |

### Movimientos (`/api/movimientos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/movimientos` | Listar todos los movimientos |
| GET | `/api/movimientos/{id}` | Obtener movimiento por ID |
| GET | `/api/movimientos/cuenta/{cuentaId}` | Obtener movimientos de una cuenta |
| POST | `/api/movimientos` | Crear nuevo movimiento |
| PUT | `/api/movimientos/{id}` | Actualizar movimiento completo |
| PATCH | `/api/movimientos/{id}` | Actualización parcial de movimiento |
| DELETE | `/api/movimientos/{id}` | Eliminar movimiento |

## 📝 Ejemplos de Uso

### Crear un Cliente

```bash
POST http://localhost:8080/api/clientes
Content-Type: application/json

{
  "persona": {
    "nombre": "María García",
    "genero": "Femenino",
    "edad": 28,
    "identificacion": "9876543210",
    "direccion": "Av. Principal 456",
    "telefono": "3109876543"
  },
  "contrasena": "password456",
  "estado": true
}
```

### Crear una Cuenta

```bash
POST http://localhost:8080/api/cuentas
Content-Type: application/json

{
  "numeroCuenta": "123456",
  "tipoCuenta": "Corriente",
  "saldoInicial": 5000.00,
  "estado": true,
  "cliente": {
    "clienteId": 1
  }
}
```

### Registrar un Movimiento

```bash
POST http://localhost:8080/api/movimientos
Content-Type: application/json

{
  "tipoMovimiento": "Depósito",
  "valor": 1000.00,
  "saldo": 6000.00,
  "cuenta": {
    "id": 1
  }
}
```

## 🔍 Verificar el Estado de la Base de Datos

```sql
-- Ver historial de migraciones
SELECT * FROM flyway_schema_history;

-- Ver clientes registrados
SELECT c.cliente_id, p.nombre, p.identificacion, c.estado 
FROM cliente c 
INNER JOIN persona p ON c.persona_id = p.id;

-- Ver cuentas con sus clientes
SELECT cu.numero_cuenta, cu.tipo_cuenta, cu.saldo_inicial, p.nombre
FROM cuenta cu
INNER JOIN cliente cl ON cu.cliente_id = cl.cliente_id
INNER JOIN persona p ON cl.persona_id = p.id;

-- Ver movimientos de una cuenta
SELECT m.fecha, m.tipo_movimiento, m.valor, m.saldo
FROM movimientos m
WHERE m.cuenta_id = 1
ORDER BY m.fecha DESC;
```

## 🛠️ Solución de Problemas

### Error de Conexión a PostgreSQL

Verifica que PostgreSQL esté ejecutándose:
```bash
# Windows
pg_ctl status

# Linux/Mac
sudo systemctl status postgresql
```

### Flyway Checksum Mismatch

Si modificas un script ya ejecutado, Flyway fallará. Soluciones:

1. **Limpiar el historial de Flyway** (solo en desarrollo):
```sql
DROP TABLE flyway_schema_history;
```

2. **Crear un nuevo script de migración** en lugar de modificar uno existente

### La aplicación se cierra inmediatamente

Asegúrate de tener `spring-boot-starter-web` en el `pom.xml`

## 📚 Referencias

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/4.0.0/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

## 👨‍💻 Autor

Desarrollado para la prueba técnica de Sofka
