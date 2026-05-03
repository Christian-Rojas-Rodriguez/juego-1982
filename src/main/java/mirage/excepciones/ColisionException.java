// ============================================================
// ColisionException — Error en la detección de colisiones
// ============================================================
// Lanzar cuando: HitBox nula, entidades en estado inválido para colisión.
// ============================================================

package mirage.excepciones;

public class ColisionException extends JuegoException {

    public ColisionException(String mensaje) {
        super(mensaje);
    }

    public ColisionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
