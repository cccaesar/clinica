package com.example.clinica.utils;


/**
 *
 * @author caio
 */
public enum MotivoCancelamento {
    PACIENTE_DESISTIU,
    MEDICO_CANCELOU,
    OUTROS;

    public static boolean contains(String test) {

        for (MotivoCancelamento c : MotivoCancelamento.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}
