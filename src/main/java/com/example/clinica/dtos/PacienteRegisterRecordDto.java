package com.example.clinica.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author caio
 */
public record PacienteRegisterRecordDto(@NotEmpty(message = "Required")
        String cpf, @NotEmpty(message = "Required")
        String nome, @NotEmpty(message = "Required")
        String telefone, @NotEmpty(message = "Required")
        String email, @NotNull(message = "Required")
        EnderecoRegisterRecordDto endereco) {

}
