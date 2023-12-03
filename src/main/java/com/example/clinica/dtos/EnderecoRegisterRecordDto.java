package com.example.clinica.dtos;

import jakarta.validation.constraints.NotEmpty;

/**
 *
 * @author caio
 */
public record EnderecoRegisterRecordDto(@NotEmpty(message = "Required")
        String cep, @NotEmpty(message = "Required")
        String logradouro, @NotEmpty(message = "Required")
        String bairro, @NotEmpty(message = "Required")
        String cidade, @NotEmpty(message = "Required")
        String uf, String complemento, String numero) {

}
