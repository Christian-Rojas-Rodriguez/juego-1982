# Casos de Uso — MVP 1

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Módulo Mirage — primera versión entregable

---

## Diagrama de casos de uso

```mermaid
flowchart LR
    J["👤<br/>Jugador"]
    H["🏠<br/>HOME"]

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
| UC-01 | Mover el Mirage | Jugador | `HomeRunner`, `HomeJuego`, `ModuloMirage`, `GameController`, `EstadoJugando`, `InputHandler`, `MoverDerechaCmd`, `Mirage` |
| UC-02 | Disparar misil | Jugador | `HomeRunner`, `HomeJuego`, `ModuloMirage`, `GameController`, `InputHandler`, `DispararCmd`, `Mirage`, `Proyectil` |
| UC-03 | Pausar / Reanudar | Jugador | `HomeRunner`, `HomeJuego`, `ModuloMirage`, `GameController`, `EstadoJugando`, `EstadoPausado` |
| UC-04 | Proyectil destruye enemigo | Sistema | `EstadoJugando`, `GameController`, `ColisionDetector`, `Mirage`, `Proyectil`, `HarrierEnemigo`, `HitBox`, `Explosion`, `EstadisticasMirage` |
| UC-05 | Enemigo impacta al Mirage | Sistema | `EstadoJugando`, `ColisionDetector`, `HarrierEnemigo`, `HitBox`, `Mirage`, `GameController`, `EstadoGameOver` |
| UC-06 | Oleada de enemigos se activa | Sistema | `EstadoJugando`, `NivelMirage`, `EnemySpawner`, `EnemyFactory`, `HarrierEnemigo`, `GameController` |
| UC-07 | Game Over | Sistema | `GameController`, `EstadoGameOver`, `EstadisticasMirage`, `GameRenderer`, `PantallaGameOver` |
| UC-08 | Exportar estadísticas al HOME | Sistema / HOME | `HomeJuego`, `ModuloMirage`, `GameController`, `EstadisticasMirage`, `Mirage`, `ResumenPartida`, `EstadisticasGenerales` |

---

## UC-01: Mover el Mirage

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO, Mirage vivo  
**Postcondición:** posición del Mirage actualizada en el siguiente frame

```mermaid
sequenceDiagram
    actor Jugador
    participant ModuloMirage
    participant GameController
    participant EstadoJugando
    participant InputHandler
    participant MoverDerechaCmd
    participant Mirage

    Note over Jugador,ModuloMirage: keyPressed() en HomeRunner → HomeJuego.manejarTecla() → reenvía vía ModuloConInput
    Jugador->>+ModuloMirage: onKeyPressed(key, keyCode)
    Note over ModuloMirage: key == CODED, keyCode == PApplet.RIGHT
    ModuloMirage->>+GameController: onKeyPressed(key, keyCode)
    GameController->>EstadoJugando: onKeyPressed(this, key, keyCode)
    Note over EstadoJugando: solo reacciona a 'P' (pausa), ignora el movimiento
    GameController->>+InputHandler: onKeyPressed(keyCode, key, mirage)
    Note over InputHandler: el comando se busca por keyCode (PApplet.RIGHT)
    opt cmd != null
        InputHandler->>+MoverDerechaCmd: ejecutar(mirage)
        MoverDerechaCmd->>Mirage: setMoverDerecha(true)
        deactivate MoverDerechaCmd
    end
    deactivate InputHandler
    deactivate GameController
    deactivate ModuloMirage

    Note over Jugador,Mirage: En el siguiente draw() frame — HomeRunner.draw() → HomeJuego.dibujar() → ModuloMirage.actualizar(app)
    activate ModuloMirage
    ModuloMirage->>+GameController: update()
    GameController->>+EstadoJugando: update(this)
    EstadoJugando->>+Mirage: update()
    Mirage->>Mirage: x += velocidad (flag moverDerecha == true)
    Mirage->>Mirage: x = constrain(x, 0, sketch.width)
    Mirage->>Mirage: y = constrain(y, 0, sketch.height)
    deactivate Mirage
    deactivate EstadoJugando
    deactivate GameController
    deactivate ModuloMirage

    Note over Jugador,ModuloMirage: keyReleased() en HomeRunner → HomeJuego.manejarTeclaReleased() → reenvía vía ModuloConInput
    Jugador->>+ModuloMirage: onKeyReleased(key, keyCode)
    Note over ModuloMirage: key == CODED, keyCode == PApplet.RIGHT
    ModuloMirage->>+GameController: onKeyReleased(key, keyCode)
    GameController->>+InputHandler: onKeyReleased(keyCode, key, mirage)
    Note over InputHandler: el comando se busca por keyCode (PApplet.RIGHT)
    opt cmd != null
        InputHandler->>+MoverDerechaCmd: deshacer(mirage)
        MoverDerechaCmd->>Mirage: setMoverDerecha(false)
        deactivate MoverDerechaCmd
    end
    deactivate InputHandler
    deactivate GameController
    deactivate ModuloMirage

    Note over Jugador,Mirage: En el siguiente draw() frame — Mirage.update() ya no suma velocidad (flag moverDerecha == false)
```

---

## UC-02: Disparar misil

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO, `cooldownActual == 0`  
**Postcondición:** 1 `Proyectil` agregado a `mirage.proyectiles`

```mermaid
sequenceDiagram
    actor Jugador
    participant ModuloMirage
    participant GameController
    participant EstadoJugando
    participant InputHandler
    participant DispararCmd
    participant Mirage
    participant Proyectil

    Note over Jugador,ModuloMirage: keyPressed() en HomeRunner → HomeJuego.manejarTecla() → reenvía vía ModuloConInput
    Jugador->>+ModuloMirage: onKeyPressed(key, keyCode)
    Note over ModuloMirage: key == ' ', keyCode == 32 (SPACE)
    ModuloMirage->>+GameController: onKeyPressed(key, keyCode)
    GameController->>EstadoJugando: onKeyPressed(this, key, keyCode)
    Note over EstadoJugando: ignora SPACE (solo maneja 'P')
    GameController->>+InputHandler: onKeyPressed(keyCode, key, mirage)
    Note over InputHandler: el comando se busca por keyCode (32 = SPACE)
    opt cmd != null
        InputHandler->>+DispararCmd: ejecutar(mirage)
        DispararCmd->>+Mirage: disparar()
        opt cooldownActual == 0
            Mirage->>+Proyectil: new Proyectil(x, y - 20)
            Proyectil-->>-Mirage: proyectil
            Mirage->>Mirage: proyectiles.add(proyectil)
            Mirage->>Mirage: disparosTotales++
            Mirage->>Mirage: cooldownActual = cooldownDisparo
        end
        deactivate Mirage
        deactivate DispararCmd
    end
    deactivate InputHandler
    deactivate GameController
    deactivate ModuloMirage
```

---

## UC-03: Pausar / Reanudar

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO o PAUSADO  
**Postcondición:** estado cambia entre JUGANDO y PAUSADO

```mermaid
sequenceDiagram
    actor Jugador
    participant ModuloMirage
    participant GameController
    participant EstadoJugando
    participant EstadoPausado

    Note over Jugador,ModuloMirage: keyPressed() en HomeRunner → HomeJuego.manejarTecla() → reenvía vía ModuloConInput
    Jugador->>+ModuloMirage: onKeyPressed(key, keyCode)
    Note over ModuloMirage: key == 'P'
    ModuloMirage->>+GameController: onKeyPressed(key, keyCode)

    alt estadoActual == EstadoJugando (JUGANDO → PAUSADO)
        GameController->>+EstadoJugando: onKeyPressed(this, 'P', keyCode)
        EstadoJugando-->>GameController: setEstado(new EstadoPausado())
        deactivate EstadoJugando
        GameController->>+EstadoPausado: alEntrar(this)
        Note over EstadoPausado: alEntrar() está vacío
        deactivate EstadoPausado
        Note over EstadoPausado: el "congelar" (no actualiza, solo renderiza) vive en update()/render() de EstadoPausado
    else estadoActual == EstadoPausado (PAUSADO → JUGANDO, reanudar)
        GameController->>+EstadoPausado: onKeyPressed(this, 'P', keyCode)
        EstadoPausado-->>GameController: setEstado(new EstadoJugando())
        deactivate EstadoPausado
        GameController->>+EstadoJugando: alEntrar(this)
        deactivate EstadoJugando
    end

    Note over GameController: setEstado() asigna estadoActual y luego invoca alEntrar() en el nuevo estado
    GameController->>InputHandler: onKeyPressed(keyCode, key, mirage)
    Note over InputHandler: 'P' no está registrada como comando → no hace nada
    deactivate GameController
    deactivate ModuloMirage
```

---

## UC-04: Proyectil destruye enemigo

**Actor:** Sistema  
**Precondición:** `Proyectil` activo y `HarrierEnemigo` activo en pantalla  
**Postcondición:** enemigo destruido, puntos sumados, disparo acertado registrado

```mermaid
sequenceDiagram
    participant GameController
    participant EstadoJugando
    participant ColisionDetector
    participant Mirage
    participant Proyectil
    participant HarrierEnemigo
    participant HitBox
    participant Explosion
    participant EstadisticasMirage

    activate GameController
    GameController->>+EstadoJugando: update(this)
    Note over EstadoJugando,GameController: EstadoJugando usa el GameController como contexto del State
    Note over EstadoJugando: Obtiene del contexto colisionDetector, mirage, proyectiles y enemigos

    EstadoJugando->>+ColisionDetector: detectarProyectilEnemigo(proyectiles, enemigos, mirage)
    loop for (Proyectil p : proyectiles)
        opt !p.isActivo()    
            Note over ColisionDetector, Proyectil: Se omite el proyectil (continue)
        end
        loop for (Enemigo e : enemigos) 
            opt !e.estaViva()
                Note over ColisionDetector, HarrierEnemigo: Si omite el enemigo (continue)
            end
            ColisionDetector->>+Proyectil: p.getHitBox()
            Proyectil->>+HitBox: new HitBox(...)
            HitBox-->>-Proyectil: hitBoxP
            Proyectil-->>-ColisionDetector: hitBoxP
            ColisionDetector->>+HarrierEnemigo: e.getHitBox()
            HarrierEnemigo->>+HitBox: new HitBox(...)
            HitBox-->>-HarrierEnemigo: hitBoxE
            HarrierEnemigo-->>-ColisionDetector: hitBoxE
            ColisionDetector->>+HitBox: hitBoxP.colisionaCon(hitBoxE)
            HitBox-->>-ColisionDetector: hayColision: boolean
            opt hayColision
                ColisionDetector->>+Proyectil: p.getDanio()
                Proyectil-->>-ColisionDetector: danio
                ColisionDetector->>+HarrierEnemigo: e.recibirDanio(danio)
                deactivate HarrierEnemigo
                ColisionDetector->>+Proyectil: p.desactivar()
                deactivate Proyectil
                ColisionDetector->>+EstadisticasMirage: registrarDisparoAcertado()
                deactivate EstadisticasMirage
                opt !e.estaViva()
                    ColisionDetector->>HarrierEnemigo: e.getPuntos()
                    HarrierEnemigo-->>ColisionDetector: puntos
                    ColisionDetector->>+Mirage: sumarPuntos(puntos)
                    deactivate Mirage
                    ColisionDetector->>HarrierEnemigo: e.getTipo()
                    HarrierEnemigo-->>ColisionDetector: tipo
                    ColisionDetector->>+EstadisticasMirage: registrarDerribo(tipo, puntos)
                    deactivate EstadisticasMirage
                end
                Note over ColisionDetector: break (corta el loop de enemigos para este proyectil)
            end
        end
    end
    deactivate ColisionDetector

    Note over EstadoJugando,Explosion: De vuelta en EstadoJugando.update(), tras detectar colisiones
    loop for (Enemigo e : enemigos)
        opt !e.estaViva()
            EstadoJugando->>+Explosion: new Explosion(e.getX(), e.getY())
            deactivate Explosion
            EstadoJugando-->>GameController: getEfectos().add(explosion)
        end
    end
    EstadoJugando-->>GameController: getEnemigos().removeIf(enemigo -> !enemigo.estaViva())
    EstadoJugando->>Mirage: getProyectiles().removeIf(proyectil -> !proyectil.isActivo())
    deactivate EstadoJugando
    deactivate GameController
```

---

## UC-05: Enemigo impacta al Mirage

**Actor:** Sistema  
**Precondición:** `HarrierEnemigo` activo, `Mirage` no invencible  
**Postcondición:** Mirage pierde 1 vida, entra en invencibilidad temporal

```mermaid
sequenceDiagram
    participant GameController
    participant EstadoJugando
    participant ColisionDetector
    participant HarrierEnemigo
    participant HitBox
    participant Mirage

    activate GameController
    GameController->>+EstadoJugando: update(this)
    Note over EstadoJugando: EstadoJugando usa el GameController como contexto del State
    EstadoJugando->>+ColisionDetector: detectarEnemigoMirage(enemigos, mirage)
    loop for (Enemigo e : enemigos)
        opt !e.estaViva()
            Note over ColisionDetector, Mirage: continue, se omite este enemigo
        end
        ColisionDetector->>+Mirage: isInvencible()
        Mirage-->>-ColisionDetector: invencible: boolean
        opt invencible
            Note over ColisionDetector, Mirage: return, aborta detectarEnemigoMirage()
        end
        ColisionDetector->>+HarrierEnemigo: e.getHitBox()
        HarrierEnemigo->>+HitBox: new HitBox(...)
        HitBox-->>-HarrierEnemigo: hitBoxE
        HarrierEnemigo-->>-ColisionDetector: hitBoxE
        ColisionDetector->>+Mirage: getHitBox()
        Mirage->>+HitBox: new HitBox(...)
        HitBox-->>-Mirage: hitBoxM
        Mirage-->>-ColisionDetector: hitBoxM
        ColisionDetector->>+HitBox: hitBoxE.colisionaCon(hitBoxM)
        HitBox-->>-ColisionDetector: hayColision: boolean
        opt hayColision
            ColisionDetector->>+Mirage: recibirDanio(1)
            Note over Mirage: vidas-- · invencible=true · frameInvencible=DURACION_INVENCIBILIDAD
            deactivate Mirage
        end
    end
    deactivate ColisionDetector

    Note over GameController, Mirage: La transición a Game Over la decide EstadoJugando, que es quien tiene el controller (el ColisionDetector solo aplica daño)
    EstadoJugando->>+Mirage: estaViva()
    Mirage-->>-EstadoJugando: vidas > 0 : boolean
    opt !mirage.estaViva()
        EstadoJugando-->>GameController: setEstado(new EstadoGameOver())
    end
    deactivate EstadoJugando
    deactivate GameController
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
    participant EnemyFactory
    participant HarrierEnemigo
    participant GameController

    EstadoJugando->>+NivelMirage: update()
    NivelMirage->>+EnemySpawner: update()
    EnemySpawner->>EnemySpawner: frameCounter++
    opt frameCounter >= intervaloFrames
        loop i = 0..tamanoOleada-1
            EnemySpawner->>EnemySpawner: x = sketch.random(20, sketch.width - 20)
            EnemySpawner->>+EnemyFactory: crear(Tipo.HARRIER, sketch, x, -20)
            EnemyFactory->>+HarrierEnemigo: new HarrierEnemigo(sketch, x, -20)
            HarrierEnemigo-->>-EnemyFactory: enemigo: Enemigo
            EnemyFactory-->>-EnemySpawner: enemigo
            EnemySpawner->>EnemySpawner: nuevosEsteFrame.add(enemigo)
        end
        EnemySpawner->>EnemySpawner: frameCounter = 0
    end
    deactivate EnemySpawner
    deactivate NivelMirage

    Note over EstadoJugando,GameController: getEnemigosNuevos() es una 2da llamada separada desde EstadoJugando.update()
    EstadoJugando->>+NivelMirage: getEnemigosNuevos()
    NivelMirage->>+EnemySpawner: getEnemigosNuevos()
    Note over EnemySpawner: retorna copia del buffer y lo limpia
    EnemySpawner-->>-NivelMirage: nuevos
    NivelMirage-->>-EstadoJugando: nuevos
    EstadoJugando->>+GameController: getEnemigos().addAll(nuevos)
```

---

## UC-07: Game Over

**Actor:** Sistema / Jugador  
**Precondición:** `mirage.getVidas() == 0`  
**Postcondición:** Estado = `EstadoGameOver`, pantalla de Game Over visible esperando input. Según la tecla: **R** reinicia la partida (`EstadoJugando`) · **ESC** notifica al HOME con evento FINALIZADO.

```mermaid
sequenceDiagram
    actor Jugador
    participant GameController
    participant EstadoGameOver
    participant Mirage
    participant EstadisticasMirage
    participant GameRenderer
    participant PantallaGameOver

    GameController->>+EstadoGameOver: alEntrar(controller)
    EstadoGameOver->>+Mirage: getPuntuacion()
    Mirage-->>-EstadoGameOver: puntaje
    EstadoGameOver->>+EstadisticasMirage: getEnemigosDerribados()
    EstadisticasMirage-->>-EstadoGameOver: derribados
    EstadoGameOver->>+EstadisticasMirage: registrarFinPartida(puntaje)
    Note over EstadisticasMirage: partidasJugadas++ · partidasPerdidas++ · actualiza puntajeMaximo
    deactivate EstadisticasMirage

    EstadoGameOver->>+GameRenderer: setPantalla(new PantallaGameOver(puntaje, derribados))
    GameRenderer->>+PantallaGameOver: new PantallaGameOver(puntaje, derribados)
    PantallaGameOver-->>-GameRenderer: pantalla
    deactivate GameRenderer
    deactivate EstadoGameOver

    Note over Jugador, PantallaGameOver: La pantalla queda visible (update() no avanza el juego) hasta que el jugador presiona una tecla

    alt Jugador presiona R
        Jugador->>+EstadoGameOver: onKeyPressed(controller, 'R')
        EstadoGameOver->>GameController: init()
        Note over GameController: Reconstruye Mirage, enemigos, estadísticas, etc. y setEstado(new EstadoJugando())
        deactivate EstadoGameOver
    else Jugador presiona ESC
        Jugador->>+EstadoGameOver: onKeyPressed(controller, ESC)
        EstadoGameOver->>GameController: getMirageModulo().finalizar()
        Note over GameController: El HOME recibe el evento FINALIZADO y vuelve al lobby
        deactivate EstadoGameOver
    end
```

---

## UC-08: Exportar estadísticas al HOME

**Actor:** Sistema / HOME  
**Precondición:** partida finalizada (Estado = GAME_OVER)  
**Postcondición:** `EstadisticasGenerales` disponible para el HOME

```mermaid
sequenceDiagram
    participant HomeJuego
    participant ModuloMirage
    participant GameController
    participant EstadisticasMirage
    participant Mirage
    participant ResumenPartida
    participant EstadisticasGenerales

    HomeJuego->>+ModuloMirage: getEstadisticasGenerales()
    alt controller != null && controller.getMirage() != null
        ModuloMirage->>+GameController: getEstadisticas()
        GameController-->>-ModuloMirage: estadisticas
        ModuloMirage->>+GameController: getMirage()
        GameController-->>-ModuloMirage: mirage
        ModuloMirage->>+GameController: getMirage().getVidas()
        GameController-->>-ModuloMirage: vidas

        ModuloMirage->>+EstadisticasMirage: exportar(vidas, mirage)
        EstadisticasMirage->>+Mirage: getPuntuacion()
        Mirage-->>-EstadisticasMirage: puntuacion
        EstadisticasMirage->>EstadisticasMirage: getPrecision(mirage)
        EstadisticasMirage->>+Mirage: getDisparosTotales()
        Mirage-->>-EstadisticasMirage: disparosTotales
        EstadisticasMirage->>+ResumenPartida: new ResumenPartida(puntuacion, derribados, vidas, duracion, precision, ...)
        ResumenPartida-->>-EstadisticasMirage: resumen
        EstadisticasMirage-->>-ModuloMirage: resumen: ResumenPartida

        ModuloMirage->>+EstadisticasGenerales: new EstadisticasGenerales("Mirage", r.getPuntajeFinal(), ...)
        EstadisticasGenerales-->>-ModuloMirage: stats
    else fallback (game loop aún no creado)
        ModuloMirage->>+EstadisticasGenerales: new EstadisticasGenerales("Mirage", puntaje, 0, 0, 0, 0, enJuegoMs / 1000)
        EstadisticasGenerales-->>-ModuloMirage: stats
    end
    ModuloMirage-->>-HomeJuego: EstadisticasGenerales
```
