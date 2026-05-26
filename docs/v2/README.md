# MVP 2 — Stats escaladas

**Agrega sobre MVP 1:** `ConfiguradorDificultad` que escala vida y velocidad de los HarrierEnemigo con el tiempo. Las oleadas se vuelven progresivamente más difíciles sin cambiar el tipo de enemigo.

## Clases nuevas
- `ConfiguradorDificultad` — multiplica vida/velocidad base según tiempo transcurrido

## Clases modificadas
- `EnemySpawner` — recibe `ConfiguradorDificultad` para configurar cada oleada
- `NivelMirage` — actualiza el configurador periódicamente

## Casos de uso nuevos
*(documentar al desarrollar esta versión)*