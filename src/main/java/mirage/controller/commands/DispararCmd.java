package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public class DispararCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        mirage.disparar();
    }

    @Override
    public void deshacer(Mirage mirage) {
        // El disparo no se deshace
    }
}
