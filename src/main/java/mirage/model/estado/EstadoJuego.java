package mirage.model.estado;

import mirage.controller.GameController;

public interface EstadoJuego {

    void update(GameController controller);

    void render(GameController controller);

    void alEntrar(GameController controller);

    void onKeyPressed(GameController controller, char key, int keyCode);
}
