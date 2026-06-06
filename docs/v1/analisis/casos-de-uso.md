# Casos de Uso — MVP 1

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Módulo Mirage — primera versión entregable

---

## Diagrama de casos de uso

```mermaid
flowchart LR
    J["👤\nJugador"]
    H["🏠\nHOME"]

    subgraph SM["Sistema Mirage"]
        direction TB
        UC01(["Mover el Mirage"])
        UC02(["Disparar misil"])
        UC03(["Pausar / Reanudar"])
        UC04(["Proyectil destruye enemigo"])
        UC05(["Enemigo impacta Mirage"])
        UC06(["Oleada de enemigos"])
        UC07(["Game Over"])
        UC08(["Exportar estadísticas al HOME"])
    end

    J --> UC01
    J --> UC02
    J --> UC03
    H -->|"«controla ciclo de vida»"| SM
    UC02 -.->|"«include»"| UC04
    UC05 -.->|"«extend»"| UC07
    UC07 -.->|"«include»"| UC08
    H -.->|"«recibe»"| UC08
```

---

## Tabla de casos de uso

| ID | Nombre | Actor | Clases involucradas |
|----|--------|-------|---------------------|
| UC-01 | Mover el Mirage | Jugador | `InputHandler`, `MoverXCmd`, `Mirage` |
| UC-02 | Disparar misil | Jugador | `InputHandler`, `DispararCmd`, `Mirage`, `Proyectil` |
| UC-03 | Pausar / Reanudar | Jugador | `GameController`, `EstadoPausado`, `EstadoJugando` |
| UC-04 | Proyectil destruye enemigo | Sistema | `ColisionDetector`, `Proyectil`, `HarrierEnemigo`, `Mirage`, `EstadisticasMirage` |
| UC-05 | Enemigo impacta al Mirage | Sistema | `ColisionDetector`, `HarrierEnemigo`, `Mirage` |
| UC-06 | Oleada de enemigos se activa | Sistema | `EnemySpawner`, `HarrierEnemigo`, `NivelMirage` |
| UC-07 | Game Over | Sistema | `GameController`, `EstadoGameOver`, `MirageModulo` |
| UC-08 | Exportar estadísticas al HOME | Sistema / HOME | `MirageModulo`, `EstadisticasMirage`, `EstadisticasGenerales`, `IModuloObserver` |

---

## UC-01: Mover el Mirage

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO, Mirage vivo  
**Postcondición:** posición del Mirage actualizada en el siguiente frame

```mermaid
sequenceDiagram
    actor Jugador
    participant Juego1982
    participant GameController
    participant InputHandler
    participant MoverDerechaCmd
    participant Mirage

    Jugador->>+Juego1982: keyPressed(keyCode=RIGHT)
    Juego1982->>+GameController: onKeyPressed(key, keyCode)
    GameController->>+InputHandler: onKeyPressed(keyCode, key, mirage)
    InputHandler->>+MoverDerechaCmd: ejecutar(mirage)
    MoverDerechaCmd->>Mirage: setMoverDerecha(true)
    deactivate MoverDerechaCmd
    deactivate InputHandler
    deactivate GameController
    deactivate Juego1982

    Note over Juego1982,Mirage: En el siguiente draw() frame
    activate Juego1982
    Juego1982->>+GameController: actualizar()
    GameController->>+Mirage: update(sketch)
    Mirage->>Mirage: x += velocidad (flag moverDerecha == true)
    Mirage->>Mirage: constrain(x, 0, width)
    deactivate Mirage
    deactivate GameController
    deactivate Juego1982

    Note over Jugador,Mirage: keyReleased baja el flag → Mirage se detiene
```

---

## UC-02: Disparar misil

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO, `cooldownActual == 0`  
**Postcondición:** 1 `Proyectil` agregado a `mirage.proyectiles`

```mermaid
sequenceDiagram
    actor Jugador
    participant Juego1982
    participant GameController
    participant InputHandler
    participant DispararCmd
    participant Mirage

    Jugador->>+Juego1982: keyPressed(key=SPACE)
    Juego1982->>+GameController: onKeyPressed(key, keyCode)
    GameController->>+InputHandler: onKeyPressed(keyCode, key, mirage)
    InputHandler->>+DispararCmd: ejecutar(mirage)
    DispararCmd->>+Mirage: disparar(sketch)
    Mirage->>Mirage: cooldownActual = cooldownDisparo
    Mirage->>Mirage: disparosTotales++
    Mirage->>Mirage: proyectiles.add(new Proyectil(x, y))
    deactivate Mirage
    deactivate DispararCmd
    deactivate InputHandler
    deactivate GameController
    deactivate Juego1982
```

---

## UC-03: Pausar / Reanudar

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO o PAUSADO  
**Postcondición:** estado cambia entre JUGANDO y PAUSADO

```mermaid
sequenceDiagram
    actor Jugador
    participant Juego1982
    participant GameController
    participant EstadoJugando
    participant EstadoPausado

    Jugador->>+Juego1982: keyPressed(key='P')
    Juego1982->>+GameController: onKeyPressed('P', keyCode)
    GameController->>+EstadoJugando: onKeyPressed(ctrl, 'P', keyCode)
    EstadoJugando->>GameController: setEstado(new EstadoPausado())
    deactivate EstadoJugando
    activate EstadoPausado
    Note over EstadoPausado: alEntrar(ctrl) — solo renderiza, no actualiza
    deactivate EstadoPausado
    deactivate GameController
    deactivate Juego1982
```

---

## UC-04: Proyectil destruye enemigo

**Actor:** Sistema  
**Precondición:** `Proyectil` activo y `HarrierEnemigo` activo en pantalla  
**Postcondición:** enemigo destruido, puntos sumados, disparo acertado registrado

```mermaid
sequenceDiagram
    participant EstadoJugando
    participant GameController
    participant ColisionDetector
    participant Mirage
    participant Proyectil
    participant HarrierEnemigo
    participant HitBox
    participant EstadisticasMirage

    EstadoJugando->>+GameController: getMirage()
    GameController-->>-EstadoJugando: mirage
    EstadoJugando->>+GameController: getEnemigos()
    GameController-->>-EstadoJugando: enemigos
    EstadoJugando->>+GameController: getColisionDetector()
    GameController-->>-EstadoJugando: colisionDetector
    EstadoJugando->>+Mirage: getProyectiles()
    Mirage-->>-EstadoJugando: proyectiles

    EstadoJugando->>+ColisionDetector: detectarProyectilEnemigo(proyectiles, enemigos, mirage)
    loop for (Proyectil p : proyectiles)
        ColisionDetector->>Proyectil: p.isActivo()
        loop for (Enemigo e : enemigos)
            ColisionDetector->>HarrierEnemigo: e.estaViva()
            ColisionDetector->>+Proyectil: p.getHitBox()
            Proyectil->>+HitBox: new HitBox(...)
            HitBox-->>-Proyectil: hitBoxP
            Proyectil-->>-ColisionDetector: hitBoxP
            ColisionDetector->>+HarrierEnemigo: e.getHitBox()
            HarrierEnemigo->>+HitBox: new HitBox(...)
            HitBox-->>-HarrierEnemigo: hitBoxE
            HarrierEnemigo-->>-ColisionDetector: hitBoxE
            ColisionDetector->>+HitBox: hitBoxP.colisionaCon(hitBoxE)
            HitBox-->>-ColisionDetector: hayColision
            alt colision
                ColisionDetector->>+Proyectil: p.getDanio()
                Proyectil-->>-ColisionDetector: danio
                ColisionDetector->>+HarrierEnemigo: e.recibirDanio(danio)
                deactivate HarrierEnemigo
                ColisionDetector->>+Proyectil: p.desactivar()
                deactivate Proyectil
                ColisionDetector->>+EstadisticasMirage: registrarDisparoAcertado()
                deactivate EstadisticasMirage
                ColisionDetector->>HarrierEnemigo: e.estaViva()
                alt enemigo destruido
                    ColisionDetector->>HarrierEnemigo: e.getPuntos()
                    HarrierEnemigo-->>ColisionDetector: puntos
                    ColisionDetector->>HarrierEnemigo: e.getTipo()
                    HarrierEnemigo-->>ColisionDetector: tipo
                    ColisionDetector->>+Mirage: sumarPuntos(puntos)
                    deactivate Mirage
                    ColisionDetector->>+EstadisticasMirage: registrarDerribo(tipo, puntos)
                    deactivate EstadisticasMirage
                end
            end
        end
    end
    deactivate ColisionDetector

    EstadoJugando->>GameController: enemigos.removeIf(!estaViva())
    EstadoJugando->>Mirage: getProyectiles().removeIf(!isActivo())
```

---

## UC-05: Enemigo impacta al Mirage

**Actor:** Sistema  
**Precondición:** `HarrierEnemigo` activo, `Mirage` no invencible  
**Postcondición:** Mirage pierde 1 vida, entra en invencibilidad temporal

```mermaid
sequenceDiagram
    participant EstadoJugando
    participant ColisionDetector
    participant HarrierEnemigo
    participant Mirage
    participant GameController

    EstadoJugando->>+ColisionDetector: detectarEnemigoMirage(enemigos, mirage)
    loop por cada enemigo activo (corta si mirage.isInvencible())
        ColisionDetector->>+HarrierEnemigo: getHitBox()
        deactivate HarrierEnemigo
        ColisionDetector->>+Mirage: getHitBox()
        deactivate Mirage
        ColisionDetector->>ColisionDetector: hitBoxE.colisionaCon(hitBoxM)?
        alt colisión
            ColisionDetector->>+Mirage: recibirDanio(1)
            Note over Mirage: vidas-- · invencible=true · frameInvencible=DURACION
            deactivate Mirage
        end
    end
    deactivate ColisionDetector

    Note over EstadoJugando,GameController: La transición a Game Over la decide EstadoJugando, que es quien tiene el controller (el ColisionDetector solo aplica daño)
    EstadoJugando->>+Mirage: estaViva()
    Mirage-->>-EstadoJugando: vidas > 0 ?
    alt !mirage.estaViva()
        EstadoJugando->>GameController: setEstado(new EstadoGameOver())
    end
```

---

## UC-06: Oleada de enemigos se activa

**Actor:** Sistema  
**Precondición:** `frameCounter >= intervaloFrames`  
**Postcondición:** N `HarrierEnemigo` spawnean en pantalla

```mermaid
sequenceDiagram
    participant EstadoJugando
    participant NivelMirage
    participant EnemySpawner
    participant HarrierEnemigo
    participant GameController

    EstadoJugando->>+NivelMirage: update()
    NivelMirage->>+EnemySpawner: update()
    EnemySpawner->>EnemySpawner: frameCounter++
    alt frameCounter >= intervaloFrames
        loop i = 0..tamanoOleada-1
            EnemySpawner->>+HarrierEnemigo: new HarrierEnemigo(randomX, -20, sketch)
            deactivate HarrierEnemigo
        end
        EnemySpawner->>EnemySpawner: frameCounter = 0
    end
    deactivate EnemySpawner
    NivelMirage->>+EnemySpawner: getEnemigosNuevos()
    deactivate EnemySpawner
    deactivate NivelMirage
    EstadoJugando->>GameController: enemigos.addAll(nuevos)
```

---

## UC-07: Game Over

**Actor:** Sistema  
**Precondición:** `mirage.getVidas() == 0`  
**Postcondición:** Estado = GAME_OVER, HOME notificado con evento FINALIZADO

```mermaid
sequenceDiagram
    participant GameController
    participant EstadoGameOver
    participant MirageModulo
    participant IModuloObserver

    GameController->>+EstadoGameOver: alEntrar(controller)
    EstadoGameOver->>+MirageModulo: notificar(FINALIZADO)
    loop por cada observer registrado
        MirageModulo->>+IModuloObserver: onEventoModulo(ModuloEvento(FINALIZADO))
        deactivate IModuloObserver
    end
    deactivate MirageModulo
    deactivate EstadoGameOver
```

---

## UC-08: Exportar estadísticas al HOME

**Actor:** Sistema / HOME  
**Precondición:** partida finalizada (Estado = GAME_OVER)  
**Postcondición:** `EstadisticasGenerales` disponible para el HOME

```mermaid
sequenceDiagram
    participant HomeJuego
    participant MirageModulo
    participant EstadisticasMirage
    participant Mirage
    participant EstadisticasGenerales

    HomeJuego->>+MirageModulo: getEstadisticasGenerales()
    MirageModulo->>+EstadisticasMirage: exportar(mirage.getVidas(), mirage)
    EstadisticasMirage->>+Mirage: getDisparosTotales()
    deactivate Mirage
    EstadisticasMirage->>EstadisticasMirage: calcular precision = acertados / totales
    EstadisticasMirage-->>-MirageModulo: datos internos
    MirageModulo->>+EstadisticasGenerales: new EstadisticasGenerales(nombreModulo, puntaje, ...)
    deactivate EstadisticasGenerales
    MirageModulo-->>-HomeJuego: EstadisticasGenerales
```
