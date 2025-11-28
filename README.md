# 📦 Proyecto Full Stack

Este proyecto contiene una aplicación **Full Stack** compuesta por un backend, frontend y base de datos, todo orquestado mediante Docker Compose.

---

## Estructura del proyecto

```bash
├── back-end
├── front-end
├── DB
├── .gitignore
├── docker-compose.yml
└── README.md
```
---

## Requisitos previos

Antes de ejecutar el proyecto asegúrate de tener instalado:

- Docker  
- Docker Compose  
- Navegador Web  

---

## Ejecución del proyecto

Para levantar todo el sistema ejecuta el siguiente comando en la raíz del proyecto:

docker-compose up --build

Este comando levantará automáticamente:

- Backend
- Base de Datos
- Servicios asociados
- Frontend

---

## Accesos del sistema

### Frontend (Aplicación Web)
Accede desde tu navegador en:
http://localhost:3000
---

### Backend (API REST)
La API estará disponible en:
http://localhost:8080

---

### Swagger (Documentación de la API)
Puedes consultar los endpoints en:

http://localhost:8080/api/v1/swagger-ui/index.html

