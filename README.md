# Sistema Web de Gestión de Citas Médicas

Este proyecto es una implementación en **Java** de un sistema web para la **gestión de citas médicas en línea**, permitiendo a pacientes agendar citas con médicos especialistas, y a los médicos gestionar su agenda. Toda la información se almacena en una base de datos **MySQL**.

El sistema sigue una **arquitectura en tres capas** (Presentación, Lógica y Datos) y fue desarrollado utilizando el framework **Spring MVC** junto con el motor de plantillas **Thymeleaf**, siguiendo el patrón **Modelo-Vista-Controlador (MVC)**. El control de acceso está basado en sesiones de usuario en el servidor.

---

## Integrantes del Proyecto

- Sebastián Álvarez Gómez  
- Esteban Sánchez Sánchez  
- Anthony Li Perera  

---

## Funcionalidades Principales

### Autenticación y Registro
- Registro e inicio de sesión para pacientes y médicos.
- Identificación del usuario por rol (paciente, médico, administrador).
- Control de acceso mediante sesiones.
- Enlace al registro desde la página de login.

### Gestión de Médicos
- Registro y configuración del perfil (especialidad, localidad, horario, reseña, etc.).
- Aprobación del registro por parte del administrador.
- Visualización y filtrado de citas por estado o paciente.
- Confirmación y finalización de citas con anotaciones.

### Gestión de Pacientes
- Búsqueda de médicos por especialidad y ubicación.
- Visualización de horarios disponibles (3 días próximos y extendidos).
- Reserva y confirmación de citas con autenticación previa.
- Acceso al historial de citas con filtros.

### Gestión del Administrador
- Listado de médicos registrados.
- Aprobación de nuevos registros de médicos.

---



