package com.example.clinica.services;

import com.example.clinica.dtos.EnderecoUpdateRecordDto;
import com.example.clinica.dtos.MedicoResponseRecordDto;
import com.example.clinica.dtos.MedicoUpdateRecordDto;
import com.example.clinica.exceptions.MedicoAlreadyExistsException;
import com.example.clinica.models.EnderecoModel;
import com.example.clinica.models.MedicoModel;
import com.example.clinica.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EnderecoService enderecoService;

    @Autowired
    public MedicoService(MedicoRepository medicoRepository, EnderecoService enderecoService) {
        this.medicoRepository = medicoRepository;
        this.enderecoService = enderecoService;
    }

    public List<MedicoResponseRecordDto> getAllMedicos(int page) {
        Sort sortOrder = Sort.by("nome").ascending();
        Pageable pageable = PageRequest.of(page, 10, sortOrder);
        List<MedicoModel> medicos = medicoRepository.findAll(pageable).toList();
        return medicos.stream()
                .filter(medico -> medico.isInativo() != true)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MedicoResponseRecordDto getMedicoByCrm(String crm) {
        MedicoModel medico = medicoRepository.findById(crm).orElse(null);
        if (medico == null || medico.isInativo()) {
            return null;
        }
        return convertToDto(medico);
    }

    @Transactional
    public MedicoResponseRecordDto createMedico(MedicoModel medicoModel) {
        Optional<MedicoModel> existingMedico = medicoRepository.findById(medicoModel.getCrm());
        if (existingMedico.isPresent()) {
            throw new MedicoAlreadyExistsException("A Medico with this CRM already exists.");
        }
        EnderecoModel enderecoModel = medicoModel.getEndereco();
        enderecoModel = enderecoService.createEndereco(enderecoModel);
        medicoModel.setEndereco(enderecoModel);
        medicoRepository.save(medicoModel);
        return convertToDto(medicoModel);
    }

    @Transactional
    public MedicoModel deleteMedicoByCrm(String crm) {
        Optional<MedicoModel> medicoOptional = medicoRepository.findById(crm);
        if (medicoOptional.isPresent()) {
            MedicoModel medicoModel = medicoOptional.get();
            medicoModel.setInativo(true);
            return medicoRepository.save(medicoModel);
        }
        return null;
    }

    @Transactional
    public MedicoResponseRecordDto updateMedico(String crm, MedicoUpdateRecordDto medicoUpdateRecordDto) {
        Optional<MedicoModel> medicoOptional = medicoRepository.findById(crm);
        if (medicoOptional.isEmpty()) {
            return null;
        }
        MedicoModel medicoModel = medicoOptional.get();
        if (medicoUpdateRecordDto.getEndereco() != null) {
            EnderecoUpdateRecordDto updatedEndereco = medicoUpdateRecordDto.getEndereco();
            EnderecoModel currentEndereco = medicoModel.getEndereco();

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
            medicoModel.setEndereco(currentEndereco);
        }
        if (medicoUpdateRecordDto.getNome() != null) {
            medicoModel.setNome(medicoUpdateRecordDto.getNome());
        }
        if (medicoUpdateRecordDto.getTelefone() != null) {
            medicoModel.setTelefone(medicoUpdateRecordDto.getTelefone());
        }

        return convertToDto(medicoRepository.save(medicoModel));
    }

    public MedicoResponseRecordDto convertToDto(MedicoModel medicoModel) {
        return new MedicoResponseRecordDto(medicoModel.getNome(), medicoModel.getEmail(), medicoModel.getCrm(), medicoModel.getEspecialidade().name());
    }
}
