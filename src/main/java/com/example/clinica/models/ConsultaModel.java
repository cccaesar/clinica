/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.clinica.models;

import com.example.clinica.utils.MotivoCancelamento;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

/**
 *
 * @author caio
 */
@Entity
@Table(name = "TB_CONSULTA")
public class ConsultaModel implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @NotNull
    private MedicoModel medico;
    @ManyToOne
    @NotNull
    private PacienteModel paciente;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dataHoraDaConsulta;
    private MotivoCancelamento motivoCancelamento;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the medico
     */
    public MedicoModel getMedico() {
        return medico;
    }

    /**
     * @return the paciente
     */
    public PacienteModel getPaciente() {
        return paciente;
    }

    /**
     * @return the dataHoraDaConsulta
     */
    public Timestamp getDataHoraDaConsulta() {
        return dataHoraDaConsulta;
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return the motivoCancelamento
     */
    public MotivoCancelamento getMotivoCancelamento() {
        return motivoCancelamento;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @param medico the medico to set
     */
    public void setMedico(MedicoModel medico) {
        this.medico = medico;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(PacienteModel paciente) {
        this.paciente = paciente;
    }

    /**
     * @param dataHoraDaConsulta the dataHoraDaConsulta to set
     */
    public void setDataHoraDaConsulta(Timestamp dataHoraDaConsulta) {
        this.dataHoraDaConsulta = dataHoraDaConsulta;
    }

    /**
     * @param motivoCancelamento the motivoCancelamento to set
     */
    public void setMotivoCancelamento(MotivoCancelamento motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }
}
