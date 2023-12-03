/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.clinica.exceptions;

/**
 *
 * @author caio
 */
public class PacienteAlreadyExistsException extends RuntimeException {

    public PacienteAlreadyExistsException(String message) {
        super(message);
    }
}
