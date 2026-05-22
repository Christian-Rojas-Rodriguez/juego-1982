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
    J((Jugador))
    H((HOME))

    J --> UC1[UC-01: Mover el Mirage]
    J --> UC2[UC-02: Disparar misil]
    J --> UC3[UC-03: Pausar el juego]
    J --> UC4[UC-04: Reiniciar partida]

    UC2 --> UC5[UC-05: Proyectil destruye enemigo y suma puntuación]

    UC7[UC-07: Enemigo impacta al Mirage] --> UC8[UC-08: Perder vida]
    UC8 -->|si vidas == 0| UC9[UC-09: Fin de partida]
    UC5 -->|si completa objetivo del nivel| UC9

    UC9 --> H

    style UC1 fill:#d4edda
    style UC2 fill:#d4edda
    style UC5 fill:#d4edda
    style UC7 fill:#ffeeba
    style UC9 fill:#f8d7da
```

---

## UC-01: Mover el Mirage

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO, Mirage vivo  
**Postcondición:** Posición del Mirage actualizada dentro de los límites de pantalla

```mermaid
sequenceDiagram
    actor Jugador
    participant HOME
    participant MirageModulo
    participant GameController
    participant EstadoJugando
    participant InputHandler
    participant MoverCmd as MoverXxxCmd
    participant Mirage

    Jugador->>HOME: presiona tecla
    HOME->>MirageModulo: onKeyPressed(key, keyCode)
    MirageModulo->>GameController: onKeyPressed(keyCode)
    GameController->>EstadoJugando: onKeyPressed(controller, keyCode)
    EstadoJugando->>InputHandler: onKeyPressed(keyCode, controller)
    InputHandler->>MoverCmd: ejecutar(controller)
    MoverCmd->>GameController: getMirage()
    GameController-->>MoverCmd: mirage
    MoverCmd->>Mirage: setMoverIzquierda(true) / etc.
    loop cada frame
        HOME->>MirageModulo: update()
        MirageModulo->>GameController: update()
        GameController->>EstadoJugando: update(controller)
        EstadoJugando->>Mirage: update()
    end
    Note over Mirage: update() actualiza x,y con constrain()
```

---

## UC-02: Disparar misil

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO, Mirage vivo  
**Postcondición:** Nuevo `Proyectil` agregado a la lista de proyectiles activos

```mermaid
sequenceDiagram
    actor Jugador
    participant HOME
    participant MirageModulo
    participant GameController
    participant EstadoJugando
    participant InputHandler
    participant DispararCmd
    participant Mirage
    participant Proyectil

    Jugador->>HOME: presiona SPACE
    HOME->>MirageModulo: onKeyPressed(key, SPACE)
    MirageModulo->>GameController: onKeyPressed(SPACE)
    GameController->>EstadoJugando: onKeyPressed(controller, SPACE)
    EstadoJugando->>InputHandler: onKeyPressed(SPACE, controller)
    InputHandler->>DispararCmd: ejecutar(controller)
    DispararCmd->>GameController: getMirage()
    GameController-->>DispararCmd: mirage
    DispararCmd->>Mirage: disparar()
    Mirage-->>Proyectil: new Proyectil(x, y, sketch)
    Mirage-->>DispararCmd: proyectil
    DispararCmd->>GameController: agregarProyectil(proyectil)
```

---

## UC-05: Proyectil destruye enemigo y suma puntuación

**Actor:** Sistema  
**Precondición:** Proyectil activo y Enemigo activo en pantalla  
**Postcondición:** Enemigo destruido, puntos sumados al Mirage

```mermaid
sequenceDiagram
    participant GameController
    participant ColisionDetector
    participant Proyectil
    participant Enemigo
    participant HitBox
    participant Mirage
    participant EstadisticasMirage

    GameController->>ColisionDetector: detectarProyectilEnemigo(proyectiles, enemigos, mirage)
    loop por cada par proyectil-enemigo
        ColisionDetector->>Proyectil: getHitBox()
        ColisionDetector->>Enemigo: getHitBox()
        ColisionDetector->>HitBox: colisionaCon(hitboxB)
        alt colisión detectada
            ColisionDetector->>Enemigo: recibirDanio(proyectil.getDanio())
            ColisionDetector->>Proyectil: desactivar()
            alt enemigo.vida <= 0
                ColisionDetector->>Mirage: sumarPuntos(enemigo.getPuntos())
                ColisionDetector->>EstadisticasMirage: registrarDerribo(puntos)
            end
        end
    end
    GameController->>GameController: limpiarEntidadesInactivas()
```

---

## UC-07/UC-08: Enemigo impacta al Mirage y pierde vida

**Actor:** Sistema  
**Precondición:** Enemigo activo colisiona con la HitBox del Mirage, Mirage no invencible  
**Postcondición:** Mirage pierde una vida y entra en estado invencible

```mermaid
sequenceDiagram
    participant GameController
    participant EstadoJugando
    participant ColisionDetector
    participant Enemigo
    participant HitBox
    participant Mirage
    participant EstadoGameOver

    GameController->>EstadoJugando: update(controller)
    EstadoJugando->>ColisionDetector: detectarEnemigoMirage(enemigos, mirage)
    loop por cada enemigo
        ColisionDetector->>Mirage: isInvencible()
        alt Mirage NO es invencible
            ColisionDetector->>Mirage: getHitBox()
            ColisionDetector->>Enemigo: getHitBox()
            ColisionDetector->>HitBox: colisionaCon(hitBoxEnemigo)
            alt colisión detectada
                ColisionDetector->>Mirage: recibirDanio(1)
                Note over Mirage: vidas--<br/>activa invencibilidad temporal
            end
        end
    end
    EstadoJugando->>Mirage: estaViva()
    alt Mirage sin vidas
        EstadoJugando->>GameController: setEstado(new EstadoGameOver())
    end
```

---

## UC-09: Fin de partida

**Actor:** Sistema  
**Precondición:** Mirage sin vidas o nivel completado  
**Postcondición:** Estadísticas guardadas, pantalla de cierre visible, HOME puede solicitar resumen

```mermaid
sequenceDiagram
    participant GameController
    participant EstadoGameOver
    participant NivelMirage
    participant EstadisticasMirage
    participant PantallaGameOver
    participant GameRenderer
    participant Mirage
    participant MirageModulo
    participant HOME as HOME (Lobby)

    alt Derrota: Mirage sin vidas
        GameController->>EstadoGameOver: alEntrar(controller)
        EstadoGameOver->>GameController: getMirage()
        GameController-->>EstadoGameOver: mirage
        EstadoGameOver->>Mirage: getPuntuacion()
    else Victoria: nivel completado
        GameController->>NivelMirage: isTerminado()
        GameController->>Mirage: getPuntuacion()
    end

    GameController->>EstadisticasMirage: registrarFinPartida(puntajeFinal)
    EstadisticasMirage->>EstadisticasMirage: guardar() en archivo CSV/JSON
    GameController->>PantallaGameOver: new PantallaGameOver(puntajeFinal, enemigosDerribados)
    GameController->>GameRenderer: setPantalla(pantallaGameOver)

    loop cada frame
        GameController->>GameRenderer: render(...)
    end

    alt Jugador confirma volver al HOME
        GameController->>GameController: finalizarModulo()
        HOME->>MirageModulo: getResumen()
        MirageModulo->>GameController: getEstadisticas()
        MirageModulo->>EstadisticasMirage: exportar()
        EstadisticasMirage-->>MirageModulo: ResumenPartida
        MirageModulo-->>HOME: ResumenPartida
    end
```

---

## Resumen de casos de uso

| ID | Caso de uso | Clases principales involucradas |
|----|-------------|--------------------------------|
| UC-01 | Mover el Mirage | `InputHandler`, `MoverXxxCmd`, `Mirage` |
| UC-02 | Disparar misil | `InputHandler`, `DispararCmd`, `Mirage`, `Proyectil` |
| UC-03 | Pausar el juego | `GameController`, `EstadoPausado` |
| UC-04 | Reiniciar partida | `GameController`, `EstadoGameOver`, `EstadoJugando` |
| UC-05 | Proyectil destruye enemigo y suma puntuación | `ColisionDetector`, `HitBox`, `Proyectil`, `Enemigo`, `Mirage`, `EstadisticasMirage` |
| UC-07 | Enemigo impacta al Mirage | `ColisionDetector`, `HitBox`, `Mirage` |
| UC-08 | Perder vida | `Mirage`, `EstadoJugando`, `EstadoGameOver` |
| UC-09 | Fin de partida (victoria o derrota) | `EstadoGameOver`, `NivelMirage`, `EstadisticasMirage`, `PantallaGameOver`, `MirageModulo`, `HOME` |
