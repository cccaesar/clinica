package com.example.clinica.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author caio
 */
public record MedicoRegisterRecordDto(
        @NotEmpty(message = "Required")
        String crm, @NotEmpty(message = "Required")
        String nome, @NotEmpty(message = "Required")
        String telefone, @NotEmpty(message = "Required")
        String email, @NotEmpty(message = "Required")
        String especialidade, @NotNull(message = "Required")
        EnderecoRegisterRecordDto endereco) {

}
