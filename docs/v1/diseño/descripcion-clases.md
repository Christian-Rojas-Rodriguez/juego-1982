# Descripción de Clases — MVP 1

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Orden: de la clase más central a la más auxiliar.

---

## Clases externas del HOME team

Estas interfaces y DTOs son **definidas por el HOME team** (paquete `contracts`). No se modifican; se implementan o se usan.

| Clase | Tipo | Rol en nuestro módulo |
|-------|------|----------------------|
| `ModuloJuego` | interface | Contrato que `ModuloMirage` implementa. Define el ciclo de vida del módulo |
| `IModuloObserver` | interface | HOME registra su `HomeJuego` como observer. Recibe `ModuloEvento` cuando cambiamos estado |
| `EstadisticasGenerales` | DTO (inmutable) | Lo construimos en `getEstadisticasGenerales()` con nuestros datos mapeados |
| `ModuloEvento` | DTO | Lo creamos al disparar eventos. El módulo emite los tipos `INICIADO`, `PAUSADO`, `REANUDADO` y `FINALIZADO` (el enum `Tipo` también incluye `ERROR`, pero el módulo no lo emite en v1) |

---

## 1. `Mirage` — El jugador

**Paquete:** `mirage.model.entidades` · **Extiende:** `Nave`

El avión que controla el jugador. Es el objeto más referenciado: `GameController` lo mueve, `ColisionDetector` lo golpea, `InputHandler` le setea flags, `GameRenderer` lo dibuja.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `x`, `y` | `float` | Posición en pantalla (heredados de `Nave`). Se `constrain` a los bordes en `update()` |
| `velocidad` | `float` | Píxeles por frame. Heredado de `Nave` (no es campo propio) |
| `sketch` | `PApplet` | Referencia al sketch. Se usa para `constrain` con `width/height` y para crear los `Proyectil` |
| `proyectiles` | `List<Proyectil>` | Proyectiles activos. `GameController` los consulta via `getProyectiles()` |
| `disparosTotales` | `int` | Contador de disparos. Se incrementa en `disparar()` |
| `vidas` | `int` | Arranca en 3, máximo 3, Game Over si llega a 0 |
| `puntuacion` | `int` | Crece con `sumarPuntos()`. Lo consulta `ModuloMirage` para el reporte |
| `invencible` | `boolean` | Si `true`, `recibirDanio()` ignora el daño (`ColisionDetector` también lo consulta antes de impactar) |
| `frameInvencible` | `int` | Cuenta regresiva de frames de invencibilidad restantes. Se setea a `DURACION_INVENCIBILIDAD` en `recibirDanio()` y decrementa hasta 0 |
| `DURACION_INVENCIBILIDAD` | `int` | Constante (120 frames = 2 seg a 60 fps) |
| `cooldownDisparo` | `int` | Frames mínimos entre disparos |
| `cooldownActual` | `int` | Cuenta regresiva. Solo dispara cuando llega a 0 |
| `moverIzquierda/Derecha/Arriba/Abajo` | `boolean` | Flags de movimiento. `true` mientras tecla presionada. Se leen en `update()` cada frame |
| `VIDAS_MAX` | `int` | Constante = 3. Nunca se supera |

> La `HitBox` **no** es un campo: `getHitBox()` crea una `HitBox` nueva centrada en `(x, y)` en cada llamada, así que no hay que sincronizarla por frame.

### Métodos

| Método | Qué hace |
|--------|---------|
| `update()` | Lee flags → mueve x/y → `constrain` → decrementa cooldown e invencibilidad → actualiza y limpia sus proyectiles |
| `render(sk)` | Dibuja el sprite. Si `invencible`, parpadea alternando visibilidad. También renderiza sus proyectiles |
| `disparar()` | Si `cooldownActual == 0`: crea un `Proyectil`, lo agrega a `proyectiles`, incrementa `disparosTotales`, resetea cooldown |
| `recibirDanio(danio)` | Si no es invencible, descuenta vidas y activa la invencibilidad temporal |
| `getProyectiles()` | Retorna la lista interna. `GameController`/`EstadoJugando` la itera para colisión y cleanup |
| `getDisparosTotales()` | Consultado por `EstadisticasMirage.getPrecision()` para calcular precisión |
| `sumarPuntos(puntos)` | Incrementa `puntuacion`. Lo llama `ColisionDetector` al destruir un enemigo |
| `setMoverIzquierda/Derecha/Arriba/Abajo(v)` | Setters de los flags. Los llaman los `Commands` desde `InputHandler` |
| `isInvencible()` | Consultado por `ColisionDetector` antes de aplicar daño |
| `estaViva()` | `true` mientras `vidas > 0` (sobreescribe `Nave`, que usa `vida`) |
| `getHitBox()` | Crea y retorna una `HitBox` nueva centrada en `(x, y)` |

---

## 2. `GameController` — El director de orquesta

**Paquete:** `mirage.controller` · **Patrón:** GRASP Controller

Recibe los eventos del game loop (via `ModuloMirage`) y coordina el resto del sistema. Es el dueño de la lista de enemigos y de los efectos. Los proyectiles los gestiona `Mirage`; `GameController` los consulta via `mirage.getProyectiles()`.

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `sketch` | `PApplet` | Referencia al sketch de Processing. Se pasa a `update()`/`render()` donde hace falta |
| `mirageModulo` | `ModuloMirage` | Facade dueño del ciclo de vida. Los estados lo usan para `finalizar()` y `reset()` |
| `estadoActual` | `EstadoJuego` | Estado activo del patrón State. Todo el comportamiento del frame se delega aquí |
| `mirage` | `Mirage` | El jugador |
| `enemigos` | `List<Enemigo>` | Enemigos activos. Se limpia cada frame con `removeIf(!estaViva())` y por salir de pantalla |
| `efectos` | `List<Explosion>` | Efectos de explosión activos. Se crean al morir un enemigo y se descartan al terminar |
| `nivel` | `NivelMirage` | Gestiona oleadas. Provee los enemigos nuevos cada frame |
| `colisionDetector` | `ColisionDetector` | Detecta todas las colisiones del frame |
| `estadisticas` | `EstadisticasMirage` | Registra métricas. Se exporta al finalizar la partida |
| `renderer` | `GameRenderer` | Dibuja todo el estado del modelo |
| `inputHandler` | `InputHandler` | Traduce teclas en Commands |

### Métodos

| Método | Qué hace |
|--------|---------|
| `init()` | Crea todas las dependencias, registra commands en `InputHandler`, arranca en `EstadoJugando` |
| `update()` | Delega en `estadoActual.update(this)`. El estado activo decide qué hacer ese frame |
| `render()` | Delega en `estadoActual.render(this)` |
| `setEstado(nuevoEstado)` | Cambia el estado activo. Llama `alEntrar(this)` en el nuevo estado |
| `onKeyPressed(key, keyCode)` | Pasa la tecla al estado (P/pausa, etc.) y al `InputHandler` (movimiento + SPACE) |
| `onKeyReleased(key, keyCode)` | Pasa la tecla al `InputHandler` para bajar flags de movimiento |

---

## 3. `ModuloMirage` — El Facade hacia el HOME

**Paquete:** `mirage` · **Patrón:** Facade · **Implementa:** `contracts.ModuloJuego`, `contracts.ModuloConInput`

Único punto de contacto con el sistema externo. El HOME team instancia `ModuloMirage`, lo registra en `HomeJuego` y llama a sus métodos de ciclo de vida.

### Responsabilidades clave

| Responsabilidad | Cómo |
|----------------|------|
| Ciclo de vida | `iniciar/pausar/reanudar/finalizar` validan en la máquina de estados del HOME (`contracts.EstadoJuego`), transicionan y disparan el `ModuloEvento` correspondiente (`INICIADO/PAUSADO/REANUDADO/FINALIZADO`) |
| Estadísticas | `getEstadisticasGenerales()` consulta `EstadisticasMirage.exportar()` (que devuelve un `ResumenPartida`) y mapea ese resumen al DTO `EstadisticasGenerales` del HOME |
| Observers | `agregarObserver()` / `removerObserver()` gestionan la lista interna. `notificar(tipo, mensaje)` (privado) la itera |
| I/O Processing | `actualizar(app)` y `dibujar(app)` son los puntos de entrada del game loop. El `GameController` se crea perezosamente en `actualizar()` |
| Input standalone | `onKeyPressed/onKeyReleased` (de `ModuloConInput`) propagan teclas al `GameController` para el runner standalone |

> `inicializarContexto(ctx)` solo guarda el `ContextoJuego` (queda un TODO de precarga). No crea el `GameController` ni carga sprites.

---

## 4. `EstadoJuego` (interno) y sus implementaciones

**Paquete:** `mirage.model.estado` · **Patrón:** State

Elimina el if-estado en cascada del `GameController`. La interfaz declara 4 métodos, todos con el `GameController` como parámetro (el estado no se acopla a él en el constructor): `update`, `render`, `alEntrar` y `onKeyPressed`.

> Es distinto de `contracts.EstadoJuego`, que modela el ciclo de vida del HOME y lo usa `ModuloMirage`.

| Método de la interfaz | Firma |
|-----------------------|-------|
| `update` | `update(GameController controller)` |
| `render` | `render(GameController controller)` |
| `alEntrar` | `alEntrar(GameController controller)` |
| `onKeyPressed` | `onKeyPressed(GameController controller, char key, int keyCode)` |

| Estado | Comportamiento principal |
|--------|-------------------------|
| `EstadoJugando` | Pide enemigos al nivel, actualiza mirage/enemigos/proyectiles, detecta colisiones, spawnea explosiones, limpia entidades inactivas o fuera de pantalla. Si el Mirage muere → `setEstado(new EstadoGameOver())`. Tecla P → `EstadoPausado` |
| `EstadoPausado` | No actualiza nada (juego congelado). Renderiza el estado de fondo + overlay "PAUSA". Tecla P → `EstadoJugando` |
| `EstadoGameOver` | Al entrar (`alEntrar()`): registra fin de partida (`estadisticas.registrarFinPartida(puntaje)`), setea la `PantallaGameOver` en el renderer y llama `mirageModulo.finalizar()` (notifica `FINALIZADO` al HOME). `update()` no hace nada; tecla R → `mirageModulo.reset()` |

---

## 5. `Nave` — Base de todas las entidades móviles

**Paquete:** `mirage.model.entidades` · **Abstracta**

Define los atributos y comportamientos comunes: posición (`x`, `y`), `velocidad` y `vida`. `GameController` y `ColisionDetector` tratan a `Mirage`, `Proyectil` y `Enemigo` de forma uniforme a través de esta clase.

| Miembro | Detalle |
|---------|---------|
| `update()` | Abstracto: cada subclase implementa su movimiento (Template Method) |
| `getHitBox()` | Abstracto: cada subclase construye su `HitBox` |
| `estaViva()` | `vida > 0` por defecto (`Mirage` lo sobreescribe para usar `vidas`) |
| `recibirDanio(danio)` | Resta a `vida` (`Mirage` lo sobreescribe para usar `vidas` + invencibilidad) |
| `getX()`, `getY()`, `getVida()` | Getters de lectura |

---

## 6. `Enemigo` — Base de todos los enemigos

**Paquete:** `mirage.model.entidades.enemigos` · **Abstracta** · **Patrón:** Template Method

Define el ciclo de vida del enemigo. `update()` es `final` y llama `moverIA()`, que cada subclase sobreescribe. Agregar un nuevo tipo de enemigo = 1 subclase que sobreescribe `moverIA()`, `render()`, `getHitBox()` y `getTipo()`.

| Miembro | Detalle |
|---------|---------|
| `update()` | `final`: delega en `moverIA()` (no se sobreescribe) |
| `moverIA()` | Abstracto (`protected`): hook del Template Method |
| `getTipo()` | Abstracto: identificador para estadísticas (ej: `"HARRIER"`) |
| `getPuntos()` | Puntos que otorga el derribo |

---

## 7. `HarrierEnemigo`

**Paquete:** `mirage.model.entidades.enemigos` · **Extiende:** `Enemigo`

Único tipo de enemigo en MVP 1. Hace un zigzag horizontal mientras desciende lentamente.

| Método | Comportamiento |
|--------|----------------|
| `moverIA()` | Avanza en X según `velocidad` y desciende en Y (`y += 1.5`). Al tocar un borde (`x <= 15` o `x >= width - 15`) invierte la dirección horizontal (`velocidad *= -1`) |
| `render(sk)` | Dibuja el sprite volteado en vertical (apunta hacia abajo) o un triángulo rojo de respaldo |
| `getHitBox()` | `HitBox` 30×30 centrada en `(x, y)` |
| `getTipo()` | Retorna `"HARRIER"` (usado para estadísticas por tipo) |

---

## 8. `Proyectil`

**Paquete:** `mirage.model.entidades` · **Extiende:** `Nave`

Se mueve hacia arriba cada frame. `Mirage`/`GameController` eliminan los inactivos o los que salen de pantalla.

| Atributo/Método | Rol |
|----------------|-----|
| `activo` | `false` tras impactar un enemigo o salir de pantalla. Se elimina con `removeIf` |
| `danio` | Daño que aplica al enemigo. Lo lee `ColisionDetector` |
| `update()` | `y -= velocidad`; si sale por arriba, llama `desactivar()` |
| `render(sk)` | Dibuja el sprite o un rectángulo amarillo de respaldo |
| `desactivar()` | Pone `activo = false`. Lo llama `ColisionDetector` al detectar impacto |
| `getHitBox()` | `HitBox` 4×12 centrada en `(x, y)` |

---

## 9. `HitBox`

**Paquete:** `mirage.model.fisica`

Rectángulo AABB (axis-aligned bounding box). Cada entidad construye la suya en `getHitBox()`. `ColisionDetector` solo trabaja con `HitBox`, nunca con coordenadas directamente.

| Método | Qué hace |
|--------|---------|
| `colisionaCon(otro)` | Retorna `true` si los rectángulos se solapan (AABB check) |
| `moverA(x, y)` | Actualiza la posición del rectángulo. Existe en la clase pero **no se usa** en v1: `Nave.update()` es abstracto y las entidades crean una `HitBox` nueva en cada `getHitBox()` en vez de mover una persistente |
| `getX/Y/Ancho/Alto()` | Getters de lectura |

---

## 10. `ColisionDetector`

**Paquete:** `mirage.model.fisica` · **Patrón:** GRASP Pure Fabrication

**Solo tiene 2 métodos** en MVP 1. La ausencia de un método `detectar(entidad, entidad)` garantiza estructuralmente que los enemigos no colisionan entre sí.

| Método | Detecta |
|--------|---------|
| `detectarProyectilEnemigo(proyectiles, enemigos, mirage)` | Proyectil del jugador vs. `HarrierEnemigo`. Aplica daño, desactiva el proyectil, registra el acierto y, si el enemigo muere, suma puntos y registra el derribo |
| `detectarEnemigoMirage(enemigos, mirage)` | `HarrierEnemigo` vs. `Mirage`. Si el Mirage no es invencible, le aplica 1 de daño |

Recibe `EstadisticasMirage` en el constructor para registrar aciertos y derribos.

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

La interfaz `Comando` declara `ejecutar(Mirage)` y `deshacer(Mirage)`. Ningún comando recibe `sketch` ni dependencias en su constructor: todos se instancian con `new XxxCmd()` y reciben el `Mirage` como parámetro al ejecutarse.

| Clase | `ejecutar(mirage)` | `deshacer(mirage)` |
|-------|-------------|-------------|
| `MoverIzquierdaCmd` | `mirage.setMoverIzquierda(true)` | `mirage.setMoverIzquierda(false)` |
| `MoverDerechaCmd` | `mirage.setMoverDerecha(true)` | `mirage.setMoverDerecha(false)` |
| `MoverArribaCmd` | `mirage.setMoverArriba(true)` | `mirage.setMoverArriba(false)` |
| `MoverAbajoCmd` | `mirage.setMoverAbajo(true)` | `mirage.setMoverAbajo(false)` |
| `DispararCmd` | `mirage.disparar()` — Mirage agrega el proyectil a su propia lista | No hace nada (el disparo no es reversible) |

`DispararCmd` se construye sin argumentos (`new DispararCmd()`). No conoce ni al `GameController`, ni al `sketch`, ni a la lista de proyectiles.

---

## 13. `Nivel` (abstracta) y `NivelMirage`

**Paquete:** `mirage.model.niveles`

`Nivel` es la base abstracta del patrón Template Method: declara `update()`, `isTerminado()` y `getEnemigosNuevos()`. `NivelMirage` la extiende.

En MVP 1 `NivelMirage` solo gestiona oleadas infinitas de `HarrierEnemigo`. Sin bosses, sin condición de nivel completado.

| Método (`NivelMirage`) | Qué hace |
|--------|---------|
| `update()` | Delega en `EnemySpawner.update()` |
| `getEnemigosNuevos()` | Retorna los nuevos enemigos del frame (delegando en el spawner). `EstadoJugando` los agrega a la lista del `GameController` |
| `isTerminado()` | Siempre `false` en MVP 1 (la partida solo termina por Game Over) |

---

## 14. `EnemySpawner`

**Paquete:** `mirage.model.niveles` · **Patrón:** GRASP Pure Fabrication

Genera oleadas a intervalos regulares de frames. En MVP 1 crea `HarrierEnemigo` via `EnemyFactory`.

| Atributo | Rol |
|----------|-----|
| `frameCounter` | Cuenta frames desde la última oleada |
| `intervaloFrames` | Frames entre oleadas (180 = 3 seg a 60 fps). Ajustable con `setIntervalo()` |
| `tamanoOleada` | Cantidad de enemigos por oleada |
| `nuevosEsteFrame` | Buffer de enemigos generados; `getEnemigosNuevos()` lo retorna y lo limpia |

---

## 15. `EnemyFactory`

**Paquete:** `mirage.model.entidades.enemigos` · **Patrón:** Factory Method (static)

Centraliza la creación de los tipos de `Enemigo`. En MVP 1 solo crea `HARRIER`. Agregar un tipo nuevo = agregar un valor al enum `Tipo` y un `case` en `crear()`; `EnemySpawner` y `GameController` no cambian.

| Miembro | Detalle |
|---------|---------|
| `enum Tipo` | `HARRIER` (en MVPs futuros: `FRAGATA`, `KAMIKAZE`) |
| `crear(tipo, sketch, x, y)` | Retorna el `Enemigo` concreto según `tipo`; lanza `IllegalArgumentException` si es desconocido |

---

## 16. `EstadisticasMirage`

**Paquete:** `mirage.model.stats`

Recolecta métricas durante la partida y mantiene un historial acumulado entre partidas. Al finalizar, `exportar()` arma un `ResumenPartida` (DTO interno); el mapeo a `EstadisticasGenerales` del HOME lo hace `ModuloMirage.getEstadisticasGenerales()`.

| Atributo | Tipo | Rol |
|----------|------|-----|
| `disparosAcertados` | `int` | Se incrementa en `ColisionDetector` al impactar un enemigo |
| `enemigosDerribados` | `int` | Total de enemigos destruidos en la partida |
| `enemigosPorTipo` | `Map<String, Integer>` | Bajas por tipo. Clave: `enemigo.getTipo()` |
| `tiempoInicioMs` | `long` | Timestamp del inicio. Para calcular la duración |
| `puntajeMaximo` | `int` | Mejor puntaje del historial (se actualiza en `registrarFinPartida()`) |
| `partidasJugadas` | `int` | Contador de partidas finalizadas |
| `partidasGanadas` | `int` | Reservado (sin condición de victoria en v1) |
| `partidasPerdidas` | `int` | En v1 toda partida termina en derrota |

| Método | Qué hace |
|--------|---------|
| `registrarDisparoAcertado()` | Incrementa `disparosAcertados` |
| `registrarDerribo(tipo, puntos)` | Incrementa `enemigosDerribados` y el contador del tipo |
| `registrarFinPartida(puntajeFinal)` | Suma a `partidasJugadas`/`partidasPerdidas` y actualiza `puntajeMaximo`. Lo llama `EstadoGameOver.alEntrar()` |
| `getPrecision(mirage)` | `disparosAcertados / mirage.getDisparosTotales()`. Protege división por cero |
| `exportar(vidasRestantes, mirage)` | Construye y devuelve un `ResumenPartida` (puntaje, derribos, duración, precisión, contadores del historial) |
| `guardar()` / `cargar()` | No-op en v1 (persistencia CSV fuera de alcance) |

---

## 17. `ResumenPartida`

**Paquete:** `mirage.model.stats` · DTO interno (inmutable)

Snapshot de una partida finalizada que crea `EstadisticasMirage.exportar()`. `ModuloMirage` lee sus getters (`getPuntajeFinal()`, `getEnemigosDerribados()`, `getPartidasJugadas/Ganadas/Perdidas()`, `getDuracionSegundos()`, etc.) para construir el `EstadisticasGenerales` del HOME. No se expone fuera del módulo.

---

## 18. `GameRenderer`

**Paquete:** `mirage.view`

Solo lee el modelo y llama a los métodos de Processing. **Nunca modifica estado.**

### Atributos

| Atributo | Tipo | Rol |
|----------|------|-----|
| `pantallaActual` | `Pantalla` | Overlay activo (ej: `PantallaGameOver`). `null` durante el juego normal |
| `spritesListos` | `boolean` | Flag de precarga diferida de sprites |
| `fondo` | `FondoMar` | Fondo de mar con las islas Malvinas |

### Métodos

| Método | Qué hace |
|--------|---------|
| `render(mirage, enemigos, proyectiles, efectos, sketch)` | En el primer render dispara `SpriteLoader.precargarTodos(sketch)` (carga diferida). Luego dibuja: fondo → proyectiles → enemigos → mirage → efectos → HUD → overlay |
| `dibujarHUD(sketch, mirage)` | Puntaje (izquierda) y vidas (derecha) |
| `setPantalla(pantalla)` | Setea el overlay activo (lo usa `EstadoGameOver` para mostrar la `PantallaGameOver`) |

---

## 19. `FondoMar`

**Paquete:** `mirage.view`

Dibuja las Islas Malvinas vistas desde arriba a partir de un "land mask" autotileado con las piezas de costa del pack Kenney. Es estático: se construye una sola vez en un buffer (`PImage`) y cada frame se copia. Si falta el sprite de agua (headless / sin assets) pinta un azul de respaldo. `GameRenderer` lo posee y llama `render(sk)` cada frame.

---

## 20. `Explosion`

**Paquete:** `mirage.model.efectos`

Efecto visual transitorio (procedural, sin sprite) al destruir un enemigo: una ráfaga de partículas que se expande y se desvanece en `DURACION` frames. No afecta la lógica. `GameController` las posee; `EstadoJugando` las crea al morir un enemigo y descarta las terminadas.

| Método | Qué hace |
|--------|---------|
| `update()` | Decrementa los frames de vida restantes |
| `terminada()` | `true` cuando la vida llega a 0 (para descartarla) |
| `render(sk)` | Dibuja el destello central y las partículas que se desvanecen |

---

## 21. `Pantalla` y sus implementaciones

**Paquete:** `mirage.view.pantallas` · **Patrón:** Strategy en la vista

La interfaz `Pantalla` declara `render(sketch)` y `update()`. `GameRenderer` dibuja la pantalla activa.

| Clase | Qué muestra |
|-------|------------|
| `PantallaJuego` | Overlay del juego activo (oleada, power-ups, etc.). Placeholder con TODOs en MVP 1 |
| `PantallaGameOver` | "GAME OVER", puntaje final y enemigos derribados, instrucciones (R reinicia, ESC vuelve al menú) |

---

## 22. `SpriteLoader`

**Paquete:** `mirage.view.sprites` · **Patrón:** GRASP Pure Fabrication

Caché estática de `PImage` (servicio no instanciable). **La carga es diferida**: ocurre en el **primer** `GameRenderer.render()`, que llama `precargarTodos(sketch)` (necesita el `PApplet` vivo). No se carga en `inicializarContexto()`. Tolerante a fallos: si una imagen falta, `get()` devuelve `null` y las entidades dibujan su forma de respaldo (esto mantiene los tests headless en verde).

| Método | Qué hace |
|--------|---------|
| `cargar(nombre, sketch)` | Carga la imagen desde `sprites/` y la cachea (una sola vez) |
| `get(nombre)` | Retorna la `PImage` cacheada o `null` |
| `precargarTodos(sketch)` | Carga todos los sprites del módulo (player, enemigo, bala, tiles del fondo) |

---

## Clases auxiliares / no usadas en v1

Estas clases existen en el código pero **no participan del flujo de v1**:

| Clase | Paquete | Estado |
|-------|---------|--------|
| `Animacion` | `mirage.view.sprites` | Esqueleto/placeholder (métodos con TODO, no se referencia desde ninguna entidad). Las explosiones usan dibujo procedural, no animación por frames |
| `JuegoException` y jerarquía | `mirage.excepciones` | Código no usado en v1. La jerarquía de excepciones que el módulo realmente usa es la del HOME (`contracts.*`, p. ej. `contracts.EstadoInvalidoException`) |
