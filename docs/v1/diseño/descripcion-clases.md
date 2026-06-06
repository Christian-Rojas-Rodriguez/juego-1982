# Descripción de Clases — MVP 1

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Orden: de la clase más central a la más auxiliar.

---

## Clases externas del HOME team

Estas interfaces y DTOs son **definidas por el HOME team** (repo `Modulo_1_Algoritmos_1`). No se modifican; se implementan o se usan.

| Clase | Tipo | Rol en nuestro módulo |
|-------|------|----------------------|
| `ModuloJuego` | interface | Contrato que `ModuloMirage` implementa. Define el ciclo de vida del módulo |
| `IModuloObserver` | interface | HOME registra su `HomeJuego` como observer. Recibe `ModuloEvento` cuando cambiamos estado |
| `EstadisticasGenerales` | DTO (inmutable) | Lo construimos en `getEstadisticasGenerales()` con nuestros datos mapeados |
| `ModuloEvento` | DTO | Lo creamos al disparar eventos (INICIADO, PAUSADO, FINALIZADO, ERROR) |

---

## 1. `Mirage` — El jugador

**Paquete:** `mirage.model.entidades` · **Extiende:** `Nave`

El avión que controla el jugador. Es el objeto más referenciado: `GameController` lo mueve, `ColisionDetector` lo golpea, `InputHandler` le setea flags, `GameRenderer` lo dibuja.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `x`, `y` | `float` | Posición en pantalla. Se constrain a los bordes en `update()` |
| `velocidad` | `float` | Píxeles por frame |
| `hitBox` | `HitBox` | Rectángulo de colisión, sincronizado con `x`, `y` cada frame |
| `proyectiles` | `List<Proyectil>` | Proyectiles activos. `GameController` los consulta via `getProyectiles()` |
| `disparosTotales` | `int` | Contador de disparos. Se incrementa en `disparar()` |
| `vidas` | `int` | Arranca en 3, máximo 3, Game Over si llega a 0 |
| `puntuacion` | `int` | Crece con `sumarPuntos()`. Lo consulta `ModuloMirage` para el reporte |
| `invencible` | `bool` | Si `true`, `ColisionDetector` ignora colisiones con enemigos |
| `frameInvencible` | `int` | Contador de frames desde que se activó la invencibilidad |
| `DURACION_INVENCIBILIDAD` | `int` | Constante (120 frames = 2 seg a 60 fps) |
| `cooldownDisparo` | `int` | Frames mínimos entre disparos |
| `cooldownActual` | `int` | Cuenta regresiva. Solo dispara cuando llega a 0 |
| `moverIzquierda/Derecha/Arriba/Abajo` | `bool` | Flags de movimiento. `true` mientras tecla presionada. Se leen en `update()` cada frame |
| `VIDAS_MAX` | `int` | Constante = 3. Nunca se supera |

### Métodos

| Método | Qué hace |
|--------|---------|
| `update(sketch)` | Lee flags → mueve x/y → constrain → decrementa cooldowns e invencibilidad |
| `render(sketch)` | Dibuja el sprite. Si `invencible`, parpadea alternando visibilidad |
| `disparar(sketch)` | Si `cooldownActual == 0`: crea `Proyectil`, lo agrega a `proyectiles`, incrementa `disparosTotales`, resetea cooldown |
| `getProyectiles()` | Retorna la lista interna. `GameController` la itera para update, colisión y cleanup |
| `getDisparosTotales()` | Consultado por `EstadisticasMirage.exportar()` para calcular precisión |
| `sumarPuntos(puntos)` | Incrementa `puntuacion`. Lo llama `ColisionDetector` al destruir un enemigo |
| `setMoverX(v)` | Setters de los flags. Los llaman los `Commands` desde `InputHandler` |
| `isInvencible()` | Consultado por `ColisionDetector` antes de aplicar daño |

---

## 2. `GameController` — El director de orquesta

**Paquete:** `mirage.controller` · **Patrón:** GRASP Controller

Recibe todos los eventos de Processing y coordina el resto del sistema. Es el dueño de la lista de enemigos. Los proyectiles los gestiona `Mirage`; `GameController` los consulta via `mirage.getProyectiles()`.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `sketch` | `PApplet` | Referencia al sketch de Processing. Se pasa a `update()` y `render()` de cada entidad |
| `estadoActual` | `EstadoJuego` | Estado activo del patrón State. Todo el comportamiento del frame se delega aquí |
| `mirage` | `Mirage` | El jugador |
| `enemigos` | `List<Enemigo>` | Enemigos activos. Se limpia cada frame con `removeIf(!estaViva())` |
| `nivel` | `NivelMirage` | Gestiona oleadas. Provee los enemigos nuevos cada frame |
| `colisionDetector` | `ColisionDetector` | Detecta todas las colisiones del frame |
| `estadisticas` | `EstadisticasMirage` | Registra métricas. Se exporta al finalizar la partida |
| `renderer` | `GameRenderer` | Dibuja todo el estado del modelo |
| `inputHandler` | `InputHandler` | Traduce teclas en Commands |

### Métodos

| Método | Qué hace |
|--------|---------|
| `init()` | Crea todas las dependencias, registra commands en `InputHandler`, arranca el nivel |
| `update()` | Delega en `estadoActual.update(this)`. El estado activo decide qué hacer ese frame |
| `render()` | Delega en `estadoActual.render(this)` y en `renderer` |
| `setEstado(estado)` | Cambia el estado activo. Llama `alEntrar()` en el nuevo estado |

---

## 3. `ModuloMirage` — El Facade hacia el HOME

**Paquete:** `mirage` · **Patrón:** Facade · **Implementa:** `ModuloJuego` (HOME)

Único punto de contacto con el sistema externo. El HOME team instancia `ModuloMirage`, lo registra en `HomeJuego` y llama a sus métodos de ciclo de vida.

### Responsabilidades clave

| Responsabilidad | Cómo |
|----------------|------|
| Ciclo de vida | Delega `iniciar/pausar/reanudar/finalizar` al `GameController` y dispara el `ModuloEvento` correspondiente |
| Estadísticas | `getEstadisticasGenerales()` consulta `EstadisticasMirage` y mapea los datos al DTO de HOME |
| Observers | `agregarObserver()` / `removerObserver()` gestionan la lista interna. `notificar(tipo)` los itera |
| I/O Processing | `actualizar()` y `dibujar()` son los puntos de entrada del game loop |

---

## 4. `EstadoJuego` y sus implementaciones

**Paquete:** `mirage.model.estado` · **Patrón:** State

Elimina el if-estado en cascada del `GameController`. Cada estado implementa su propio `update()`, `render()` y `onKeyPressed()`.

| Estado | Comportamiento principal |
|--------|-------------------------|
| `EstadoJugando` | Mueve entidades, detecta colisiones, actualiza spawner, pide enemigos nuevos |
| `EstadoPausado` | Solo renderiza. No actualiza física ni lógica |
| `EstadoGameOver` | Muestra puntaje. Al entrar llama `ModuloMirage.notificar(FINALIZADO)` |

---

## 5. `Nave` — Base de todas las entidades móviles

**Paquete:** `mirage.model.entidades` · **Abstracta**

Define los atributos y comportamientos comunes: posición, velocidad, vida, hitBox. `GameController` y `ColisionDetector` tratan a `Mirage`, `Proyectil` y `Enemigo` de forma uniforme a través de esta clase.

---

## 6. `Enemigo` — Base de todos los enemigos

**Paquete:** `mirage.model.entidades.enemigos` · **Abstracta** · **Patrón:** Template Method

Define el ciclo de vida del enemigo. `update()` llama `moverIA()` que cada subclase sobreescribe. Agregar un nuevo tipo de enemigo = 1 subclase que sobreescribe `moverIA()` y `getTipo()`.

---

## 7. `HarrierEnemigo`

**Paquete:** `mirage.model.entidades.enemigos` · **Extiende:** `Enemigo`

Único tipo de enemigo en MVP 1. Desciende con un movimiento ondulatorio horizontal. Sobreescribe `moverIA()` con esa lógica.

| Método | Comportamiento |
|--------|----------------|
| `moverIA(sketch)` | Desciende en Y a velocidad constante, oscila en X con seno |
| `getTipo()` | Retorna `"HARRIER"` (usado para estadísticas por tipo) |

---

## 8. `Proyectil`

**Paquete:** `mirage.model.entidades` · **Extiende:** `Nave`

Se mueve hacia arriba cada frame. `GameController` elimina los inactivos o los que salen de pantalla.

| Atributo/Método | Rol |
|----------------|-----|
| `activo` | `false` tras impactar un enemigo. `GameController` lo elimina con `removeIf` |
| `danio` | Daño que aplica al enemigo. Lo lee `ColisionDetector` |
| `desactivar()` | Lo llama `ColisionDetector` al detectar impacto |

---

## 9. `HitBox`

**Paquete:** `mirage.model.fisica`

Rectángulo AABB (axis-aligned bounding box). Toda entidad tiene una. `ColisionDetector` solo trabaja con `HitBox`, nunca con coordenadas directamente.

| Método | Qué hace |
|--------|---------|
| `colisionaCon(otro)` | Retorna `true` si los rectángulos se solapan (AABB check) |
| `moverA(x, y)` | Actualiza la posición. Lo llama `Nave.update()` cada frame |

---

## 10. `ColisionDetector`

**Paquete:** `mirage.model.fisica` · **Patrón:** GRASP Pure Fabrication

**Solo tiene 2 métodos** en MVP 1. La ausencia de un método `detectar(entidad, entidad)` garantiza estructuralmente que los enemigos no colisionan entre sí.

| Método | Detecta |
|--------|---------|
| `detectarProyectilEnemigo(proyectiles, enemigos, mirage)` | Proyectil del jugador vs. HarrierEnemigo |
| `detectarEnemigoMirage(enemigos, mirage)` | HarrierEnemigo vs. Mirage |

---

## 11. `InputHandler`

**Paquete:** `mirage.controller` · **Patrón:** GRASP Pure Fabrication

Mantiene un mapa `keyCode → Comando`. Desacopla la tecla física de la acción del juego.

| Método | Qué hace |
|--------|---------|
| `registrarComando(keyCode, cmd)` | Asocia una tecla a un comando en `init()` del `GameController` |
| `onKeyPressed(keyCode, key, mirage)` | Busca el comando → `ejecutar(mirage)` |
| `onKeyReleased(keyCode, key, mirage)` | Busca el comando → `deshacer(mirage)` (baja flags de movimiento) |

---

## 12. `Comando` y sus implementaciones

**Paquete:** `mirage.controller.commands` · **Patrón:** Command

| Clase | `ejecutar()` | `deshacer()` |
|-------|-------------|-------------|
| `MoverIzquierdaCmd` | `mirage.setMoverIzquierda(true)` | `mirage.setMoverIzquierda(false)` |
| `MoverDerechaCmd` | `mirage.setMoverDerecha(true)` | `mirage.setMoverDerecha(false)` |
| `MoverArribaCmd` | `mirage.setMoverArriba(true)` | `mirage.setMoverArriba(false)` |
| `MoverAbajoCmd` | `mirage.setMoverAbajo(true)` | `mirage.setMoverAbajo(false)` |
| `DispararCmd` | `mirage.disparar(sketch)` — Mirage agrega el proyectil a su propia lista | No hace nada |

`DispararCmd` solo necesita `sketch PApplet` en el constructor. No conoce ni al `GameController` ni a la lista de proyectiles.

---

## 13. `NivelMirage`

**Paquete:** `mirage.model.niveles`

En MVP 1 solo gestiona oleadas infinitas de `HarrierEnemigo`. Sin bosses, sin condición de nivel completado.

| Método | Qué hace |
|--------|---------|
| `update()` | Delega en `EnemySpawner.update()` |
| `getEnemigosNuevos()` | Retorna los nuevos enemigos del frame. `GameController` los agrega a su lista |

---

## 14. `EnemySpawner`

**Paquete:** `mirage.model.niveles` · **Patrón:** GRASP Pure Fabrication

Genera oleadas a intervalos regulares de frames. En MVP 1 solo crea `HarrierEnemigo`.

| Atributo | Rol |
|----------|-----|
| `frameCounter` | Cuenta frames desde la última oleada |
| `intervaloFrames` | Frames entre oleadas (ej: 180 = 3 seg a 60 fps) |
| `tamanoOleada` | Cantidad de enemigos por oleada |

---

## 15. `EstadisticasMirage`

**Paquete:** `mirage.model.stats`

Recolecta métricas durante la partida. Al finalizar, mapea los datos al DTO `EstadisticasGenerales` del HOME.

| Atributo | Tipo | Rol |
|----------|------|-----|
| `disparosAcertados` | `int` | Se incrementa en `ColisionDetector` al impactar un enemigo |
| `enemigosDerribados` | `int` | Total de enemigos destruidos |
| `enemigosPorTipo` | `Map<String, Integer>` | Bajas por tipo. Clave: `enemigo.getTipo()` |
| `tiempoInicioMs` | `long` | Timestamp del inicio. Para calcular `tiempoJugadoSegundos` |

| Método | Qué hace |
|--------|---------|
| `registrarDisparoAcertado()` | Incrementa `disparosAcertados` |
| `registrarDerribo(tipo, puntos)` | Incrementa `enemigosDerribados` y el contador del tipo |
| `exportar(vidasRestantes, mirage)` | Consulta `mirage.getDisparosTotales()` para precisión. Construye el mapa para `EstadisticasGenerales` |
| `getPrecision(mirage)` | `disparosAcertados / mirage.getDisparosTotales()`. Protege división por cero |

---

## 16. `GameRenderer`

**Paquete:** `mirage.view`

Solo lee el modelo y llama a los métodos de Processing. **Nunca modifica estado.**

| Método | Qué hace |
|--------|---------|
| `render(sketch, mirage, enemigos)` | Dibuja fondo → entidades → HUD |
| `dibujarHUD(sketch, mirage)` | Vidas, puntaje, nivel |

---

## 17. `Pantalla` y sus implementaciones

**Paquete:** `mirage.view.pantallas` · **Patrón:** Strategy en la vista

| Clase | Qué muestra |
|-------|------------|
| `PantallaJuego` | El fondo del juego durante la partida |
| `PantallaGameOver` | Puntaje final, instrucciones para reiniciar o salir |

---

## 18. `SpriteLoader`

**Paquete:** `mirage.view.sprites` · **Patrón:** GRASP Pure Fabrication

Caché de `PImage`. Se carga una vez en `inicializarContexto()` y se consulta durante el render.

---

## 19. `JuegoException`

**Paquete:** `mirage.excepciones` · **Checked**

Jerarquía base de excepciones del módulo. Toda excepción inesperada en runtime se reclasifica aquí antes de propagarse.
