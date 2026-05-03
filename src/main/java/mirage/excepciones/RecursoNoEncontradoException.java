// ============================================================
// RecursoNoEncontradoException — Error al cargar sprites o archivos
// ============================================================
// Lanzar cuando: imagen no encontrada en assets/, archivo de
// estadísticas corrupto o ruta inválida.
// ============================================================

package mirage.excepciones;

public class RecursoNoEncontradoException extends JuegoException {

    private final String recurso;

    public RecursoNoEncontradoException(String recurso) {
        super("Recurso no encontrado: " + recurso);
        this.recurso = recurso;
    }

    public String getRecurso() { return recurso; }
}
