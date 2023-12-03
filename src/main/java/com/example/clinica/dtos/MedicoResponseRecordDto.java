/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.example.clinica.dtos;

/**
 *
 * @author caio
 */
public record MedicoResponseRecordDto(String nome,
        String email,
        String crm,
        String especialidade) {

}
