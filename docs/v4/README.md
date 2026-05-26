# MVP 4 — Estadísticas avanzadas

**Agrega sobre MVP 3:** métricas avanzadas visibles al HOME y en pantalla de Game Over.

## Clases modificadas
- `EstadisticasMirage` — agrega heatmap de posición (`int[][]`), bajas por tipo (`Map<String,Integer>`)
- `EstadisticasGenerales` (mapping) — precisión de disparo incluida en el reporte al HOME
- `GameRenderer` / `PantallaGameOver` — muestra precisión y enemigos derribados

## Casos de uso nuevos
- Registrar heatmap de posición (cada frame)

*(documentar al desarrollar esta versión)*
