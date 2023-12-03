package com.example.clinica.services;

import com.example.clinica.dtos.CancelarConsultaRecordDto;
import com.example.clinica.exceptions.InvalidConsultationTimeException;
import com.example.clinica.exceptions.InactivePacienteException;
import com.example.clinica.dtos.ConsultaRegisterRecordDto;
import com.example.clinica.dtos.ConsultaResponseRecordDto;
import com.example.clinica.exceptions.ConsultaNotFoundException;
import com.example.clinica.exceptions.DuplicateAppointmentException;
import com.example.clinica.exceptions.InactiveMedicoException;
import com.example.clinica.exceptions.InvalidCancellationReasonException;
import com.example.clinica.exceptions.InvalidCancellationTimeException;
import com.example.clinica.exceptions.MedicoConflictException;
import com.example.clinica.exceptions.NoAvailableMedicoException;
import com.example.clinica.models.ConsultaModel;
import com.example.clinica.models.MedicoModel;
import com.example.clinica.models.PacienteModel;
import com.example.clinica.repositories.ConsultaRepository;
import com.example.clinica.repositories.MedicoRepository;
import com.example.clinica.repositories.PacienteRepository;
import com.example.clinica.utils.MotivoCancelamento;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author caio
 */
@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    @Autowired
    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    public ConsultaResponseRecordDto createConsulta(ConsultaRegisterRecordDto consultaRequest) {
        PacienteModel paciente = pacienteRepository.findById(consultaRequest.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente not found"));
        if (paciente.isInativo()) {
            throw new InactivePacienteException("Paciente is inactive and cannot schedule appointments.");
        }

        MedicoModel medico = null;
        if (consultaRequest.getMedicoId() != null) {
            medico = medicoRepository.findById(consultaRequest.getMedicoId())
                    .orElseThrow(() -> new EntityNotFoundException("Medico not found"));
            if (medico != null && medico.isInativo()) {
                throw new InactiveMedicoException("Medico is inactive and cannot schedule appointments.");
            }
        } else {
            medico = selectRandomAvailableMedico(consultaRequest.getDataHora().toLocalDateTime());
        }

        validateConsultaCreationRules(paciente, medico, consultaRequest.getDataHora());

        ConsultaModel consulta = new ConsultaModel();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHoraDaConsulta(consultaRequest.getDataHora());

        return convertToDto(consultaRepository.save(consulta));
    }

    public List<ConsultaResponseRecordDto> getAllConsultas(int page) {
        Sort sortOrder = Sort.by("dataHoraDaConsulta").ascending();
        Pageable pageable = PageRequest.of(page, 10, sortOrder);
        List<ConsultaModel> medicos = consultaRepository.findAll(pageable).toList();
        return medicos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private void validateConsultaCreationRules(PacienteModel paciente, MedicoModel medico, Timestamp dataHoraDaConsulta) {
        if (!isValidClinicOpeningHours(dataHoraDaConsulta.toLocalDateTime())) {
            throw new InvalidConsultationTimeException("Appointments can only be scheduled during clinic opening hours.");
        }

        if (!isValidAppointmentBookingTime(dataHoraDaConsulta.toLocalDateTime())) {
            throw new InvalidConsultationTimeException("Appointments must be booked at least 30 minutes in advance.");
        }

        if (isDuplicateAppointmentForPaciente(paciente, dataHoraDaConsulta)) {
            throw new DuplicateAppointmentException("Paciente already has an appointment scheduled for this date/time.");
        }

        if (medico != null && this.hasConflictingAppointment(medico, dataHoraDaConsulta)) {
            throw new MedicoConflictException("Medico already has an appointment scheduled for this date/time.");
        }
    }

    private boolean isValidClinicOpeningHours(LocalDateTime appointmentTime) {
        LocalTime clinicOpeningTime = LocalTime.of(7, 0);
        LocalTime clinicClosingTime = LocalTime.of(19, 0);

        LocalTime appointmentLocalTime = appointmentTime.toLocalTime();

        return !appointmentLocalTime.isBefore(clinicOpeningTime) && !appointmentLocalTime.isAfter(clinicClosingTime);
    }

    private boolean isDuplicateAppointmentForPaciente(PacienteModel paciente, Timestamp appointmentTime) {
        List<ConsultaModel> duplicateAppointments = consultaRepository.findDuplicateAppointmentsForPaciente(paciente, appointmentTime);
        return !duplicateAppointments.isEmpty();
    }

    private boolean hasConflictingAppointment(MedicoModel medico, Timestamp appointmentTime) {
        List<ConsultaModel> appointments = consultaRepository.findByMedicoAndDataHoraDaConsulta(medico, appointmentTime);

        return !appointments.isEmpty();
    }

    private boolean isValidAppointmentBookingTime(LocalDateTime appointmentTime) {

        LocalDateTime currentTime = LocalDateTime.now();

        return currentTime.plus(30, ChronoUnit.MINUTES).isBefore(appointmentTime);
    }

    private List<MedicoModel> findAvailableMedicos(LocalDateTime appointmentTime) {

        List<MedicoModel> allMedicos = medicoRepository.findByInativoFalse();

        List<MedicoModel> availableMedicos = new ArrayList<>();

        for (MedicoModel medico : allMedicos) {
            if (!hasConflictingAppointment(medico, Timestamp.valueOf(appointmentTime))) {
                availableMedicos.add(medico);
            }
        }

        return availableMedicos;
    }

    @Transactional
    public MedicoModel selectRandomAvailableMedico(LocalDateTime appointmentTime) {
        List<MedicoModel> availableMedicos = findAvailableMedicos(appointmentTime);

        if (availableMedicos.isEmpty()) {
            throw new NoAvailableMedicoException("No available Medicos for the given appointment time.");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(availableMedicos.size());

        return availableMedicos.get(randomIndex);
    }

    @Transactional
    public ConsultaResponseRecordDto cancelarConsulta(CancelarConsultaRecordDto cancelarConsultaDto) {
        UUID appointmentId = cancelarConsultaDto.getAppointmentId();
        String cancellationReason = cancelarConsultaDto.getCancellationReason();

        ConsultaModel consulta = consultaRepository.findById(appointmentId)
                .orElseThrow(() -> new ConsultaNotFoundException("Appointment not found"));

        if (!isValidCancellationReason(cancellationReason)) {
            throw new InvalidCancellationReasonException("Invalid cancellation reason");
        }

        if (!isValidCancellationTime(consulta.getDataHoraDaConsulta().toLocalDateTime())) {
            throw new InvalidCancellationTimeException("Appointment can only be canceled at least 24 hours in advance");
        }

        consulta.setMotivoCancelamento(MotivoCancelamento.valueOf(cancellationReason));
        return convertToDto(consultaRepository.save(consulta));
    }

    private boolean isValidCancellationReason(String cancellationReason) {
        return MotivoCancelamento.contains(cancellationReason);
    }

    private boolean isValidCancellationTime(LocalDateTime appointmentTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Duration.between(currentDateTime, appointmentTime).toHours() >= 24;
    }

    public ConsultaResponseRecordDto convertToDto(ConsultaModel consultaModel) {
        if (consultaModel == null) {
            return null;
        }
        String motivoCancelamentoName = null;
        if(consultaModel.getMotivoCancelamento() != null) {
            motivoCancelamentoName = consultaModel.getMotivoCancelamento().name();
        }
        return new ConsultaResponseRecordDto(consultaModel.getId(), consultaModel.getPaciente().getNome(), consultaModel.getPaciente().getCpf(), consultaModel.getMedico().getNome(), consultaModel.getMedico().getCrm(), consultaModel.getMedico().getEspecialidade().name(), consultaModel.getDataHoraDaConsulta(), motivoCancelamentoName);
    }
}
