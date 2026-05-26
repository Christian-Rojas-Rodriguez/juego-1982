# MVP 3 — Powerups

**Agrega sobre MVP 2:** sistema de powerups que caen al cruzar umbrales de puntuación.

## Clases nuevas
- `TipoPowerup` (enum) — VELOCIDAD_MOVIMIENTO, VELOCIDAD_DISPARO, DISPARO_DOBLE
- `Powerup` — entidad que cae en pantalla
- `PowerupManager` — genera powerups al cruzar umbrales de score

## Clases modificadas
- `ColisionDetector` — agrega `detectarPowerupMirage()`
- `Mirage` — agrega `aplicarPowerup()`, `revertirPowerup()`, `framesPowerupRestantes`
- `DispararCmd` — respeta `TipoPowerup.DISPARO_DOBLE`
- `GameController` / `EstadoJugando` — gestiona lista de powerups
- `GameRenderer` — dibuja powerups en pantalla

## Casos de uso nuevos
*(documentar al desarrollar esta versión)*
