package com.example.clinica.services;

import com.example.clinica.dtos.PacienteResponseRecordDto;
import com.example.clinica.models.EnderecoModel;
import com.example.clinica.models.PacienteModel;
import com.example.clinica.repositories.PacienteRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author caio
 */
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final EnderecoService enderecoService;

    public PacienteService(PacienteRepository pacienteRepository, EnderecoService enderecoService) {
        this.pacienteRepository = pacienteRepository;
        this.enderecoService = enderecoService;
    }

    public List<PacienteResponseRecordDto> getAllPacientes(int page) {
        Sort sortOrder = Sort.by("nome").ascending();
        Pageable pageable = PageRequest.of(page, 10, sortOrder);
        List<PacienteModel> pacientes = pacienteRepository.findAll(pageable).toList();

        return pacientes.stream()
                .filter(paciente -> paciente.isInativo() != true)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PacienteResponseRecordDto getPacienteByCpf(String cpf) {
        PacienteModel paciente = pacienteRepository.findById(cpf).orElse(null);
        if (paciente == null || paciente.isInativo()) {
            return null;
        }
        return convertToDto(paciente);
    }

    @Transactional
    public PacienteResponseRecordDto createPaciente(PacienteModel pacienteModel) {
        EnderecoModel enderecoModel = pacienteModel.getEndereco();
        enderecoModel = enderecoService.createEndereco(enderecoModel);
        pacienteModel.setEndereco(enderecoModel);
        return convertToDto(pacienteRepository.save(pacienteModel));
    }

    @Transactional
    public PacienteResponseRecordDto updatePaciente(String cpf, PacienteModel paciente) {
        Optional<PacienteModel> existingPaciente = pacienteRepository.findById(cpf);
        if (existingPaciente.isEmpty()) {
            return null;
        }
        PacienteModel existingPacienteModel = existingPaciente.get();
        if (paciente.getEndereco() != null) {
            EnderecoModel updatedEndereco = paciente.getEndereco();
            EnderecoModel currentEndereco = existingPacienteModel.getEndereco();

            if (updatedEndereco.getLogradouro() != null) {
                currentEndereco.setLogradouro(updatedEndereco.getLogradouro());
            }
            if (updatedEndereco.getBairro() != null) {
                currentEndereco.setBairro(updatedEndereco.getBairro());
            }
            if (updatedEndereco.getCep() != null) {
                currentEndereco.setCep(updatedEndereco.getCep());
            }
            if (updatedEndereco.getCidade() != null) {
                currentEndereco.setCidade(updatedEndereco.getCidade());
            }
            if (updatedEndereco.getUf() != null) {
                currentEndereco.setUf(updatedEndereco.getUf());
            }
            if (updatedEndereco.getComplemento() != null) {
                currentEndereco.setComplemento(updatedEndereco.getComplemento());
            }
            if (updatedEndereco.getNumero() != null) {
                currentEndereco.setNumero(updatedEndereco.getNumero());
            }
            enderecoService.updateEndereco(currentEndereco);
            existingPacienteModel.setEndereco(currentEndereco);
        }
        if (paciente.getNome() != null) {
            existingPacienteModel.setNome(paciente.getNome());
        }
        if (paciente.getTelefone() != null) {
            existingPacienteModel.setTelefone(paciente.getTelefone());
        }
        return convertToDto(pacienteRepository.save(existingPacienteModel));
    }

    @Transactional
    public PacienteModel deletePaciente(String cpf) {
        Optional<PacienteModel> pacienteOptional = pacienteRepository.findById(cpf);
        if (pacienteOptional.isEmpty()) {
            return null;
        }
        PacienteModel pacienteModel = pacienteOptional.get();
        pacienteModel.setInativo(true);
        return pacienteRepository.save(pacienteModel);
    }

    public PacienteResponseRecordDto convertToDto(PacienteModel pacienteModel) {
        if (pacienteModel == null) {
            return null;
        }
        return new PacienteResponseRecordDto(pacienteModel.getNome(), pacienteModel.getEmail(), pacienteModel.getCpf(), pacienteModel.getTelefone());
    }
}
