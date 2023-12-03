package com.example.clinica.dtos;

/**
 *
 * @author caio
 */
public record MedicoUpdateRecordDto(String nome, String telefone, EnderecoUpdateRecordDto endereco) {

    /**
     * @return the name
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @return the endereco
     */
    public EnderecoUpdateRecordDto getEndereco() {
        return endereco;
    }

}
