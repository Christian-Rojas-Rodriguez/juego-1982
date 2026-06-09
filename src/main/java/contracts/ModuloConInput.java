package contracts;

/**
 * Contrato opcional de input para módulos que gestionan su propio teclado.
 * HomeJuego verifica instanceof y reenvía las teclas que él no intercepta.
 */
public interface ModuloConInput {
    void onKeyPressed(char key, int keyCode);
    void onKeyReleased(char key, int keyCode);
}