package com.example.clinica.models;

import com.example.clinica.utils.Especialidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

/**
 *
 * @author caio
 */
@Entity
@Table(name = "TB_MEDICO")
@Data
@EqualsAndHashCode(callSuper = false)
public class MedicoModel extends RepresentationModel<MedicoModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String crm;

    @NotNull
    private String nome;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String telefone;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Especialidade especialidade;

    @Column(columnDefinition = "boolean default false")
    private boolean inativo;

    @OneToOne
    @JoinColumn(name = "endereco_cep")
    @NotNull
    private EnderecoModel endereco;

    @OneToMany(mappedBy = "medico")
    private Set<ConsultaModel> consultas;
}
