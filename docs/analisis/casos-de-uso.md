# Casos de Uso — Módulo Mirage

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Cubre p8 del TPI: al menos 5 casos de uso con alta interacción entre clases

---

## Actores

| Actor | Descripción |
|-------|-------------|
| **Jugador** | Persona que usa el teclado para controlar el Mirage |
| **Sistema** | El módulo Mirage ejecutándose en Processing |
| **HOME** | Lobby del juego que lanza y recibe resultados del módulo |

---

## Diagrama general de casos de uso

```mermaid
graph LR
    J(("Jugador"))
    S(("Sistema"))
    H(("HOME"))

    J --> UC01["UC-01: Mover el Mirage"]
    J --> UC02["UC-02: Disparar misil"]
    J --> UC06["UC-06: Pausar el juego"]

    S --> UC03["UC-03: Proyectil destruye enemigo"]
    S --> UC04["UC-04: Enemigo impacta al Mirage"]
    S --> UC05["UC-05: Fin de partida (Game Over)"]
    S --> UC07["UC-07: Nivel completado"]
    S --> UC08["UC-08: Oleada de enemigos se activa"]
    S --> UC09["UC-09: Jefe aparece en pantalla"]
    S --> UC10["UC-10: JefeBarco lanza kamikazes"]
    S --> UC11["UC-11: JefeCuadrilla despliega formación"]
    S --> UC12["UC-12: JefeCazador intercepta al jugador"]
    S --> UC13["UC-13: JefeAvionGigante ataque de área"]
    S --> UC14["UC-14: Recoger powerup"]
    S --> UC15["UC-15: Disparo doble activado"]
    S --> UC16["UC-16: Efecto de powerup expira"]
    S --> UC17["UC-17: Escalar dificultad al subir nivel"]
    S --> UC18["UC-18: Registrar heatmap de posición"]
    S --> UC19["UC-19: Exportar métricas al HOME"]
    S --> UC20["UC-20: Kamikaze carga contra el Mirage"]

    UC05 --> H
    UC07 --> H
    UC17 --> H
    UC19 --> H
```

---

## Resumen de casos de uso

| ID | Caso de uso | Actor | Clases principales |
|----|-------------|-------|-------------------|
| UC-01 | Mover el Mirage | Jugador | `InputHandler`, `MoverXxxCmd`, `Mirage` |
| UC-02 | Disparar misil | Jugador | `InputHandler`, `DispararCmd`, `Mirage`, `Proyectil` |
| UC-03 | Proyectil destruye enemigo | Sistema | `ColisionDetector`, `HitBox`, `Proyectil`, `Enemigo`, `EstadisticasMirage` |
| UC-04 | Enemigo impacta al Mirage | Sistema | `ColisionDetector`, `HitBox`, `Mirage`, `EstadoGameOver` |
| UC-05 | Fin de partida (Game Over) | Sistema | `EstadoGameOver`, `EstadisticasMirage`, `HomeFacade`, `MirageModulo` |
| UC-06 | Pausar el juego | Jugador | `GameController`, `EstadoPausado` |
| UC-07 | Nivel completado | Sistema | `NivelMirage`, `EstadoNivelCompletado`, `ConfiguradorDificultad`, `HomeFacade` |
| UC-08 | Oleada de enemigos se activa | Sistema | `EnemySpawner`, `EntidadFactory`, `ConfiguradorDificultad` |
| UC-09 | Jefe aparece en pantalla | Sistema | `NivelMirage`, `EntidadFactory`, `Jefe`, `PantallaBoss` |
| UC-10 | JefeBarco lanza kamikazes | Sistema | `JefeBarco`, `EntidadFactory`, `EnemigoKamikaze`, `ConfiguradorDificultad` |
| UC-11 | JefeCuadrilla despliega formación | Sistema | `JefeCuadrilla`, `EntidadFactory`, `HarrierEnemigo` |
| UC-12 | JefeCazador intercepta al jugador | Sistema | `JefeCazador`, `Mirage` |
| UC-13 | JefeAvionGigante ataque de área | Sistema | `JefeAvionGigante`, `Proyectil`, `GameController` |
| UC-14 | Recoger powerup | Jugador/Sistema | `PowerupManager`, `Powerup`, `ColisionDetector`, `Mirage` |
| UC-15 | Disparo doble activado | Sistema | `Mirage`, `DispararCmd`, `TipoPowerup`, `Proyectil` |
| UC-16 | Efecto de powerup expira | Sistema | `Mirage`, `TipoPowerup` |
| UC-17 | Escalar dificultad al subir nivel | Sistema | `NivelMirage`, `ConfiguradorDificultad`, `HomeFacade`, `EstadoNivelCompletado` |
| UC-18 | Registrar heatmap de posición | Sistema | `EstadisticasMirage`, `EstadoJugando` |
| UC-19 | Exportar métricas al HOME | Sistema | `EstadisticasMirage`, `ResumenPartida`, `HomeFacade`, `MirageModulo` |
| UC-20 | Kamikaze carga contra el Mirage | Sistema | `EnemigoKamikaze`, `Mirage`, `ColisionDetector` |

---

## UC-01: Mover el Mirage

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO, Mirage vivo  
**Postcondición:** Posición del Mirage actualizada dentro de los límites de pantalla

> En Processing, `keyPressed()` se dispara con OS key-repeat al mantener una tecla. Para movimiento suave, los Commands setean **flags booleanos** en `Mirage`; el movimiento ocurre en `Mirage.update()` en cada frame de `draw()`.

```mermaid
sequenceDiagram
    actor Jugador
    participant Juego1982
    participant GameController
    participant InputHandler
    participant MoverCmd as MoverXxxCmd
    participant Mirage

    Jugador->>+Juego1982: keyPressed(keyCode=LEFT)
    Juego1982->>+GameController: onKeyPressed(key, keyCode)
    GameController->>+InputHandler: onKeyPressed(keyCode, key, mirage)
    InputHandler->>+MoverCmd: ejecutar(mirage)
    MoverCmd->>+Mirage: setMoverIzquierda(true)
    deactivate Mirage
    deactivate MoverCmd
    deactivate InputHandler
    deactivate GameController
    deactivate Juego1982

    Note over Juego1982,Mirage: Cada frame de draw()...
    Juego1982->>+GameController: update()
    GameController->>+Mirage: update(sketch)
    Mirage->>Mirage: if moverIzquierda → x -= velocidad
    Mirage->>Mirage: x = constrain(x, 0, width)
    deactivate Mirage
    deactivate GameController

    Jugador->>+Juego1982: keyReleased(keyCode=LEFT)
    Juego1982->>+GameController: onKeyReleased(key, keyCode)
    GameController->>+InputHandler: onKeyReleased(keyCode, key, mirage)
    InputHandler->>+MoverCmd: deshacer(mirage)
    MoverCmd->>+Mirage: setMoverIzquierda(false)
    deactivate Mirage
    deactivate MoverCmd
    deactivate InputHandler
    deactivate GameController
    deactivate Juego1982
```

---

## UC-02: Disparar misil

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO, Mirage vivo, cooldownActual == 0  
**Postcondición:** 1 o 2 `Proyectil` agregados a la lista de proyectiles activos

```mermaid
sequenceDiagram
    actor Jugador
    participant Juego1982
    participant GameController
    participant InputHandler
    participant DispararCmd
    participant Mirage
    participant EstadisticasMirage

    Jugador->>+Juego1982: keyPressed(key=SPACE)
    Juego1982->>+GameController: onKeyPressed(key, keyCode)
    GameController->>+InputHandler: onKeyPressed(keyCode, key, mirage)
    InputHandler->>+DispararCmd: ejecutar(mirage)
    DispararCmd->>+Mirage: disparar(sketch)
    Mirage->>Mirage: cooldownActual = cooldownDisparo
    alt TipoPowerup == DISPARO_DOBLE
        Mirage-->>DispararCmd: [Proyectil(x-10,y), Proyectil(x+10,y)]
    else disparo normal
        Mirage-->>DispararCmd: [Proyectil(x, y)]
    end
    deactivate Mirage
    GameController->>GameController: proyectiles.addAll(nuevos)
    deactivate DispararCmd
    deactivate InputHandler
    GameController->>+EstadisticasMirage: registrarDisparoTotal()
    deactivate EstadisticasMirage
    deactivate GameController
    deactivate Juego1982
```

---

## UC-03: Proyectil destruye enemigo

**Actor:** Sistema  
**Precondición:** Proyectil activo y Enemigo activo en pantalla  
**Postcondición:** Enemigo destruido, puntos sumados, disparo registrado como acertado

```mermaid
sequenceDiagram
    participant GameController
    participant ColisionDetector
    participant Proyectil
    participant Enemigo
    participant Mirage
    participant EstadisticasMirage

    GameController->>+ColisionDetector: detectarProyectilEnemigo(proyectiles, enemigos, mirage)
    loop por cada par proyectil-enemigo
        ColisionDetector->>+Proyectil: getHitBox()
        Proyectil-->>-ColisionDetector: HitBox
        ColisionDetector->>+Enemigo: getHitBox()
        Enemigo-->>-ColisionDetector: HitBox
        ColisionDetector->>ColisionDetector: hitboxA.colisionaCon(hitboxB)
        alt colisión detectada
            ColisionDetector->>+Enemigo: recibirDanio(proyectil.getDanio())
            deactivate Enemigo
            ColisionDetector->>+Proyectil: desactivar()
            deactivate Proyectil
            ColisionDetector->>+EstadisticasMirage: registrarDisparoAcertado()
            deactivate EstadisticasMirage
            alt enemigo.vida <= 0
                ColisionDetector->>+Mirage: sumarPuntos(enemigo.getPuntos())
                deactivate Mirage
                ColisionDetector->>+EstadisticasMirage: registrarDerribo(enemigo.getTipo(), puntos)
                deactivate EstadisticasMirage
            end
        end
    end
    deactivate ColisionDetector
    GameController->>GameController: enemigos.removeIf(!estaViva())
    GameController->>GameController: proyectiles.removeIf(!isActivo())
```

---

## UC-04: Enemigo impacta al Mirage

**Actor:** Sistema  
**Precondición:** Enemigo activo colisiona con la HitBox del Mirage, Mirage no invencible  
**Postcondición:** Mirage pierde una vida y entra en estado invencible

```mermaid
sequenceDiagram
    participant GameController
    participant ColisionDetector
    participant Enemigo
    participant Mirage
    participant EstadisticasMirage

    GameController->>+ColisionDetector: detectarEnemigoMirage(enemigos, mirage)
    loop por cada enemigo
        ColisionDetector->>+Mirage: isInvencible()
        Mirage-->>-ColisionDetector: false
        ColisionDetector->>+Mirage: getHitBox()
        Mirage-->>-ColisionDetector: HitBox
        ColisionDetector->>+Enemigo: getHitBox()
        Enemigo-->>-ColisionDetector: HitBox
        ColisionDetector->>ColisionDetector: hitboxA.colisionaCon(hitboxB)
        alt colisión detectada
            ColisionDetector->>+Mirage: recibirDanio(1)
            Note over Mirage: vidas-- (máx=3, no sube por bosses)
            Note over Mirage: activa invencibilidad temporal
            deactivate Mirage
            alt vidas == 0
                ColisionDetector->>+GameController: setEstado(new EstadoGameOver())
                deactivate GameController
            end
        end
    end
    deactivate ColisionDetector
```

---

## UC-05: Fin de partida (Game Over)

**Actor:** Sistema  
**Precondición:** Mirage sin vidas  
**Postcondición:** Estadísticas exportadas al HOME, pantalla Game Over visible

```mermaid
sequenceDiagram
    participant GameController
    participant EstadoGameOver
    participant EstadisticasMirage
    participant HomeFacade
    participant PantallaGameOver
    participant GameRenderer

    GameController->>+EstadoGameOver: alEntrar(controller)
    EstadoGameOver->>+EstadisticasMirage: registrarFinPartida(puntajeFinal, nivelActual)
    deactivate EstadisticasMirage
    EstadoGameOver->>+EstadisticasMirage: guardar()
    deactivate EstadisticasMirage
    EstadoGameOver->>+EstadisticasMirage: exportar(vidasRestantes)
    EstadisticasMirage-->>-EstadoGameOver: ResumenPartida
    EstadoGameOver->>+HomeFacade: enviarResumen(resumenPartida)
    deactivate HomeFacade
    EstadoGameOver->>+PantallaGameOver: new PantallaGameOver(puntaje, nivel)
    deactivate PantallaGameOver
    EstadoGameOver->>+GameRenderer: setPantalla(pantallaGameOver)
    deactivate GameRenderer
    deactivate EstadoGameOver

    loop cada frame de draw()
        GameController->>+EstadoGameOver: update(controller)
        deactivate EstadoGameOver
        GameController->>+EstadoGameOver: render(controller)
        deactivate EstadoGameOver
    end

    alt Jugador presiona ESC
        GameController->>+EstadoGameOver: onKeyPressed(controller, key, keyCode)
        EstadoGameOver->>GameController: señal de finalizar módulo
        deactivate EstadoGameOver
    end
```

---

## UC-06: Pausar el juego

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO  
**Postcondición:** Estado = PAUSADO (la lógica de juego se congela)

```mermaid
sequenceDiagram
    actor Jugador
    participant Juego1982
    participant GameController
    participant EstadoJugando
    participant EstadoPausado

    Jugador->>+Juego1982: keyPressed(key='P')
    Juego1982->>+GameController: onKeyPressed(key, keyCode)
    GameController->>+EstadoJugando: onKeyPressed(controller, key, keyCode)
    EstadoJugando->>+GameController: setEstado(new EstadoPausado())
    GameController->>+EstadoPausado: alEntrar(controller)
    deactivate EstadoPausado
    deactivate GameController
    deactivate EstadoJugando
    deactivate GameController
    deactivate Juego1982

    loop cada frame de draw()
        Note over EstadoPausado: solo render(), no update()
    end

    Jugador->>+Juego1982: keyPressed(key='P')
    Juego1982->>+GameController: onKeyPressed(key, keyCode)
    GameController->>+EstadoPausado: onKeyPressed(controller, key, keyCode)
    EstadoPausado->>+GameController: setEstado(new EstadoJugando())
    deactivate GameController
    deactivate EstadoPausado
    deactivate GameController
    deactivate Juego1982
```

---

## UC-07: Nivel completado (4 bosses vencidos)

**Actor:** Sistema  
**Precondición:** `jefesVencidos == 4` en `NivelMirage`  
**Postcondición:** Dificultad escalada, nuevo nivel iniciado, HOME notificado

```mermaid
sequenceDiagram
    participant EstadoJugando
    participant GameController
    participant NivelMirage
    participant ConfiguradorDificultad
    participant EstadisticasMirage
    participant HomeFacade
    participant EstadoNivelCompletado

    EstadoJugando->>+NivelMirage: registrarJefeVencido()
    NivelMirage->>NivelMirage: jefesVencidos++
    alt jefesVencidos == 4
        NivelMirage->>NivelMirage: isTerminado() → true
        EstadoJugando->>+GameController: setEstado(new EstadoNivelCompletado())
        GameController->>+EstadoNivelCompletado: alEntrar(controller)
        EstadoNivelCompletado->>+EstadisticasMirage: registrarFinPartida(puntaje, nivelActual)
        deactivate EstadisticasMirage
        EstadoNivelCompletado->>+ConfiguradorDificultad: escalarParaNivel(nivelActual + 1)
        deactivate ConfiguradorDificultad
        EstadoNivelCompletado->>+GameController: nivel = new NivelMirage(sketch, diff, factory)
        deactivate GameController
        EstadoNivelCompletado->>+HomeFacade: notificarNivelCompletado(nivelActual)
        deactivate HomeFacade
        Note over EstadoNivelCompletado: timerFrames cuenta hasta DURACION, luego → EstadoJugando
        deactivate EstadoNivelCompletado
        deactivate GameController
    end
    deactivate NivelMirage
```

---

## UC-08: Oleada de enemigos se activa

**Actor:** Sistema  
**Precondición:** Estado = JUGANDO, `enFaseBoss == false`  
**Postcondición:** Batch de enemigos normales agregados a la lista activa

```mermaid
sequenceDiagram
    participant draw as Juego1982.draw()
    participant GameController
    participant NivelMirage
    participant EnemySpawner
    participant EntidadFactory
    participant ConfiguradorDificultad

    draw->>+GameController: update()
    GameController->>+NivelMirage: update()
    NivelMirage->>+EnemySpawner: update()
    EnemySpawner->>EnemySpawner: frameCounter++
    alt frameCounter >= intervaloFrames
        loop i = 0..tamanoOleada-1
            EnemySpawner->>+EntidadFactory: crearEnemigo(tipoAleatorio, randomX, -20, diff, sketch)
            EntidadFactory->>+ConfiguradorDificultad: getVidaEscalada(base)
            ConfiguradorDificultad-->>-EntidadFactory: vida escalada
            EntidadFactory->>+ConfiguradorDificultad: getVelocidadEscalada(base)
            ConfiguradorDificultad-->>-EntidadFactory: velocidad escalada
            EntidadFactory-->>-EnemySpawner: Enemigo configurado
        end
        EnemySpawner->>EnemySpawner: frameCounter = 0
    end
    NivelMirage->>+EnemySpawner: getEnemigosNuevos()
    EnemySpawner-->>-NivelMirage: List~Enemigo~
    deactivate EnemySpawner
    NivelMirage-->>-GameController: List~Enemigo~
    GameController->>GameController: enemigos.addAll(nuevos)
    GameController->>+EstadisticasMirage: registrarPosicion(mirage.x, mirage.y, width, height)
    deactivate EstadisticasMirage
    deactivate GameController
```

---

## UC-10: JefeBarco lanza kamikazes

**Actor:** Sistema  
**Precondición:** `jefeActual` es `JefeBarco`, `timerAtaque >= intervaloAtaque`  
**Postcondición:** N `EnemigoKamikaze` apuntando al Mirage agregados a la lista

```mermaid
sequenceDiagram
    participant EstadoJugando
    participant NivelMirage
    participant JefeBarco
    participant ConfiguradorDificultad
    participant EntidadFactory
    participant GameController

    EstadoJugando->>+NivelMirage: update()
    NivelMirage->>+JefeBarco: update(sketch, mirage, enemigos)
    JefeBarco->>JefeBarco: timerAtaque++
    alt timerAtaque >= intervaloAtaque
        JefeBarco->>+ConfiguradorDificultad: getKamikazeCount()
        ConfiguradorDificultad-->>-JefeBarco: N
        loop i = 0..N-1
            JefeBarco->>+EntidadFactory: crearEnemigo(KAMIKAZE, x+offset_i, jefeY+50, diff, sketch)
            EntidadFactory-->>-JefeBarco: EnemigoKamikaze
            JefeBarco->>+JefeBarco: kamikaze.setTarget(mirage.x, mirage.y)
            deactivate JefeBarco
            JefeBarco->>JefeBarco: enemigos.add(kamikaze)
        end
        JefeBarco->>JefeBarco: timerAtaque = 0
    end
    deactivate JefeBarco
    deactivate NivelMirage
```

---

## UC-14: Recoger powerup

**Actor:** Jugador / Sistema  
**Precondición:** `puntuacion >= proximoUmbral`, Powerup cayendo en pantalla  
**Postcondición:** Efecto del powerup aplicado a Mirage por `DURACION_FRAMES`

```mermaid
sequenceDiagram
    participant EstadoJugando
    participant PowerupManager
    participant ColisionDetector
    participant Powerup
    participant Mirage

    EstadoJugando->>+PowerupManager: update(mirage.getPuntuacion(), sketch)
    alt puntuacion >= proximoUmbral
        PowerupManager->>PowerupManager: new Powerup(tipoAleatorio)
        PowerupManager->>PowerupManager: activosPowerups.add(powerup)
        PowerupManager->>PowerupManager: proximoUmbralIndex++
    end
    PowerupManager->>+Powerup: update()
    deactivate Powerup
    deactivate PowerupManager

    EstadoJugando->>+ColisionDetector: detectarPowerupMirage(powerups, mirage, duracion)
    loop por cada Powerup activo
        ColisionDetector->>+Powerup: getHitBox()
        Powerup-->>-ColisionDetector: HitBox
        ColisionDetector->>+Mirage: getHitBox()
        Mirage-->>-ColisionDetector: HitBox
        ColisionDetector->>ColisionDetector: hitboxPow.colisionaCon(hitboxMirage)?
        alt colisión
            ColisionDetector->>+Mirage: aplicarPowerup(powerup.getTipo(), duracion)
            Note over Mirage: modifica velocidad / cooldownDisparo / modoDisparoDoble
            deactivate Mirage
            ColisionDetector->>+Powerup: desactivar()
            deactivate Powerup
        end
    end
    deactivate ColisionDetector
    EstadoJugando->>EstadoJugando: powerups.removeIf(!isActivo())
```

---

## UC-20: Kamikaze carga contra el Mirage

**Actor:** Sistema  
**Precondición:** `EnemigoKamikaze` activo, `mirage.getY() > umbralY`  
**Postcondición:** Kamikaze se lanza en línea recta hacia la última posición del Mirage

```mermaid
sequenceDiagram
    participant GameController
    participant EnemigoKamikaze
    participant Mirage
    participant ColisionDetector
    participant EstadisticasMirage

    GameController->>+EnemigoKamikaze: update(sketch)
    EnemigoKamikaze->>+EnemigoKamikaze: moverIA(sketch)
    alt mirage.getY() > umbralY && !cargando
        EnemigoKamikaze->>+Mirage: getX()
        Mirage-->>-EnemigoKamikaze: x
        EnemigoKamikaze->>+Mirage: getY()
        Mirage-->>-EnemigoKamikaze: y
        EnemigoKamikaze->>EnemigoKamikaze: setTarget(x, y)
        EnemigoKamikaze->>EnemigoKamikaze: cargando = true
    end
    alt cargando
        EnemigoKamikaze->>EnemigoKamikaze: mover hacia target a velocidad * 2
    end
    deactivate EnemigoKamikaze
    deactivate EnemigoKamikaze

    GameController->>+ColisionDetector: detectarEnemigoMirage(enemigos, mirage)
    alt colisión kamikaze-mirage && !mirage.isInvencible()
        ColisionDetector->>+Mirage: recibirDanio(1)
        Note over Mirage: vidas-- (máx 3, no sube por bosses)
        deactivate Mirage
        alt vidas == 0
            ColisionDetector->>+GameController: setEstado(new EstadoGameOver())
            deactivate GameController
        end
        ColisionDetector->>+EstadisticasMirage: registrarDerribo("kamikaze", 0)
        deactivate EstadisticasMirage
    end
    deactivate ColisionDetector
```