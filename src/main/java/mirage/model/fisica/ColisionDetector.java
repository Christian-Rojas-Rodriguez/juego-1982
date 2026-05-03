// ============================================================
// ColisionDetector — Detecta y resuelve colisiones entre entidades
// ============================================================
// GRASP : Pure Fabrication, Information Expert
// ============================================================
// Qué implementar:
//   - detectarProyectilEnemigo(): iterar todos los pares proyectil-enemigo,
//     si sus HitBoxes colisionan → enemigo.recibirDanio(), proyectil.desactivar(),
//     si enemigo muere → mirage.sumarPuntos(), estadisticas.registrarDerribo()
//   - detectarEnemigoMirage(): iterar enemigos, si colisiona con mirage
//     y mirage no es invencible → mirage.recibirDanio(1)
// Nota: no limpiar las listas aquí. El Controller limpia con removeIf
//       después de llamar al detector.
// ============================================================

package mirage.model.fisica;

import mirage.model.entidades.Mirage;
import mirage.model.entidades.Proyectil;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.stats.EstadisticasMirage;

import java.util.List;

public class ColisionDetector {

    // --- Atributos ---
    private final EstadisticasMirage estadisticas;

    // --- Constructor ---
    public ColisionDetector(EstadisticasMirage estadisticas) {
        // TODO: this.estadisticas = estadisticas
    }

    // --- Detectar proyectil contra enemigo ---
    public void detectarProyectilEnemigo(List<Proyectil> proyectiles, List<Enemigo> enemigos, Mirage mirage) {
        // TODO: for cada proyectil activo:
        //          for cada enemigo vivo:
        //            si proyectil.getHitBox().colisionaCon(enemigo.getHitBox()):
        //              enemigo.recibirDanio(proyectil.getDanio())
        //              proyectil.desactivar()
        //              si !enemigo.estaViva():
        //                mirage.sumarPuntos(enemigo.getPuntos())
        //                estadisticas.registrarDerribo(enemigo.getPuntos())
    }

    // --- Detectar enemigo contra Mirage ---
    public void detectarEnemigoMirage(List<Enemigo> enemigos, Mirage mirage) {
        // TODO: for cada enemigo vivo:
        //          si !mirage.isInvencible():
        //            si enemigo.getHitBox().colisionaCon(mirage.getHitBox()):
        //              mirage.recibirDanio(1)
    }
}
