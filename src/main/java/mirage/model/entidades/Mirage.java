package mirage.model.entidades;

import mirage.model.fisica.HitBox;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * El avión Mirage controlado por el jugador.
 *
 * Es el objeto más referenciado del sistema: GameController lo actualiza,
 * ColisionDetector lo consulta, Commands le setean flags de movimiento.
 *
 * Movimiento con flags booleanos: keyPressed activa el flag, keyReleased lo baja.
 * update() lee los flags cada frame → movimiento suave, independiente del OS key-repeat.
 *
 * Dueño de sus proyectiles: disparar() los agrega a la lista interna.
 * GameController accede via getProyectiles() para colisionar y limpiar.
 * disparosTotales se incrementa aquí; EstadisticasMirage lo consulta al exportar.
 *
 * Patrón: Information Expert (posee proyectiles y contador de disparos)
 */
public class Mirage extends Nave {

    // ── Constantes ───────────────────────────────────────────────────────────
    private static final int DURACION_INVENCIBILIDAD = 120; // frames (2 seg a 60 fps)
    private static final int VIDAS_MAX               = 3;
    private static final int ANCHO_HITBOX            = 30;
    private static final int ALTO_HITBOX             = 30;
    private static final int COOLDOWN_BASE           = 15;  // frames entre disparos

    // ── Estado del jugador ───────────────────────────────────────────────────
    private int vidas;
    private int puntuacion;

    // ── Invencibilidad tras recibir daño ─────────────────────────────────────
    private boolean invencible;
    private int frameInvencible;

    // ── Cooldown de disparo ──────────────────────────────────────────────────
    private int cooldownDisparo;
    private int cooldownActual;

    // ── Flags de movimiento (activados/desactivados por Commands) ────────────
    private boolean moverIzquierda;
    private boolean moverDerecha;
    private boolean moverArriba;
    private boolean moverAbajo;

    // ── Proyectiles propios ──────────────────────────────────────────────────
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private int disparosTotales;

    private final PApplet sketch;

    // ── Constructor ──────────────────────────────────────────────────────────

    public Mirage(PApplet sketch) {
        super(sketch.width / 2f, sketch.height - 100, 4f, VIDAS_MAX);
        this.sketch          = sketch;
        this.vidas           = VIDAS_MAX;
        this.puntuacion      = 0;
        this.cooldownDisparo = COOLDOWN_BASE;
        this.cooldownActual  = 0;
        this.disparosTotales = 0;
    }

    // ── Game loop ────────────────────────────────────────────────────────────

    @Override
    public void update() {
        if (moverIzquierda) x -= velocidad;
        if (moverDerecha)   x += velocidad;
        if (moverArriba)    y -= velocidad;
        if (moverAbajo)     y += velocidad;
        x = PApplet.constrain(x, 0, sketch.width);
        y = PApplet.constrain(y, 0, sketch.height);
        // TODO: if (cooldownActual > 0) cooldownActual--
        // TODO: if (invencible) { frameInvencible--; if (frameInvencible <= 0) invencible = false }
        // TODO: proyectiles.forEach(Proyectil::update)
        // TODO: proyectiles.removeIf(p -> !p.isActivo())
    }

    public void render(PApplet sk) {
        // TODO: si invencible y sk.frameCount % 10 < 5 → no dibujar (efecto parpadeo)
        // Nave simple: triángulo apuntando hacia arriba, centrado en (x, y).
        sk.fill(120, 200, 255);
        sk.triangle(x, y - 15, x - 12, y + 12, x + 12, y + 12);
        for (Proyectil p : proyectiles) p.render(sk);
    }

    // ── Disparo ──────────────────────────────────────────────────────────────

    /**
     * Crea un proyectil y lo agrega a la lista interna si el cooldown llegó a 0.
     * Llamado por DispararCmd.ejecutar(mirage) — el comando no conoce la lista.
     */
    public void disparar() {
        // TODO: if (cooldownActual > 0) return
        // TODO: proyectiles.add(new Proyectil(x, y - 20, sketch))
        // TODO: disparosTotales++
        // TODO: cooldownActual = cooldownDisparo
    }

    // ── Daño e invencibilidad ────────────────────────────────────────────────

    @Override
    public void recibirDanio(int danio) {
        // TODO: if (invencible) return
        // TODO: vidas = Math.max(0, vidas - danio)
        // TODO: if (vidas > 0) { invencible = true; frameInvencible = DURACION_INVENCIBILIDAD }
    }

    @Override
    public HitBox getHitBox() {
        // TODO: return new HitBox(x - ANCHO_HITBOX/2, y - ALTO_HITBOX/2, ANCHO_HITBOX, ALTO_HITBOX)
        return null;
    }

    // ── Setters de movimiento (llamados por Commands) ─────────────────────────

    public void setMoverIzquierda(boolean v) { moverIzquierda = v; }
    public void setMoverDerecha(boolean v)   { moverDerecha   = v; }
    public void setMoverArriba(boolean v)    { moverArriba    = v; }
    public void setMoverAbajo(boolean v)     { moverAbajo     = v; }

    // ── Getters ───────────────────────────────────────────────────────────────

    public int getVidas()                   { return vidas; }
    public int getPuntuacion()              { return puntuacion; }
    public boolean isInvencible()           { return invencible; }
    public List<Proyectil> getProyectiles() { return proyectiles; }
    public int getDisparosTotales()         { return disparosTotales; }

    public void sumarPuntos(int puntos) {
        puntuacion += puntos;
    }
}