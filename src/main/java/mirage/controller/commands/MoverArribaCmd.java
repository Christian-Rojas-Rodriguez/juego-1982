package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public class MoverArribaCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        mirage.setMoverArriba(true);
    }

    @Override
    public void deshacer(Mirage mirage) {
        mirage.setMoverArriba(false);
    }
}
