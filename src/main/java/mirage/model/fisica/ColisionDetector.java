package mirage.model.fisica;

import mirage.model.entidades.Mirage;
import mirage.model.entidades.Proyectil;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.stats.EstadisticasMirage;

import java.util.List;

/**
 * Detecta y resuelve colisiones entre entidades del módulo Mirage.
 *
 * Solo expone 2 métodos en MVP 1. La ausencia de un método genérico
 * enemigo↔enemigo garantiza estructuralmente que los enemigos no colisionan
 * entre sí — la regla de negocio está reforzada por diseño, no por lógica.
 *
 * Cada método maneja un par específico:
 *   1. Proyectil del jugador vs. Enemigo/Jefe
 *   2. Enemigo/Jefe vs. Mirage
 *
 * Al detectar un impacto, aplica daño, notifica a EstadisticasMirage y
 * desactiva/destruye las entidades correspondientes.
 *
 * Patrón: GRASP Pure Fabrication + Information Expert (usa HitBox de cada entidad)
 */
public class ColisionDetector {

    private final EstadisticasMirage estadisticas;

    public ColisionDetector(EstadisticasMirage estadisticas) {
        this.estadisticas = estadisticas;
    }

    /**
     * Verifica proyectiles del jugador contra todos los enemigos activos.
     * Los proyectiles son obtenidos via mirage.getProyectiles().
     *
     * Al colisionar:
     *   - Aplica proyectil.getDanio() al enemigo
     *   - Desactiva el proyectil
     *   - Si el enemigo muere: sumarPuntos en Mirage + registrarDerribo en estadisticas
     */
    public void detectarProyectilEnemigo(List<Proyectil> proyectiles, List<Enemigo> enemigos, Mirage mirage) {
        for (Proyectil p : proyectiles) {
            if (!p.isActivo()) {
                continue;
            }

            for (Enemigo e : enemigos) {
                if (!e.estaViva()) {
                    continue;
                }

                if (p.getHitBox().colisionaCon(e.getHitBox())) {
                    e.recibirDanio(p.getDanio());
                    p.desactivar();
                    estadisticas.registrarDisparoAcertado();

                    if (!e.estaViva()) {
                        mirage.sumarPuntos(e.getPuntos());
                        estadisticas.registrarDerribo(e.getTipo(), e.getPuntos());
                    }

                    break;
                }
            }
        }
    }

    /**
     * Verifica todos los enemigos activos contra el Mirage.
     * Si el Mirage está invencible, ignora la colisión.
     *
     * Al colisionar:
     *   - Aplica 1 punto de daño al Mirage
     *   - El Mirage activa su periodo de invencibilidad internamente
     */
    public void detectarEnemigoMirage(List<Enemigo> enemigos, Mirage mirage) {
        for (Enemigo e : enemigos) {
            if (!e.estaViva()) continue;
            if (mirage.isInvencible()) return; // ya invencible: nada más este frame
            if (e.getHitBox().colisionaCon(mirage.getHitBox())) {
                mirage.recibirDanio(1);
            }
        }
    }
}
