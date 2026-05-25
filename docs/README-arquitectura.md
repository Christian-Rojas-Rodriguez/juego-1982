# Arquitectura — Módulo Mirage (Fuerza Aérea Argentina)

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Juego "1982" — Conflicto del Atlántico Sur

---

## Visión general

El módulo implementa el nivel del **Avión Mirage** como una unidad autocontenida que:
- Se comunica con el HOME/Lobby a través de una interfaz estable (`MirageModulo` + `HomeFacade`)
- Implementa el patrón **MVC** para separar datos, lógica y presentación
- Aplica los principios **GRASP** y patrones de diseño vistos en clase

**Diferenciadores del juego:**
- Partida infinita de supervivencia con oleadas de enemigos
- 4 bosses únicos con patrones de ataque propios y fases de vida
- 3 tipos de powerups que modifican dinámicamente el comportamiento del Mirage
- Escalado progresivo de dificultad: cada nivel nuevos parámetros de vida, velocidad y oleada
- Métricas avanzadas: precisión de disparo, heatmap de posición, bajas por tipo de enemigo

---

## Estructura de paquetes

```
mirage/
├── MirageModulo.java          ← Facade: único punto de contacto con el HOME
├── HomeFacade.java            ← Interface que el HOME implementa (Protected Variations)
│
├── controller/
│   ├── GameController.java    ← Controller GRASP: orquesta Model y View
│   ├── InputHandler.java      ← Pure Fabrication: traduce teclas a Commands
│   └── commands/              ← Command Pattern: acciones del jugador
│
├── model/
│   ├── estado/                ← State Pattern: JUGANDO, PAUSADO, GAME_OVER, NIVEL_COMPLETADO
│   ├── entidades/             ← Nave (abstract), Mirage, Proyectil
│   │   └── enemigos/          ← Enemigo (abstract) + 3 normales + 4 bosses + EntidadFactory
│   │       └── jefes/         ← Jefe (abstract), JefeBarco, JefeAvionGigante,
│   │                             JefeCuadrilla, JefeCazador
│   ├── fisica/                ← ColisionDetector (filtrado), HitBox
│   ├── niveles/               ← Nivel (abstract), NivelMirage, EnemySpawner,
│   │                             ConfiguradorDificultad
│   ├── powerup/               ← Powerup, TipoPowerup (enum), PowerupManager
│   └── stats/                 ← EstadisticasMirage (heatmap + precisión), ResumenPartida (DTO)
│
├── view/
│   ├── GameRenderer.java      ← Renderiza el estado del Model + boss bar + HUD
│   ├── pantallas/             ← Pantalla (interface), PantallaJuego, PantallaGameOver,
│   │                             PantallaBoss (transición de nivel y entrada de boss)
│   └── sprites/               ← SpriteLoader, Animacion
│
└── excepciones/               ← Jerarquía de excepciones del módulo
```

---

## Separación MVC en el game loop de Processing

```
Juego1982 (extends PApplet)       ← solo propaga eventos del framework
    │  setup()  → MirageModulo.iniciar()
    │  draw()   → MirageModulo.update() + .render()
    │  keyPressed()   → MirageModulo.onKeyPressed(key, keyCode)
    │  keyReleased()  → MirageModulo.onKeyReleased(key, keyCode)
    ▼
MirageModulo (Facade)             ← contrato con el HOME
    │
    ▼
GameController                    ← CONTROLLER: recibe input, orquesta
    │  InputHandler → Commands → flags en Mirage
    │  EstadoJuego (State Pattern) → delega update/render
    │  Mirage.update() → movimiento suave frame a frame
    │  NivelMirage → EnemySpawner + Bosses
    │  ColisionDetector → detectarXxx() filtrado por tipo
    │  PowerupManager → gestión de umbrales y efectos
    │
    ▼
GameRenderer                      ← VIEW: solo lee el Model y llama sketch.xxx()
    │  nunca modifica estado
    │  dibuja fondo, entidades, HUD, boss bar, powerups, pantalla activa
```

**Nota Processing:** `keyPressed()` se dispara repetidamente con OS key-repeat al mantener una tecla. Por eso los Commands de movimiento setean **flags booleanos** en `Mirage` (no ejecutan la lógica directamente). `Mirage.update()` lee los flags en cada frame de `draw()`, logrando movimiento suave y desacoplado del OS.

**Regla clave:** la Vista nunca modifica el Model. El Model nunca llama a Processing directamente.

---

## Principios GRASP aplicados

| Principio | Clase | Decisión |
|-----------|-------|----------|
| **Information Expert** | `ColisionDetector`, `HitBox` | Quien tiene las `HitBox` detecta colisiones |
| **Creator** | `EntidadFactory` | Única clase que instancia `Enemigo` y `Jefe` |
| **Controller** | `GameController` | Único receptor de eventos del sistema (no la Vista) |
| **Low Coupling** | `HomeFacade` (interface), `EnemySpawner` | HOME desacoplado; Spawner retorna enemigos en lugar de mutar listas |
| **High Cohesion** | `GameRenderer`, `PowerupManager`, `ConfiguradorDificultad` | Cada clase hace exactamente una cosa |
| **Polymorphism** | `EstadoJuego`, `Enemigo`, `Jefe` | Variaciones manejadas con herencia/interfaz, sin ifs en cascada |
| **Pure Fabrication** | `InputHandler`, `SpriteLoader`, `EnemySpawner`, `PowerupManager`, `ConfiguradorDificultad` | Servicios que no representan dominio pero mejoran el diseño |
| **Indirection** | `MirageModulo`, `HomeFacade` | Desacopla el módulo del contrato inter-grupos |
| **Protected Variations** | `Enemigo` abstracto + `moverIA()`, `HomeFacade` interface | Los puntos de variación están protegidos por jerarquía o interfaz |

---

## Patrones de diseño

| Patrón | Clase(s) | Por qué |
|--------|----------|---------|
| **Facade** | `MirageModulo`, `HomeFacade` | Contrato estable con el HOME; el interior puede evolucionar |
| **State** | `EstadoJuego` y subclases | Elimina el if-estado en cascada del GameController |
| **Template Method** | `Enemigo.update()`, `Jefe.update()` | Ciclo de vida fijo; lógica de IA variable por subclase (`moverIA`, `ejecutarAtaqueEspecial`) |
| **Factory** | `EntidadFactory` | Crea enemigos y bosses sin exponer el tipo concreto; centraliza el escalado de dificultad |
| **Command** | `Comando` y subclases | Desacopla tecla de acción; facilita teclas configurables y flag-based movement |

---

## Reglas de colisión

`ColisionDetector` tiene **3 métodos explícitos**; no existe método genérico entre entidades cualesquiera:

| Método | Detecta |
|--------|---------|
| `detectarProyectilEnemigo()` | Proyectil del jugador vs. Enemigo/Jefe |
| `detectarEnemigoMirage()` | Enemigo/Jefe vs. Mirage |
| `detectarPowerupMirage()` | Powerup vs. Mirage |

Esto garantiza estructuralmente que **los enemigos no colisionan entre sí**, cumpliendo la regla de negocio sin lógica condicional adicional.

---

## Escalado de dificultad

`ConfiguradorDificultad` centraliza todos los multiplicadores del nivel actual. Se crea una vez en `GameController.init()` y se pasa por composición a `NivelMirage → EnemySpawner / EntidadFactory`. Al completar un nivel, `escalarParaNivel(n+1)` actualiza los multiplicadores en lugar de crear una nueva instancia.

| Parámetro escalado | Afecta a |
|--------------------|---------|
| `multVida` | Vida base de todos los enemigos y bosses |
| `multVelocidad` | Velocidad de movimiento |
| `multCooldown` | Cadencia de disparo de enemigos |
| `multOleada` | Tamaño del batch de enemigos por oleada |
| `getKamikazeCount()` | Cantidad de kamikazes que lanza JefeBarco |

---

## Métricas avanzadas

`EstadisticasMirage` recolecta en tiempo real:

| Métrica | Cómo se registra |
|---------|-----------------|
| Precisión de disparo | `registrarDisparoTotal()` en `DispararCmd`; `registrarDisparoAcertado()` en `ColisionDetector` |
| Heatmap de posición | `registrarPosicion(x,y,w,h)` en `EstadoJugando.update()`, cuantiza a grilla int[][] |
| Bajas por tipo | `registrarDerribo(tipo, puntos)` en `ColisionDetector` con `enemigo.getTipo()` |
| Proyectiles destruidos | Extensible: llamada a `registrarProyectilDestruido()` si se agrega esa mecánica |

Al finalizar la partida, `exportar(vidasRestantes)` genera un `ResumenPartida` que se envía al HOME via `HomeFacade`.

---

## Jerarquía de excepciones

```
JuegoException  (checked)
├── ColisionException
└── RecursoNoEncontradoException
```

Todas las excepciones inesperadas en runtime deben capturarse y reclasificarse
en esta jerarquía antes de propagarse.

---

## Orden recomendado de desarrollo

1. `HomeFacade` + `MirageModulo` — definir el contrato con el HOME primero
2. `HitBox` — implementar `colisionaCon()` y `moverA()` (están en TODO)
3. `Nave` → `Mirage` → `Proyectil` — el Model central, con flags de movimiento
4. `Enemigo` → `HarrierEnemigo`, `FragataEnemiga`, `EnemigoKamikaze` — enemigos normales
5. `Jefe` → `JefeBarco`, `JefeAvionGigante`, `JefeCuadrilla`, `JefeCazador` — bosses
6. `EntidadFactory` + `ConfiguradorDificultad` — creación y escalado
7. `HitBox` → `ColisionDetector` — física con métodos filtrados
8. `EstadoJuego` → estados concretos + `EstadoNivelCompletado` — máquina de estados
9. `NivelMirage` + `EnemySpawner` — secuencia de oleadas y bosses
10. `Powerup` + `TipoPowerup` + `PowerupManager` — sistema de powerups
11. `GameController` — orquestación completa
12. `GameRenderer` + pantallas — vista con boss bar y HUD
13. `EstadisticasMirage` + persistencia — métricas avanzadas
14. Tests JUnit sobre Model (sin Processing)

---

## Documentación relacionada

- [`docs/diseño/diagrama-clases.md`](diseño/diagrama-clases.md) — Diagrama UML de clases (Mermaid)
- [`docs/diseño/diagramas-estado.md`](diseño/diagramas-estado.md) — Máquinas de estado (Mermaid)
- [`docs/analisis/casos-de-uso.md`](analisis/casos-de-uso.md) — Casos de uso y diagramas de secuencia (Mermaid)