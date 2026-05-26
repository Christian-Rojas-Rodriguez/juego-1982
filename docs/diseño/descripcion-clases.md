# Descripción de Clases — Módulo Mirage

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Guía de lectura complementaria al diagrama de clases. Orden: de la clase más central a la más auxiliar.

---

## 1. `Mirage` — El jugador

**Paquete:** `mirage.model.entidades`  
**Extiende:** `Nave`

El avión que controla el jugador. Es el objeto más referenciado del sistema: el `GameController` lo mueve, el `ColisionDetector` lo golpea, el `InputHandler` le setea flags, y el `GameRenderer` lo dibuja.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `x`, `y` | `float` | Posición en pantalla. Se constrain a los bordes en `update()` |
| `velocidad` | `float` | Píxeles por frame. Aumenta con el powerup `VELOCIDAD_MOVIMIENTO` |
| `hitBox` | `HitBox` | Rectángulo de colisión, sincronizado con `x`, `y` cada frame |
| `vidas` | `int` | Vidas actuales. Arranca en 3, máximo 3, Game Over si llega a 0 |
| `puntuacion` | `int` | Puntos acumulados. Crece con `sumarPuntos()`. Lo consulta `PowerupManager` para los umbrales |
| `invencible` | `bool` | Si es `true`, `ColisionDetector` ignora colisiones con enemigos |
| `frameInvencible` | `int` | Contador de frames desde que se activó la invencibilidad |
| `DURACION_INVENCIBILIDAD` | `int` | Constante (120 frames = 2 seg). Cuando `frameInvencible` la alcanza, se desactiva la invencibilidad |
| `cooldownDisparo` | `int` | Frames mínimos entre disparos. Se reduce con el powerup `VELOCIDAD_DISPARO` |
| `cooldownActual` | `int` | Cuenta regresiva. Solo se puede disparar cuando llega a 0 |
| `moverIzquierda/Derecha/Arriba/Abajo` | `bool` | Flags de movimiento. `true` mientras la tecla está presionada. Se leen en `update()` cada frame para movimiento suave |
| `powerupActivo` | `TipoPowerup` | Enum del powerup en curso (`NINGUNO`, `VELOCIDAD_MOVIMIENTO`, `VELOCIDAD_DISPARO`, `DISPARO_DOBLE`) |
| `framesPowerupRestantes` | `int` | Cuenta regresiva hasta que el efecto expira. Al llegar a 0 se llama `revertirPowerup()` |

### Métodos

| Método | Qué hace |
|--------|---------|
| `update(sketch)` | Lee los flags → mueve x/y → constrain → decrementa cooldowns y frames de powerup/invencibilidad |
| `render(sketch)` | Dibuja el sprite. Si `invencible`, parpadea alternando visibilidad cada N frames |
| `disparar(sketch)` | Si `cooldownActual == 0`: crea 1 o 2 `Proyectil` según `powerupActivo`. Resetea cooldown. Retorna la lista |
| `aplicarPowerup(tipo, duracion)` | Guarda el tipo, setea `framesPowerupRestantes`. Modifica `velocidad`, `cooldownDisparo` o activa disparo doble |
| `setMoverX(v)` | Setters de los flags. Los llaman los `Commands` desde `InputHandler` |
| `sumarPuntos(puntos)` | Incrementa `puntuacion`. Lo llama `ColisionDetector` al destruir un enemigo |
| `isInvencible()` | Consultado por `ColisionDetector` antes de aplicar daño |
| `getPowerupActivo()` | Consultado por `DispararCmd` para saber cuántos proyectiles crear |

---

## 2. `GameController` — El director de orquesta

**Paquete:** `mirage.controller`  
**Patrón:** GRASP Controller

Recibe todos los eventos de Processing y coordina el resto del sistema. Es el único dueño de las listas de enemigos, proyectiles y powerups. Nadie más las modifica directamente.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `sketch` | `PApplet` | Referencia al sketch de Processing. Se pasa a `update()` y `render()` de cada entidad |
| `estadoActual` | `EstadoJuego` | Estado activo del patrón State. Todo el comportamiento de cada frame se delega aquí |
| `mirage` | `Mirage` | El jugador. Se actualiza, renderiza y consulta en colisiones |
| `enemigos` | `List<Enemigo>` | Todos los enemigos activos (normales y el boss actual). Se limpia cada frame con `removeIf(!estaViva())` |
| `proyectiles` | `List<Proyectil>` | Proyectiles activos del jugador. Se limpia con `removeIf(!isActivo())` |
| `powerups` | `List<Powerup>` | Powerups cayendo en pantalla. Se limpia con `removeIf(!isActivo())` |
| `nivel` | `NivelMirage` | Gestiona oleadas y bosses. Se reemplaza entero al subir de nivel |
| `colisionDetector` | `ColisionDetector` | Detecta todas las colisiones del frame |
| `estadisticas` | `EstadisticasMirage` | Registra métricas. Se exporta al finalizar la partida |
| `renderer` | `GameRenderer` | Dibuja todo el estado del modelo |
| `inputHandler` | `InputHandler` | Traduce teclas en Commands |
| `powerupManager` | `PowerupManager` | Decide cuándo soltar powerups según la puntuación |

### Métodos

| Método | Qué hace |
|--------|---------|
| `init()` | Crea todas las dependencias y las conecta. Punto de arranque del módulo |
| `update()` | Delega en `estadoActual.update(this)`. El estado activo decide qué hacer ese frame |
| `render()` | Delega en `estadoActual.render(this)` y en `renderer` |
| `setEstado(estado)` | Cambia el estado activo. Llama `alEntrar()` en el nuevo estado |
| `onKeyPressed/Released(key, keyCode)` | Delega en `estadoActual` y en `inputHandler` |

---

## 3. `MirageModulo` — La puerta de entrada

**Paquete:** `mirage`  
**Patrón:** Facade

Es el único archivo que el equipo HOME conoce. `Juego1982.java` (el PApplet) lo instancia y llama a sus métodos desde los hooks de Processing: `setup()`, `draw()`, `keyPressed()`, `keyReleased()`.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `controller` | `GameController` | Toda la lógica real vive aquí. El facade solo delega |
| `homeFacade` | `HomeFacade` | Referencia al objeto que implementa la comunicación con el HOME team. Se inyecta por constructor |

### Métodos

| Método | Qué hace |
|--------|---------|
| `iniciar(sketch)` | Llama a `controller.init()`. Equivale al `setup()` del módulo |
| `update()` | Llama a `controller.update()`. Se ejecuta en cada `draw()` |
| `render()` | Llama a `controller.render()`. Se ejecuta en cada `draw()` |
| `onKeyPressed/Released(key, keyCode)` | Propaga el evento al `GameController` |
| `pausar()` / `reanudar()` | API para que el HOME pueda pausar el módulo desde afuera |
| `getResumen()` | Retorna el `ResumenPartida` al HOME al finalizar la partida |

---

## 4. `Nave` — La base de toda entidad móvil

**Paquete:** `mirage.model.entidades`  
**Tipo:** Clase abstracta

Define los atributos y comportamientos comunes a todo lo que se mueve en pantalla: el Mirage, los proyectiles y todos los enemigos. Establece el contrato mínimo que `GameController` y `ColisionDetector` necesitan para tratar a cualquier entidad de forma uniforme.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `x`, `y` | `float` | Posición en pantalla |
| `velocidad` | `float` | Rapidez base de movimiento |
| `vida` | `int` | Puntos de vida actuales |
| `hitBox` | `HitBox` | Caja de colisión. Cada subclase define su tamaño en el constructor |

### Métodos

| Método | Qué hace |
|--------|---------|
| `update()` / `render(sketch)` | Abstractos. Cada subclase implementa su propia lógica de movimiento y dibujo |
| `recibirDanio(danio)` | Descuenta `vida`. Implementado en `Nave`; las subclases pueden sobreescribir para efectos adicionales |
| `estaViva()` | Retorna `vida > 0`. Lo usa `GameController` para limpiar las listas cada frame |
| `getX()` / `getY()` / `getHitBox()` | Getters usados por `ColisionDetector` y `GameRenderer` |

---

## 5. `Enemigo` — La base de todos los enemigos

**Paquete:** `mirage.model.entidades.enemigos`  
**Extiende:** `Nave`  
**Tipo:** Clase abstracta  
**Patrón:** Template Method en `moverIA()`

Define el ciclo de vida común de todos los enemigos. El método `update()` llama a `moverIA()` que cada subclase implementa con su propia IA. Esto permite que `GameController` trate a todos los enemigos —incluidos los bosses— de la misma forma, sin saber qué tipo son.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `puntos` | `int` | Puntos que otorga al ser destruido. Se suma al Mirage via `ColisionDetector` |
| `velocidadX` | `float` | Velocidad horizontal, usada en movimientos sinusoidales o laterales |

### Métodos

| Método | Qué hace |
|--------|---------|
| `update(sketch)` | Llama a `moverIA(sketch)` y sincroniza `hitBox` con la nueva posición |
| `moverIA(sketch)` | **Abstracto.** Cada subclase define su patrón de movimiento aquí |
| `render(sketch)` | **Abstracto.** Cada subclase dibuja su propio sprite |
| `getPuntos()` | Getter usado por `ColisionDetector` al registrar el derribo |
| `getTipo()` | Retorna un `String` identificador (ej: `"harrier"`, `"kamikaze"`). Lo usa `EstadisticasMirage` para las estadísticas por tipo |

---

## 6. `HarrierEnemigo` — Enemigo básico

**Paquete:** `mirage.model.entidades.enemigos`  
**Extiende:** `Enemigo`

El enemigo más simple. Desciende en línea recta desde la parte superior de la pantalla. Sirve como base para entender la jerarquía de enemigos y para entrenar al jugador en los primeros frames de cada oleada.

### `moverIA(sketch)`
Incrementa `y` en `velocidad` cada frame. Sin lógica adicional.

---

## 7. `FragataEnemiga` — Enemigo que dispara

**Paquete:** `mirage.model.entidades.enemigos`  
**Extiende:** `Enemigo`

Desciende con un movimiento sinusoidal horizontal y dispara proyectiles hacia abajo periódicamente.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `cooldownDisparo` | `int` | Frames entre disparos |
| `cooldownActual` | `int` | Cuenta regresiva. Cuando llega a 0, dispara y se resetea |

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `moverIA(sketch)` | Mueve `y` hacia abajo y oscila `x` con una función seno |
| `debeDisparar()` | Retorna `true` cuando `cooldownActual == 0` |
| `disparar(sketch)` | Crea un `Proyectil` apuntando hacia abajo. Lo agrega `GameController` a la lista de proyectiles enemigos |

---

## 8. `EnemigoKamikaze` — Enemigo suicida

**Paquete:** `mirage.model.entidades.enemigos`  
**Extiende:** `Enemigo`

Baja normalmente hasta que el Mirage entra en su zona de carga (`mirage.y > umbralY`). En ese momento "fija" la posición del Mirage como target y se lanza en línea recta hacia él a velocidad doble.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `umbralY` | `float` | Coordenada Y por debajo de la cual activa la carga |
| `cargando` | `bool` | Una vez `true`, ya no cambia el target aunque el Mirage se mueva |
| `targetX`, `targetY` | `float` | Posición del Mirage al momento de iniciar la carga |

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `setTarget(tx, ty)` | Lo llama `JefeBarco` al crear kamikazes: les asigna la posición actual del Mirage |
| `moverIA(sketch)` | Si `!cargando`: baja normal. Si el Mirage está en zona: captura target, activa `cargando`. Si `cargando`: se mueve hacia target a `velocidad * 2` |

---

## 9. `Jefe` — Base de todos los bosses

**Paquete:** `mirage.model.entidades.enemigos.jefes`  
**Extiende:** `Enemigo`  
**Tipo:** Clase abstracta  
**Patrón:** Template Method en `moverIA()` y `ejecutarAtaqueEspecial()`

Agrega sobre `Enemigo` el concepto de **fases** y **ataque especial**. Cada boss tiene múltiples fases de vida; al perder HP suficiente, avanza de fase y su comportamiento cambia. El ataque especial se ejecuta periódicamente usando un timer de frames.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `faseActual` | `int` | Fase de vida (0, 1, 2…). Determina velocidad de ataque y agresividad |
| `maxFases` | `int` | Cuántas fases tiene el boss. Al destruirse en la última fase muere |
| `timerAtaque` | `int` | Contador de frames desde el último ataque especial |
| `intervaloAtaque` | `int` | Frames entre ataques especiales. Puede reducirse al avanzar de fase |

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `update(sketch, mirage, enemigos)` | Recibe `mirage` y `enemigos` porque el boss necesita leer la posición del jugador y puede agregar enemigos. Incrementa `timerAtaque` y llama `ejecutarAtaqueEspecial` si corresponde |
| `ejecutarAtaqueEspecial(mirage, enemigos, sketch)` | **Abstracto.** Cada boss implementa su ataque único |
| `avanzarFase()` | Incrementa `faseActual`. Lo llama `NivelMirage` o el propio boss al superar un umbral de HP |
| `moverIA(sketch)` | **Abstracto.** Cada boss tiene su patrón de movimiento |

---

## 10. `JefeBarco` — Boss 1

**Patrón de movimiento:** Se desplaza horizontalmente de extremo a extremo de la pantalla (bouncing).  
**Ataque especial:** Crea N `EnemigoKamikaze` usando `EntidadFactory`. N depende de `ConfiguradorDificultad.getKamikazeCount()` y crece con cada nivel. Cada kamikaze recibe la posición actual del Mirage como target.

---

## 11. `JefeAvionGigante` — Boss 2

**Patrón de movimiento:** Desciende lentamente desde arriba hasta una posición central y se queda ahí.  
**Ataque especial:** Dispara un abanico de N proyectiles en distintos ángulos que cubren el ancho de la pantalla. El número de proyectiles del abanico (`proyectilesAbanico`) aumenta con la fase.

---

## 12. `JefeCuadrilla` — Boss 3

**Patrón de movimiento:** Oscila lateralmente en la parte superior de la pantalla.  
**Ataque especial:** Instancia 6 `HarrierEnemigo` en formación V usando `EntidadFactory` y los inyecta en la lista de enemigos del `GameController`. La formación aparece desde distintas posiciones X.

---

## 13. `JefeCazador` — Boss 4

**Patrón de movimiento:** En dos fases alternadas:
1. **Alineando:** ajusta su `x` para quedar sobre el Mirage (se mueve horizontalmente hacia él frame a frame)
2. **Zambullendo:** una vez alineado, cae en picada verticalmente a alta velocidad

**Ataque especial:** La zambullada en sí es el ataque. Al salir por abajo de la pantalla, reaparece en la parte superior y reinicia el ciclo.

---

## 14. `EstadoJuego` — Contrato del estado

**Paquete:** `mirage.model.estado`  
**Tipo:** Interface  
**Patrón:** State

Define los 4 métodos que todo estado debe implementar. `GameController` solo conoce esta interface; nunca sabe en qué estado concreto está.

| Método | Cuándo se llama |
|--------|----------------|
| `update(ctrl)` | Cada frame de `draw()`. Aquí va la lógica del estado |
| `render(ctrl)` | Cada frame de `draw()`. Aquí va el dibujo del estado |
| `alEntrar(ctrl)` | Una sola vez, cuando `GameController.setEstado()` lo activa |
| `onKeyPressed(ctrl, key, keyCode)` | Cada vez que el jugador presiona una tecla |

---

## 15. `EstadoJugando`

**Implementa:** `EstadoJuego`

El estado principal. En su `update()` ocurre todo el juego: mueve entidades, spawea enemigos, detecta colisiones, registra el heatmap. Escucha `P` para pausar.

---

## 16. `EstadoPausado`

**Implementa:** `EstadoJuego`

Solo renderiza, no llama a `update()` de ninguna entidad. Escucha `P` para volver a `EstadoJugando`.

---

## 17. `EstadoGameOver`

**Implementa:** `EstadoJuego`

En `alEntrar()`: exporta estadísticas y llama a `HomeFacade.enviarResumen()`. Muestra `PantallaGameOver`. Escucha `ESC` para finalizar el módulo.

---

## 18. `EstadoNivelCompletado`

**Implementa:** `EstadoJuego`

Se activa cuando los 4 bosses del nivel caen. En `alEntrar()`: registra estadísticas, escala la dificultad con `ConfiguradorDificultad`, crea el nuevo `NivelMirage` y notifica al HOME. Tiene un `timerFrames` que cuenta hasta `DURACION` para mostrar una pantalla de transición antes de volver a `EstadoJugando`.

---

## 19. `ColisionDetector` — El árbitro de colisiones

**Paquete:** `mirage.model.fisica`  
**Patrón:** GRASP Pure Fabrication, Information Expert

Centraliza toda la detección de colisiones. Tiene exactamente **3 métodos** — uno por cada tipo de colisión que el juego necesita. No existe ningún método para colisión enemigo-enemigo, lo que garantiza estructuralmente que esa situación nunca ocurre.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `estadisticas` | `EstadisticasMirage` | Al detectar un impacto, registra el disparo acertado o el derribo directamente |

### Métodos

| Método | Qué hace |
|--------|---------|
| `detectarProyectilEnemigo(proyectiles, enemigos, mirage)` | Itera cada par (proyectil, enemigo). Si sus `HitBox` se solapan: aplica daño, desactiva el proyectil, registra estadística. Si el enemigo muere: suma puntos al Mirage |
| `detectarEnemigoMirage(enemigos, mirage)` | Si un enemigo colisiona con el Mirage y éste no es invencible: aplica daño. Si `vidas == 0`: pide al `GameController` que cambie a `EstadoGameOver` |
| `detectarPowerupMirage(powerups, mirage, duracion)` | Si un powerup colisiona con el Mirage: llama `mirage.aplicarPowerup()` y desactiva el powerup |

---

## 20. `HitBox` — La caja de colisión

**Paquete:** `mirage.model.fisica`  
**Patrón:** GRASP Information Expert

Rectángulo invisible (AABB — Axis-Aligned Bounding Box). Sabe si se solapa con otro rectángulo. Cada `Nave` tiene una `HitBox` que se sincroniza con su posición en cada frame.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `x`, `y` | `float` | Esquina superior izquierda del rectángulo |
| `ancho`, `alto` | `float` | Dimensiones. Se definen en el constructor y no cambian |

### Métodos

| Método | Qué hace |
|--------|---------|
| `colisionaCon(otro)` | Algoritmo AABB: dos rectángulos NO colisionan si uno está completamente a la izquierda, derecha, arriba o abajo del otro. Negar esa condición da la colisión |
| `moverA(x, y)` | Actualiza la posición. Lo llama `Nave.update()` en cada frame para seguir al sprite |

---

## 21. `Proyectil` — La bala del Mirage

**Paquete:** `mirage.model.entidades`  
**Extiende:** `Nave`

Se mueve hacia arriba cada frame. Cuando impacta un enemigo o sale por el borde superior, se marca como inactivo y `GameController` lo elimina de la lista.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `activo` | `bool` | Mientras sea `true` se actualiza y colisiona. Al llegar a `false` se elimina |
| `danio` | `int` | Daño que aplica al impactar. Se pasa a `Enemigo.recibirDanio()` |
| `velocidadY` | `float` | Negativo (se mueve hacia arriba). Si es proyectil enemigo, es positivo |

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `update()` | Decrementa `y` en `velocidadY`. Si `y < 0` llama `desactivar()` |
| `desactivar()` | Pone `activo = false`. Lo llama `ColisionDetector` al detectar impacto |
| `isActivo()` | Consultado por `GameController` para limpiar la lista |

---

## 22. `NivelMirage` — El director de nivel

**Paquete:** `mirage.model.niveles`  
**Extiende:** `Nivel`

Gestiona la secuencia completa de un nivel: oleadas de enemigos normales intercaladas con los 4 bosses en orden fijo. Sabe en qué fase está (oleada o boss) y cuándo se termina el nivel.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `spawner` | `EnemySpawner` | Genera las oleadas de enemigos normales |
| `secuenciaBosses` | `List<TipoJefe>` | Lista ordenada: `[BARCO, AVION_GIGANTE, CUADRILLA, CAZADOR]` |
| `indiceBossActual` | `int` | Apunta al próximo boss a spawnear |
| `jefeActual` | `Jefe` | El boss activo en pantalla, o `null` si estamos en fase de oleadas |
| `jefesVencidos` | `int` | Al llegar a 4 → `isTerminado()` retorna `true` |
| `enFaseBoss` | `bool` | `true` mientras hay un boss activo. Las oleadas se pausan |
| `diff` | `ConfiguradorDificultad` | Parámetros del nivel actual. Se pasa a `EnemySpawner` y `EntidadFactory` |

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `update()` | Si `!enFaseBoss`: delega en `spawner`. Cuando las oleadas terminan: crea el siguiente boss via `EntidadFactory`. Si el boss muere: `registrarJefeVencido()` |
| `getEnemigosNuevos()` | Retorna los enemigos creados este frame (del spawner o el nuevo boss) para que `GameController` los agregue |
| `registrarJefeVencido()` | Incrementa `jefesVencidos`. Si llega a 4, el nivel queda terminado |
| `isTerminado()` | Retorna `jefesVencidos == 4` |

---

## 23. `EnemySpawner` — El generador de oleadas

**Paquete:** `mirage.model.niveles`  
**Patrón:** GRASP Pure Fabrication

Usa un contador de frames para spawnear un batch de enemigos cada cierto intervalo. Delega la creación concreta en `EntidadFactory`. Su `intervaloFrames` y `tamanoOleada` los configura `NivelMirage` según el nivel.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `frameCounter` | `int` | Frames desde el último spawn. Se incrementa en `update()` |
| `intervaloFrames` | `int` | Frames entre oleadas. Se reduce al subir de nivel |
| `tamanoOleada` | `int` | Enemigos por oleada. Crece al subir de nivel |
| `tipos` | `List<TipoEnemigo>` | Tipos disponibles para esta oleada. Se aleatoriza en cada spawn |

---

## 24. `ConfiguradorDificultad` — El escalador de dificultad

**Paquete:** `mirage.model.niveles`  
**Patrón:** GRASP Pure Fabrication

Centraliza todos los multiplicadores del nivel actual. Se crea una vez y se pasa por composición a `NivelMirage`, `EnemySpawner` y `EntidadFactory`. Al completar un nivel, `escalarParaNivel(n+1)` actualiza los multiplicadores en el mismo objeto, sin necesidad de crear uno nuevo.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `nivelActual` | `int` | El número de nivel (1, 2, 3…) |
| `multVida` | `float` | Multiplica la vida base de todos los enemigos y bosses |
| `multVelocidad` | `float` | Multiplica la velocidad de movimiento |
| `multCooldown` | `float` | Multiplica (reduce) el cooldown de disparo de enemigos |
| `multOleada` | `float` | Multiplica el tamaño del batch de oleadas |

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `escalarParaNivel(n)` | Recalcula todos los multiplicadores para el nivel `n`. Lo llama `EstadoNivelCompletado` |
| `getVidaEscalada(base)` | Retorna `base * multVida`. Lo usa `EntidadFactory` al construir cada enemigo |
| `getKamikazeCount()` | Retorna cuántos kamikazes crea `JefeBarco`. Crece con el nivel |

---

## 25. `EntidadFactory` — La fábrica única

**Paquete:** `mirage.model.entidades.enemigos`  
**Patrón:** Factory Method

Único lugar donde se instancian enemigos y bosses. Recibe el tipo como enum, aplica los valores escalados de `ConfiguradorDificultad` y retorna el objeto listo para usar. El resto del sistema nunca hace `new HarrierEnemigo(...)` directamente.

### Métodos

| Método | Qué hace |
|--------|---------|
| `crearEnemigo(tipo, x, y, diff, sketch)` | Según `TipoEnemigo`: instancia `HarrierEnemigo`, `FragataEnemiga` o `EnemigoKamikaze` con vida y velocidad escaladas |
| `crearJefe(tipo, diff, sketch)` | Según `TipoJefe`: instancia `JefeBarco`, `JefeAvionGigante`, `JefeCuadrilla` o `JefeCazador` con stats escalados |

---

## 26. `Powerup` — El ítem coleccionable

**Paquete:** `mirage.model.powerup`

Cae desde la parte superior de la pantalla cuando el jugador cruza un umbral de puntuación. Si el Mirage lo toca, su efecto se aplica inmediatamente.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `tipo` | `TipoPowerup` | El efecto que aplica (`VELOCIDAD_MOVIMIENTO`, `VELOCIDAD_DISPARO`, `DISPARO_DOBLE`) |
| `activo` | `bool` | Mientras sea `true` se mueve y puede ser recogido |

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `update()` | Incrementa `y` (cae hacia abajo). Si sale de pantalla: `desactivar()` |
| `getTipo()` | Lo consulta `ColisionDetector` para saber qué efecto aplicar al Mirage |
| `desactivar()` | Pone `activo = false`. Lo llama `ColisionDetector` al detectar la recogida |

---

## 27. `TipoPowerup` — Los tipos de powerup

**Paquete:** `mirage.model.powerup`  
**Tipo:** Enumeración

| Valor | Efecto en Mirage |
|-------|-----------------|
| `NINGUNO` | Estado base, sin powerup activo |
| `VELOCIDAD_MOVIMIENTO` | Aumenta `velocidad` |
| `VELOCIDAD_DISPARO` | Reduce `cooldownDisparo` |
| `DISPARO_DOBLE` | `disparar()` crea 2 proyectiles con offset ±10px |

---

## 28. `PowerupManager` — El gestor de umbrales

**Paquete:** `mirage.model.powerup`  
**Patrón:** GRASP Pure Fabrication

Sabe cuándo soltar cada powerup según la puntuación del jugador. Tiene una lista de umbrales (ej: 500, 1500, 3000 puntos). Cuando `puntuacion >= proximoUmbral`, crea un `Powerup` de tipo aleatorio y lo retorna al `GameController`.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `umbrales` | `List<Integer>` | Puntos necesarios para soltar cada powerup |
| `proximoUmbralIndex` | `int` | Apunta al próximo umbral sin alcanzar |
| `DURACION_FRAMES` | `int` | Cuántos frames dura el efecto del powerup en el Mirage |

---

## 29. `InputHandler` — El traductor de teclas

**Paquete:** `mirage.controller`  
**Patrón:** GRASP Pure Fabrication

Mantiene un mapa `keyCode → Comando`. Cuando llega un evento de teclado, busca el `Comando` correspondiente y lo ejecuta. Desacopla la tecla física de la acción del juego.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `comandosPorKeyCode` | `Map<Integer, Comando>` | Mapeo tecla → acción. Permite cambiar atajos sin tocar otra clase |

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `onKeyPressed(keyCode, key, mirage)` | Busca el comando en el mapa. Si existe: `comando.ejecutar(mirage)` |
| `onKeyReleased(keyCode, key, mirage)` | Busca el comando. Si existe: `comando.deshacer(mirage)` (baja los flags de movimiento) |

---

## 30. `Comando` y sus implementaciones

**Paquete:** `mirage.controller.commands`  
**Tipo:** Interface + 5 implementaciones  
**Patrón:** Command

| Clase | `ejecutar()` | `deshacer()` |
|-------|-------------|-------------|
| `MoverIzquierdaCmd` | `mirage.setMoverIzquierda(true)` | `mirage.setMoverIzquierda(false)` |
| `MoverDerechaCmd` | `mirage.setMoverDerecha(true)` | `mirage.setMoverDerecha(false)` |
| `MoverArribaCmd` | `mirage.setMoverArriba(true)` | `mirage.setMoverArriba(false)` |
| `MoverAbajoCmd` | `mirage.setMoverAbajo(true)` | `mirage.setMoverAbajo(false)` |
| `DispararCmd` | Consulta `mirage.getPowerupActivo()` → llama `mirage.disparar(sketch)` → agrega proyectiles al `GameController` | No hace nada (`deshacer` no aplica al disparo) |

---

## 31. `EstadisticasMirage` — El registrador de métricas

**Paquete:** `mirage.model.stats`

Recolecta todas las métricas avanzadas durante la partida. Al finalizar, las empaqueta en un `ResumenPartida` y lo envía al HOME via `HomeFacade`.

### Atributos clave

| Atributo | Tipo | Rol |
|----------|------|-----|
| `disparosTotales` | `int` | Se incrementa en `DispararCmd` |
| `disparosAcertados` | `int` | Se incrementa en `ColisionDetector` al impactar un enemigo |
| `heatmap` | `int[][]` | Grilla 20×20. Cada celda acumula cuántos frames pasó el Mirage en esa zona |
| `enemigosPorTipo` | `Map<String, Integer>` | Bajas por tipo de enemigo. Clave: `enemigo.getTipo()` |
| `nivelMaximo` | `int` | Mayor nivel alcanzado en la partida |

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `registrarPosicion(x, y, w, h)` | Cuantiza `(x,y)` a una celda de la grilla y la incrementa. Se llama cada frame desde `EstadoJugando` |
| `registrarDisparoTotal()` | Incrementa `disparosTotales` |
| `registrarDisparoAcertado()` | Incrementa `disparosAcertados` |
| `registrarDerribo(tipo, puntos)` | Incrementa el contador del tipo en `enemigosPorTipo` |
| `getPrecision()` | Retorna `disparosAcertados / disparosTotales`. Protege división por cero |
| `exportar(vidasRestantes)` | Construye y retorna el `ResumenPartida` con todas las métricas |

---

## 32. `ResumenPartida` — El DTO de resultados

**Paquete:** `mirage.model.stats`  
**Patrón:** DTO (Data Transfer Object)

Solo tiene atributos y getters. No tiene lógica. Es el objeto que `MirageModulo.getResumen()` entrega al HOME team. Contiene todo lo que el HOME necesita para mostrar resultados: puntos, precisión, heatmap, bajas por tipo, nivel alcanzado.

---

## 33. `GameRenderer` — El dibujante

**Paquete:** `mirage.view`

Recibe el estado completo del modelo y lo dibuja usando el `sketch` de Processing. **Nunca modifica el estado.** Delega el dibujo de pantallas especiales (game over, transición) en la `Pantalla` activa.

### Métodos clave

| Método | Qué hace |
|--------|---------|
| `render(sketch, mirage, enemigos, proyectiles, powerups, jefe)` | Dibuja fondo → entidades → HUD → boss bar (si hay jefe activo) → pantalla activa |
| `dibujarHUD(sketch, mirage, nivel)` | Dibuja vidas, puntuación y nivel en la interfaz |
| `dibujarBossBar(sketch, jefe)` | Barra de vida del boss, visible cuando `jefe != null` |
| `setPantalla(pantalla)` | Cambia la pantalla activa (game over, transición de nivel, intro de boss) |

---

## 34. `Pantalla` y sus implementaciones

**Paquete:** `mirage.view.pantallas`  
**Tipo:** Interface + 3 implementaciones

| Clase | Cuándo se usa |
|-------|--------------|
| `PantallaJuego` | Durante `EstadoJugando` normal |
| `PantallaGameOver` | Al entrar en `EstadoGameOver`. Muestra puntaje y nivel alcanzado |
| `PantallaBoss` | Al inicio de un boss (intro) y durante `EstadoNivelCompletado` (transición) |

---

## 35. `HomeFacade` — El contrato con el HOME team

**Paquete:** `mirage`  
**Tipo:** Interface  
**Patrón:** Protected Variations, Indirection

Nosotros definimos esta interface; el HOME team la implementa. `MirageModulo` la recibe por constructor injection y nunca importa ninguna clase del HOME. Si el HOME cambia su implementación, nuestro módulo no se modifica.

| Método | Cuándo se llama |
|--------|----------------|
| `enviarResumen(resumen)` | Al entrar en `EstadoGameOver` |
| `notificarNivelCompletado(nivel)` | Al entrar en `EstadoNivelCompletado` |

---

## 36. `SpriteLoader` y `Animacion` — Los recursos gráficos

**Paquete:** `mirage.view.sprites`

**`SpriteLoader`** (Pure Fabrication): carga imágenes del disco una sola vez y las guarda en un cache `Map<String, PImage>`. Todos los `render()` piden sus imágenes aquí en lugar de cargarlas cada frame.

**`Animacion`**: gestiona una secuencia de frames (lista de nombres de imágenes). En cada `update()` avanza el frame según su velocidad. Se usa para explosiones y efectos visuales de corta duración.

---

## 37. `Nivel` — Contrato de nivel

**Paquete:** `mirage.model.niveles`  
**Tipo:** Clase abstracta

Define el contrato mínimo que `GameController` necesita de cualquier nivel: `update()`, `getEnemigosNuevos()` e `isTerminado()`. Si en el futuro hubiera un modo arena o un nivel tutorial, extenderían esta clase.

---

## 38. Jerarquía de Excepciones

**Paquete:** `mirage.excepciones`

| Clase | Cuándo se lanza |
|-------|----------------|
| `JuegoException` | Raíz checked. Toda excepción del módulo debe ser subclase de esta |
| `ColisionException` | Problema en la detección de colisiones (ej: hitbox nula) |
| `RecursoNoEncontradoException` | Imagen o recurso no encontrado por `SpriteLoader` |

Toda excepción inesperada de runtime se captura y reclasifica en esta jerarquía antes de propagarse, para que el HOME pueda manejarla de forma uniforme.

---

## Enumeraciones de tipos

| Enum | Valores | Usado por |
|------|---------|-----------|
| `TipoEnemigo` | `HARRIER`, `FRAGATA`, `KAMIKAZE` | `EntidadFactory`, `EnemySpawner` |
| `TipoJefe` | `BARCO`, `AVION_GIGANTE`, `CUADRILLA`, `CAZADOR` | `EntidadFactory`, `NivelMirage` |
| `TipoPowerup` | `NINGUNO`, `VELOCIDAD_MOVIMIENTO`, `VELOCIDAD_DISPARO`, `DISPARO_DOBLE` | `Mirage`, `Powerup`, `DispararCmd` |