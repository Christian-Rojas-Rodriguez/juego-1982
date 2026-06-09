# Diagramas de Estado — Módulo Mirage

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Vista de comportamiento dinámico del sistema

---

## 1. Estado del Juego (máquina principal)

Gestiona el flujo de pantallas y controla qué se ejecuta en cada frame.
Implementado con el **patrón State** en `GameController`.

```mermaid
stateDiagram-v2
    [*] --> JUGANDO : iniciar()

    JUGANDO --> PAUSADO : tecla P
    PAUSADO --> JUGANDO : tecla P

    JUGANDO --> GAME_OVER : vidasMirage == 0
    GAME_OVER --> JUGANDO : tecla R (reiniciar)
    GAME_OVER --> [*] : tecla ESC → HomeFacade.enviarResumen()

    JUGANDO --> NIVEL_COMPLETADO : jefesVencidos == 4
    NIVEL_COMPLETADO --> JUGANDO : timerFrames >= DURACION (nuevo nivel)

    note right of JUGANDO
        update: mover entidades, detectar colisiones,
        actualizar spawner, registrar heatmap
    end note

    note right of PAUSADO
        Solo renderiza.
        No actualiza física ni lógica.
    end note

    note right of GAME_OVER
        Muestra puntaje y nivel alcanzado.
        Llama HomeFacade.enviarResumen().
    end note

    note right of NIVEL_COMPLETADO
        Pantalla de transición.
        ConfiguradorDificultad escala al siguiente nivel.
        HomeFacade.notificarNivelCompletado().
    end note
```

**Clases involucradas:**

| Estado | Clase Java |
|--------|-----------|
| JUGANDO | `EstadoJugando` |
| PAUSADO | `EstadoPausado` |
| GAME_OVER | `EstadoGameOver` |
| NIVEL_COMPLETADO | `EstadoNivelCompletado` |
| Transiciones | `GameController.setEstado(EstadoJuego)` |

---

## 2. Estado del Mirage (jugador)

Después de recibir daño, el Mirage entra en **invencibilidad temporal**
para evitar pérdidas de vida en cadena.

```mermaid
stateDiagram-v2
    [*] --> NORMAL : Mirage creado (vidas = 3)

    NORMAL --> INVENCIBLE : recibirDanio() && vidas > 0
    INVENCIBLE --> NORMAL : frameInvencible >= DURACION_INV

    NORMAL --> MUERTO : recibirDanio() && vidas == 0

    MUERTO --> [*] : GameController detecta !estaViva()

    note right of NORMAL
        Colisiones activas.
        Renderiza normalmente.
        Puede disparar y recoger powerups.
    end note

    note right of INVENCIBLE
        Ignora colisiones con enemigos.
        Renderiza parpadeando (blink).
        Cuenta frameInvencible hasta DURACION_INVENCIBILIDAD.
    end note

    note right of MUERTO
        vidas == 0.
        GameController transiciona a EstadoGameOver.
    end note
```

**Implementación en `Mirage.java`:**
- Campo `boolean invencible`
- Campo `int frameInvencible`
- Constante `int DURACION_INVENCIBILIDAD` (120 frames = 2 seg a 60 fps)
- Constante `int VIDAS_MAX = 3` (no se excede nunca)

---

## 3. Estado de un Enemigo / Jefe

Cada enemigo (incluidos los Jefes) tiene su propio ciclo de vida gestionado por `GameController`.
Los Jefes además tienen un estado interno de **fases**.

```mermaid
stateDiagram-v2
    [*] --> ACTIVO : EntidadFactory crea enemigo/jefe

    ACTIVO --> DESTRUIDO : vida <= 0
    ACTIVO --> FUERA_DE_PANTALLA : getY() > altura + margen

    DESTRUIDO --> [*] : GameController lo elimina (suma puntos)
    FUERA_DE_PANTALLA --> [*] : GameController lo elimina (sin puntos)

    state ACTIVO {
        [*] --> FASE_1
        FASE_1 --> FASE_2 : vida <= umbralFase2
        FASE_2 --> FASE_3 : vida <= umbralFase3
        FASE_3 --> [*]

        note right of FASE_1
            Solo aplica a Jefes (Jefe.avanzarFase()).
            Enemigos normales no tienen fases.
        end note
    }

    note right of ACTIVO
        Ejecuta moverIA() cada frame.
        HitBox activa.
        FragataEnemiga puede disparar.
        EnemigoKamikaze carga si mirage.y > umbralY.
        Jefe ejecuta ataque especial cada intervaloAtaque frames.
    end note

    note right of DESTRUIDO
        estaViva() == false.
        NivelMirage.registrarJefeVencido() si era Jefe.
    end note
```

---

## 4. Estado de un Powerup

```mermaid
stateDiagram-v2
    [*] --> CAYENDO : PowerupManager crea al cruzar umbral de puntaje

    CAYENDO --> RECOGIDO : ColisionDetector detecta colisión con Mirage
    CAYENDO --> PERDIDO : getY() > altura + margen

    RECOGIDO --> [*] : Mirage.aplicarPowerup(tipo, duracion)
    PERDIDO  --> [*] : GameController limpia lista

    note right of CAYENDO
        Desciende pantalla.
        HitBox activa.
        Se renderiza con ícono del tipo (TipoPowerup).
    end note

    note right of RECOGIDO
        Efecto aplicado inmediatamente en Mirage.
        VELOCIDAD_MOVIMIENTO / VELOCIDAD_DISPARO / DISPARO_DOBLE.
        Mirage cuenta framesPowerupRestantes hasta revertir.
    end note
```

---

## 5. Estado de un Proyectil

Los proyectiles los crea `Mirage.disparar()` y los gestiona `GameController`.

```mermaid
stateDiagram-v2
    [*] --> ACTIVO : Mirage.disparar() — 1 o 2 proyectiles según TipoPowerup

    ACTIVO --> IMPACTADO : ColisionDetector detecta colisión con Enemigo/Jefe
    ACTIVO --> FUERA_DE_PANTALLA : getY() < 0

    IMPACTADO --> [*] : desactivar() — GameController limpia lista
    FUERA_DE_PANTALLA --> [*] : GameController limpia lista

    note right of ACTIVO
        Se mueve hacia arriba cada frame.
        HitBox activa.
        danio aplicado al impactar.
        Si TipoPowerup.DISPARO_DOBLE → 2 Proyectil offset ±10px.
    end note
```

---

## Resumen: quién gestiona cada estado

| Ciclo de vida | Gestionado por |
|---------------|---------------|
| Estado del juego (JUGANDO/PAUSADO/GAME_OVER/NIVEL_COMPLETADO) | `GameController` con patrón **State** |
| Estado del Mirage (NORMAL/INVENCIBLE/MUERTO) | Campo interno en `Mirage.update()` |
| Estado del Enemigo/Jefe (ACTIVO/DESTRUIDO/FASES) | `estaViva()` + `NivelMirage` + `GameController` |
| Estado del Powerup (CAYENDO/RECOGIDO/PERDIDO) | `PowerupManager` + `ColisionDetector` + `GameController` |
| Estado del Proyectil (ACTIVO/IMPACTADO) | `isActivo()` + `GameController` limpia la lista |