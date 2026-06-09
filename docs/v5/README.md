# MVP 5 — Primer jefe (JefeBarco)

**Agrega sobre MVP 4:** concepto de "nivel" — completar N oleadas desencadena un boss. Primer boss: JefeBarco.

## Clases nuevas
- `Jefe` (abstract, extiende `Enemigo`) — fases, timer de ataque, `ejecutarAtaqueEspecial()`
- `JefeBarco` — ataque especial: lanza EnemigoKamikaze (nuevo tipo de enemigo)
- `EnemigoKamikaze` — se activa cuando detecta al Mirage debajo de umbralY
- `EntidadFactory` — centraliza creación de enemigos y bosses
- `TipoEnemigo` (enum), `TipoJefe` (enum)
- `EstadoNivelCompletado` — pantalla de transición entre niveles
- `PantallaBoss` — muestra entrada del boss

## Clases modificadas
- `NivelMirage` — secuencia oleadas → boss → nuevo nivel
- `GameRenderer` — dibuja boss bar
- `ColisionDetector` — Jefe tratado como Enemigo (ya funciona por herencia)

## Casos de uso nuevos
- Jefe aparece en pantalla
- JefeBarco lanza kamikazes
- Kamikaze carga contra el Mirage
- Escalar dificultad al subir nivel

*(documentar al desarrollar esta versión)*