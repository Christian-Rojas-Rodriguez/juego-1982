// ============================================================
// JuegoException — Raíz de la jerarquía de excepciones del módulo
// ============================================================
// Todas las excepciones propias del módulo Mirage heredan de aquí.
// Esto permite capturar cualquier error del módulo con un solo catch.
// ============================================================

package mirage.excepciones;

public class JuegoException extends Exception {

    public JuegoException(String mensaje) {
        super(mensaje);
    }

    public JuegoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
