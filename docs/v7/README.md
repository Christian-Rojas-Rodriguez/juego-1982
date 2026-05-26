# MVP 7 — N jefes (versión completa)

**Agrega sobre MVP 6:** los 4 bosses completos con patrones de ataque únicos. Secuencia completa de niveles.

## Clases nuevas
- `JefeAvionGigante` — dispara abanico de proyectiles que cubre la pantalla
- `JefeCuadrilla` — despliega 6 HarrierEnemigo en formación V
- `JefeCazador` — se alinea al X del Mirage y zambulle verticalmente

## Secuencia de bosses por nivel
1. JefeBarco
2. JefeAvionGigante
3. JefeCuadrilla
4. JefeCazador

Al vencer los 4 → `EstadoNivelCompletado` → `ConfiguradorDificultad.escalarParaNivel(n+1)` → nuevo nivel.

## Casos de uso nuevos
- JefeAvionGigante ataque de área
- JefeCuadrilla despliega formación
- JefeCazador intercepta al jugador
- Nivel completado (4 bosses vencidos)

## Documentación completa
Ver `analisis/` y `diseño/` en esta carpeta para los docs del sistema completo.
