# Arquitectura — Módulo Mirage (Fuerza Aérea Argentina)

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Juego "1982" — Conflicto del Atlántico Sur

---

## Visión general

El módulo implementa el nivel del **Avión Mirage** como una unidad autocontenida que:
- Se comunica con el HOME/Lobby a través de una interfaz estable (`MirageModulo`)
- Implementa el patrón **MVC** para separar datos, lógica y presentación
- Aplica los principios **GRASP** y patrones de diseño vistos en clase

---

## Estructura de paquetes

```
mirage/
├── MirageModulo.java          ← Facade: único punto de contacto con el HOME
│
├── controller/
│   ├── GameController.java    ← Controller GRASP: orquesta Model y View
│   ├── InputHandler.java      ← Pure Fabrication: traduce teclas a Commands
│   └── commands/              ← Command Pattern: acciones del jugador
│
├── model/
│   ├── estado/                ← State Pattern: JUGANDO, PAUSADO, GAME_OVER
│   ├── entidades/             ← Nave (abstract), Mirage, Proyectil
│   │   └── enemigos/          ← Enemigo (abstract) + subclases + Factory
│   ├── fisica/                ← ColisionDetector, HitBox
│   ├── niveles/               ← Nivel (abstract), NivelMirage, EnemySpawner
│   └── stats/                 ← EstadisticasMirage, ResumenPartida (DTO)
│
├── view/
│   ├── GameRenderer.java      ← Renderiza el estado del Model
│   ├── pantallas/             ← Pantalla (interface), PantallaJuego, etc.
│   └── sprites/               ← SpriteLoader, Animacion
│
└── excepciones/               ← Jerarquía de excepciones del módulo
```

---

## Separación MVC en el game loop

```
Juego1982 (PApplet)           ← solo propaga eventos del framework
    │
    ▼
MirageModulo (Facade)         ← contrato con el HOME
    │
    ▼
GameController                ← CONTROLLER: recibe input, orquesta
    │  lee InputHandler → Commands → Mirage.mover()
    │  delega lógica   → EstadoJuego (State Pattern)
    │  actualiza Model → Mirage, Enemigos, Proyectiles, Nivel
    │  detecta colisiones → ColisionDetector
    │
    ▼
GameRenderer                  ← VIEW: solo lee el Model y llama sketch.xxx()
    │  nunca modifica estado
    │  dibuja fondo, entidades, HUD, pantalla activa
```

**Regla clave:** la Vista nunca modifica el Model. El Model nunca llama a Processing directamente.

---

## Principios GRASP aplicados

| Principio | Clase | Decisión |
|-----------|-------|----------|
| **Information Expert** | `ColisionDetector` | Quien tiene las `HitBox` detecta colisiones |
| **Creator** | `EnemyFactory` | Quien usa/agrega los enemigos los crea |
| **Controller** | `GameController` | Único receptor de eventos del sistema (no la Vista) |
| **Low Coupling** | `EnemySpawner` | Retorna enemigos en lugar de mutar listas ajenas |
| **High Cohesion** | `GameRenderer` | Solo dibuja; `GameController` solo orquesta |
| **Polymorphism** | `EstadoJuego`, `Enemigo` | Variaciones manejadas con herencia/interfaz, sin ifs en cadena |
| **Pure Fabrication** | `InputHandler`, `SpriteLoader`, `EnemySpawner` | Servicios que no representan dominio pero mejoran el diseño |
| **Indirection** | `MirageModulo` | Desacopla el módulo del contrato inter-grupos |
| **Protected Variations** | `Enemigo` abstracto + `moverIA()` | El punto de variación (IA) está protegido por la jerarquía |

---

## Patrones de diseño

| Patrón | Clase(s) | Por qué |
|--------|----------|---------|
| **Facade** | `MirageModulo` | Contrato estable con el HOME; el interior puede evolucionar |
| **State** | `EstadoJuego` y subclases | Elimina el if-estado en cascada del GameController |
| **Template Method** | `Enemigo.update()` | Ciclo de vida fijo; lógica de IA variable por subclase |
| **Factory Method** | `EnemyFactory` | Crea enemigos sin exponer el tipo concreto |
| **Command** | `Comando` y subclases | Desacopla tecla de acción; facilita teclas configurables |
| **Strategy** | `moverIA()` en enemigos | Algoritmo de movimiento intercambiable sin tocar el Controller |

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

1. `MirageModulo` — definir el contrato con el HOME primero
2. `Nave` → `Mirage` → `Proyectil` — el Model central
3. `Enemigo` → subclases → `EnemyFactory` — entidades enemigas
4. `HitBox` → `ColisionDetector` — física
5. `EstadoJuego` → estados concretos — máquina de estados
6. `GameController` — orquestación
7. `GameRenderer` + pantallas — vista
8. `EstadisticasMirage` + persistencia — estadísticas
9. Tests JUnit sobre Model (sin Processing)

---

## Documentación relacionada

- [`docs/diseño/diagrama-clases.md`](diseño/diagrama-clases.md) — Diagrama UML de clases (Mermaid)
- [`docs/diseño/diagramas-estado.md`](diseño/diagramas-estado.md) — Máquinas de estado (Mermaid)
- [`docs/analisis/casos-de-uso.md`](analisis/casos-de-uso.md) — Casos de uso y diagramas de secuencia (Mermaid)
