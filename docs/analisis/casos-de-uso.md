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

    UC2 --> UC5[UC-05: Proyectil destruye enemigo]
    UC5 --> UC6[UC-06: Sumar puntuación]

    UC7[UC-07: Enemigo impacta al Mirage] --> UC8[UC-08: Perder vida]
    UC8 --> UC9[UC-09: Fin de partida - Game Over]

    UC9 --> H
    UC10[UC-10: Nivel completado] --> H

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
    participant Juego1982
    participant GameController
    participant InputHandler
    participant MoverCmd as MoverXxxCmd
    participant Mirage

    Jugador->>Juego1982: keyPressed(keyCode)
    Juego1982->>GameController: onKeyPressed(keyCode)
    GameController->>InputHandler: onKeyPressed(keyCode, mirage)
    InputHandler->>MoverCmd: ejecutar(mirage)
    MoverCmd->>Mirage: moverIzquierda() / moverDerecha() / etc.
    Mirage->>Mirage: actualizar x,y con constrain()
    Note over Mirage: x = constrain(x, 0, width)<br/>y = constrain(y, 0, height)
```

---

## UC-02: Disparar misil

**Actor:** Jugador  
**Precondición:** Estado = JUGANDO, Mirage vivo  
**Postcondición:** Nuevo `Proyectil` agregado a la lista de proyectiles activos

```mermaid
sequenceDiagram
    actor Jugador
    participant Juego1982
    participant GameController
    participant InputHandler
    participant DispararCmd
    participant Mirage
    participant Proyectil

    Jugador->>Juego1982: keyPressed(SPACE)
    Juego1982->>GameController: onKeyPressed(SPACE)
    GameController->>InputHandler: onKeyPressed(SPACE, mirage)
    InputHandler->>DispararCmd: ejecutar(mirage)
    DispararCmd->>Mirage: disparar()
    Mirage-->>Proyectil: new Proyectil(x, y)
    Mirage-->>DispararCmd: proyectil
    DispararCmd->>GameController: proyectiles.add(proyectil)
```

---

## UC-03: Proyectil destruye enemigo

**Actor:** Sistema  
**Precondición:** Proyectil activo y Enemigo activo en pantalla  
**Postcondición:** Enemigo destruido, puntos sumados al Mirage

```mermaid
sequenceDiagram
    participant GameController
    participant ColisionDetector
    participant Proyectil
    participant Enemigo
    participant Mirage
    participant EstadisticasMirage

    GameController->>ColisionDetector: detectarProyectilEnemigo(proyectiles, enemigos)
    loop por cada par proyectil-enemigo
        ColisionDetector->>Proyectil: getHitBox()
        ColisionDetector->>Enemigo: getHitBox()
        ColisionDetector->>ColisionDetector: hitboxA.colisionaCon(hitboxB)
        alt colisión detectada
            ColisionDetector->>Enemigo: recibirDanio(proyectil.getDanio())
            ColisionDetector->>Proyectil: desactivar()
            alt enemigo.vida <= 0
                ColisionDetector->>Mirage: sumarPuntos(enemigo.getPuntos())
                ColisionDetector->>EstadisticasMirage: registrarDerribo(puntos)
            end
        end
    end
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
    participant EstadoGameOver

    GameController->>ColisionDetector: detectarEnemigoMirage(enemigos, mirage)
    loop por cada enemigo
        ColisionDetector->>Mirage: estaInvencible()
        alt Mirage NO es invencible
            ColisionDetector->>Mirage: getHitBox()
            ColisionDetector->>Enemigo: getHitBox()
            ColisionDetector->>ColisionDetector: colisionaCon()
            alt colisión detectada
                ColisionDetector->>Mirage: recibirDanio(1)
                Note over Mirage: vidas--<br/>activa invencibilidad temporal
                alt vidas == 0
                    GameController->>GameController: setEstado(new EstadoGameOver())
                    GameController->>EstadisticasMirage: registrarFinPartida(puntuacion)
                end
            end
        end
    end
```

---

## UC-05: Fin de partida (Game Over)

**Actor:** Sistema  
**Precondición:** Mirage sin vidas  
**Postcondición:** Estadísticas guardadas, pantalla Game Over visible, HOME puede solicitar resumen

```mermaid
sequenceDiagram
    participant GameController
    participant EstadoGameOver
    participant EstadisticasMirage
    participant PantallaGameOver
    participant GameRenderer
    participant MirageModulo
    participant HOME as HOME (Lobby)

    GameController->>EstadoGameOver: alEntrar(controller)
    EstadoGameOver->>EstadisticasMirage: registrarFinPartida(puntajeFinal)
    EstadisticasMirage->>EstadisticasMirage: guardar() en archivo CSV/JSON
    EstadoGameOver->>PantallaGameOver: new PantallaGameOver(puntaje)
    EstadoGameOver->>GameRenderer: setPantalla(pantallaGameOver)

    loop cada frame
        GameController->>EstadoGameOver: update(controller)
        GameController->>EstadoGameOver: render(controller)
        EstadoGameOver->>GameRenderer: render(...)
    end

    alt Jugador presiona ESC
        EstadoGameOver->>GameController: señal de finalizar módulo
        HOME->>MirageModulo: getResumen()
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
| UC-03 | Proyectil destruye enemigo | `ColisionDetector`, `HitBox`, `Proyectil`, `Enemigo`, `Mirage` |
| UC-04 | Enemigo impacta al Mirage | `ColisionDetector`, `HitBox`, `Mirage`, `EstadoGameOver` |
| UC-05 | Fin de partida (Game Over) | `EstadoGameOver`, `EstadisticasMirage`, `PantallaGameOver`, `MirageModulo` |
| UC-06 | Pausar el juego | `GameController`, `EstadoPausado` |
| UC-07 | Nivel completado | `NivelMirage`, `EstadisticasMirage`, `MirageModulo`, `HOME` |
