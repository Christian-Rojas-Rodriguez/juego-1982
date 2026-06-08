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

`ModuloMirage` implementa la interfaz `ModuloJuego` del HOME team. Al registrar el módulo con `HomeJuego.registrarModulo(mirageModulo)`, el HOME puede:
- Controlar el ciclo de vida (`iniciar`, `pausar`, `reanudar`, `finalizar`)
- Consultar estadísticas con `getEstadisticasGenerales()` → devuelve `EstadisticasGenerales`
- Recibir notificaciones via `IModuloObserver` (se disparan en INICIADO, PAUSADO, REANUDADO, FINALIZADO)
- Reenviar teclas al módulo via `ModuloConInput` (las que el HOME no intercepta)

## Clases

Ver `diseño/diagrama-clases.md` para el diagrama completo y `diseño/descripcion-clases.md` para la descripción de cada clase.

| Capa | Clases |
|------|--------|
| HOME (externas) | `ModuloJuego`, `ModuloConInput`, `IModuloObserver`, `EstadisticasGenerales`, `ModuloEvento`, `ContextoJuego` (+ máquina de estados y excepciones del ciclo de vida) |
| Facade | `ModuloMirage` (implementa `ModuloJuego` + `ModuloConInput`) |
| Controller | `GameController`, `InputHandler`, `Comando`, 5 Commands (`MoverIzquierda/Derecha/Arriba/Abajo`, `Disparar`) |
| State (gameplay) | `EstadoJuego`, `EstadoJugando`, `EstadoPausado`, `EstadoGameOver` |
| Model | `Nave`, `Mirage`, `Proyectil`, `Enemigo`, `HarrierEnemigo`, `EnemyFactory` |
| Física | `HitBox`, `ColisionDetector` |
| Niveles | `Nivel`, `NivelMirage`, `EnemySpawner` |
| Stats | `EstadisticasMirage`, `ResumenPartida` |
| Vista | `GameRenderer`, `Pantalla`, `PantallaJuego`, `PantallaGameOver`, `SpriteLoader` |
| Efectos | `Explosion` |

> `Animacion` (vista) y la jerarquía `mirage.excepciones.JuegoException` existen como andamiaje pero **no se usan en v1** (la explosión es procedural; las excepciones que se usan son las del HOME, `contracts.*`).

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
