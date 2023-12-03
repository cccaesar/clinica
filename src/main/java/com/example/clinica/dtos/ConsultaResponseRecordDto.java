package com.example.clinica.dtos;

import com.example.clinica.utils.MotivoCancelamento;
import java.sql.Timestamp;
import java.util.UUID;

/**
 *
 * @author caio
 */
public record ConsultaResponseRecordDto(UUID id, String pacienteNome, String pacienteCpf, String medicoNome, String medicoCrm, String medicoEspecialidade, Timestamp dataHora, String motivoCancelamento) {

}
