package mirage.controller.commands;

import mirage.model.entidades.Mirage;

public interface Comando {

    /** Acción al presionar la tecla. */
    void ejecutar(Mirage mirage);

    /** Acción al soltar la tecla — frena el movimiento continuo. */
    void deshacer(Mirage mirage);
}
