/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.example.clinica.dtos;

import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;

/**
 *
 * @author caio
 */
public record CancelarConsultaRecordDto(
        UUID appointmentId,
        String cancellationReason) {

    /**
     * @return the appointmentId
     */
    public UUID getAppointmentId() {
        return appointmentId;
    }

    /**
     * @return the cancellationReason
     */
    public String getCancellationReason() {
        return cancellationReason;
    }
}
