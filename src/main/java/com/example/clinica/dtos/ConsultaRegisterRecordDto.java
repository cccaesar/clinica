package com.example.clinica.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 *
 * @author caio
 */
public record ConsultaRegisterRecordDto(@NotBlank(message = "Required") String pacienteId, String medicoId, @NotNull(message = "Required") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") Timestamp dataHora) {

    /**
     * @return the pacienteId
     */
    public String getPacienteId() {
        return pacienteId;
    }

    /**
     * @return the medicoId
     */
    public String getMedicoId() {
        return medicoId;
    }

    /**
     * @return the dataHora
     */
    public Timestamp getDataHora() {
        return dataHora;
    }

}
