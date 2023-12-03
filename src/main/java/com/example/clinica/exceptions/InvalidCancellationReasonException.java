/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.clinica.exceptions;

/**
 *
 * @author caio
 */
public class InvalidCancellationReasonException extends ClientErrorException {
    public InvalidCancellationReasonException(String message) {
        super(message);
    }
}
