package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public class MoverIzquierdaCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        mirage.setMoverIzquierda(true);
    }

    @Override
    public void deshacer(Mirage mirage) {
        mirage.setMoverIzquierda(false);
    }
}
