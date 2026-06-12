package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public class MoverDerechaCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        mirage.setMoverDerecha(true);
    }

    @Override
    public void deshacer(Mirage mirage) {
        mirage.setMoverDerecha(false);
    }
}
