package mirage.model.estado;

import mirage.controller.GameController;

/**
 * Estado del juego (patrón State). Cada método recibe el GameController para
 * acceder al model sin que el estado se acople a él en el constructor.
 */
public interface EstadoJuego {

    void update(GameController controller);

    void render(GameController controller);

    void alEntrar(GameController controller);

    void onKeyPressed(GameController controller, char key, int keyCode);
}
