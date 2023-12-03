package com.example.clinica.exceptions;

/**
 *
 * @author caio
 */
public class MedicoAlreadyExistsException extends RuntimeException {

    public MedicoAlreadyExistsException(String message) {
        super(message);
    }
}
