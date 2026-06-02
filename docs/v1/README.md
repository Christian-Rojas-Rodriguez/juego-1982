# MVP 1 — Un enemigo, puntos, integración HOME

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Primera versión entregable y jugable del módulo Mirage

## Qué es jugable

- Mirage se mueve con flechas y dispara con SPACE
- Oleadas de `HarrierEnemigo` caen desde arriba
- Colisiones: proyectil destruye enemigo (suma puntos), enemigo impacta Mirage (pierde vida)
- 3 vidas · Game Over al llegar a 0
- Pausa con P

## Integración con HOME

`MirageModulo` implementa la interfaz `ModuloJuego` del HOME team. Al registrar el módulo con `HomeJuego.registrarModulo(mirageModulo)`, el HOME puede:
- Controlar el ciclo de vida (`iniciar`, `pausar`, `reanudar`, `finalizar`)
- Consultar estadísticas con `getEstadisticasGenerales()` → devuelve `EstadisticasGenerales`
- Recibir notificaciones via `IModuloObserver` (se disparan en INICIADO, PAUSADO, FINALIZADO)

## Clases introducidas (26)

Ver `diseño/diagrama-clases.md` para el diagrama completo y `diseño/descripcion-clases.md` para la descripción de cada clase.

| Capa | Clases |
|------|--------|
| HOME (externas) | `ModuloJuego`, `IModuloObserver`, `EstadisticasGenerales`, `ModuloEvento` |
| Facade | `MirageModulo` |
| Controller | `GameController`, `InputHandler`, `Comando`, 5 Commands |
| State | `EstadoJuego`, `EstadoJugando`, `EstadoPausado`, `EstadoGameOver` |
| Model | `Nave`, `Mirage`, `Proyectil`, `Enemigo`, `HarrierEnemigo` |
| Física | `HitBox`, `ColisionDetector` |
| Niveles | `NivelMirage`, `EnemySpawner` |
| Stats | `EstadisticasMirage` |
| Vista | `GameRenderer`, `Pantalla`, `PantallaJuego`, `PantallaGameOver`, `SpriteLoader` |
| Excepciones | `JuegoException` |

## Casos de uso (8)

Ver `analisis/casos-de-uso.md`

| ID | Nombre | Actor |
|----|--------|-------|
| UC-01 | Mover el Mirage | Jugador |
| UC-02 | Disparar misil | Jugador |
| UC-03 | Pausar / Reanudar | Jugador |
| UC-04 | Proyectil destruye enemigo | Sistema |
| UC-05 | Enemigo impacta al Mirage | Sistema |
| UC-06 | Oleada de enemigos se activa | Sistema |
| UC-07 | Game Over | Sistema |
| UC-08 | Exportar estadísticas al HOME | Sistema / HOME |
