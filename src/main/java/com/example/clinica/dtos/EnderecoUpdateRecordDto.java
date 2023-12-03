/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.example.clinica.dtos;

/**
 *
 * @author caio
 */
public record EnderecoUpdateRecordDto(String cep, String logradouro, String bairro, String cidade, String uf, String complemento, String numero) {

    /**
     * @return the cep
     */
    public String getCep() {
        return cep;
    }

    /**
     * @return the logradouro
     */
    public String getLogradouro() {
        return logradouro;
    }

    /**
     * @return the bairro
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * @return the cidade
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * @return the uf
     */
    public String getUf() {
        return uf;
    }

    /**
     * @return the complemento
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

}
