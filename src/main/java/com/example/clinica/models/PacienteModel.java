package com.example.clinica.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import org.springframework.hateoas.RepresentationModel;

/**
 *
 * @author caio
 */
@Entity
@Table(name = "TB_PACIENTE")
public class PacienteModel extends RepresentationModel<PacienteModel>  implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    private String cpf;
    
    @NotNull
    @Column(unique=true)
    private String email;
    @NotNull
    private String nome;
    @NotNull
    private String telefone;
    @NotNull
    @Column(columnDefinition = "boolean default false")
    private boolean inativo;
    @OneToOne
    @JoinColumn(name = "endereco_cep")
    @NotNull
    private EnderecoModel endereco;
    
    @OneToMany(mappedBy="paciente")
    private Set<ConsultaModel> consultas;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the inativo
     */
    public boolean isInativo() {
        return inativo;
    }

    /**
     * @param inativo the inativo to set
     */
    public void setInativo(boolean inativo) {
        this.inativo = inativo;
    }

    /**
     * @return the endereco
     */
    public EnderecoModel getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(EnderecoModel endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the consultas
     */
    public Set<ConsultaModel> getConsultas() {
        return consultas;
    }

    /**
     * @param consultas the consultas to set
     */
    public void setConsultas(Set<ConsultaModel> consultas) {
        this.consultas = consultas;
    }
}
