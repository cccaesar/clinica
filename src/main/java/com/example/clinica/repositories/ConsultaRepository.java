package com.example.clinica.repositories;

import com.example.clinica.models.ConsultaModel;
import com.example.clinica.models.MedicoModel;
import com.example.clinica.models.PacienteModel;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author caio
 */
@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaModel, UUID> {

    @Query("SELECT c FROM ConsultaModel c WHERE c.paciente = :paciente AND FUNCTION('DATE', c.dataHoraDaConsulta) = FUNCTION('DATE', :appointmentTime)")
    List<ConsultaModel> findDuplicateAppointmentsForPaciente(@Param("paciente") PacienteModel paciente, @Param("appointmentTime") Timestamp appointmentTime);
    
    @Query("SELECT c FROM ConsultaModel c WHERE c.medico = :medico AND c.dataHoraDaConsulta = :appointmentTime")
    List<ConsultaModel> findByMedicoAndDataHoraDaConsulta(MedicoModel medico, Timestamp appointmentTime);
}
