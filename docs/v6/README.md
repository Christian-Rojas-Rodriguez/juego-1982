# MVP 6 — N tipos de enemigos normales

**Agrega sobre MVP 5:** 3 tipos de enemigos normales en oleadas. `EntidadFactory` ya existe, solo se agregan subclases.

## Clases nuevas
- `FragataEnemiga` — movimiento sinusoidal + dispara proyectiles hacia abajo
- (EnemigoKamikaze ya se introdujo en MVP 5)

## Clases modificadas
- `EnemySpawner` — elige tipo de enemigo aleatoriamente entre HARRIER, FRAGATA, KAMIKAZE
- `GameController` / `EstadoJugando` — gestiona proyectiles enemigos (nueva lista o lista unificada)

## Casos de uso nuevos
- FragataEnemiga dispara proyectil (actor: Sistema)

*(documentar al desarrollar esta versión)*
