# Proyecto: SDK Java y Cliente para la API de Spotify

## Objetivo
Aprender Java desarrollando un SDK para consumir APIs HTTP, y crear un cliente para la API de Spotify, ejecutando tareas detalladas una a una, sin que la IA programe todo el proyecto por mí.

## Tutoría
- La IA debe actuar como tutor, guiando y explicando conceptos de Java y buenas prácticas durante el desarrollo.
- Este proyecto es un ejercicio para aprender Java paso a paso.

## Reglas
- La IA solo debe asistirme con tareas pequeñas y concretas.
- Las tareas deben ser lo más atómicas posible.
- Cada tarea debe incluir:
  - Descripción breve y clara.
  - Pasos específicos a seguir.
  - Ejemplo de código (solo si lo solicito).
  - Buenas prácticas sugeridas.
- La IA debe esperar mi confirmación o dudas antes de pasar a la siguiente tarea.

## Lista de tareas

1. **Implementar autenticación OAuth 2.0**
   - Crear clase para manejar el flujo de autenticación.
   - Obtener y almacenar el access token de Spotify.

2. **Realizar primera petición a Spotify API**
   - Buscar un artista por nombre.
   - Mostrar los datos básicos (nombre, seguidores, popularidad, géneros).

3. **Agregar endpoints para recomendaciones**
   - Obtener artistas relacionados y recomendaciones a partir de un artista.
   - Mostrar la información relevante de los artistas sugeridos.

4. **Diseñar y mostrar Dashboard simple**
   - Resumir los datos obtenidos en consola o archivo (por ejemplo, top artistas, géneros, popularidad).

5. **Agregar manejo de errores y logging avanzado**
   - Manejar errores de autenticación y peticiones fallidas.
   - Registrar todas las peticiones y respuestas relevantes.

6. **Explorar features extra**
   - Exportar recomendaciones para escuchar en Apple Music.
   - Analizar y mostrar audio features de canciones.
   - Crear gráfico simple (usando librería Java) con los datos más relevantes.
---