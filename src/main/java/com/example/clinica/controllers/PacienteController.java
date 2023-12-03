package com.example.clinica.controllers;

import com.example.clinica.dtos.PacienteRegisterRecordDto;
import com.example.clinica.dtos.PacienteResponseRecordDto;
import com.example.clinica.dtos.PacienteUpdateRecordDto;
import com.example.clinica.exceptions.PacienteAlreadyExistsException;
import com.example.clinica.models.EnderecoModel;
import com.example.clinica.models.PacienteModel;
import com.example.clinica.services.PacienteService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.postgresql.util.PSQLException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author caio
 */
@RestController
@RequestMapping("/paciente")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseRecordDto>> getAllPacientes(@RequestParam(name = "page", defaultValue = "0") int page) {
        List<PacienteResponseRecordDto> pacientes = pacienteService.getAllPacientes(page);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getPacienteByCpf(@PathVariable String cpf) {
        if(cpf.isBlank()) {
            return ResponseEntity.badRequest().body("cpf informado está vazio");
        }
        PacienteResponseRecordDto paciente = pacienteService.getPacienteByCpf(cpf);
        if (paciente != null) {
            return ResponseEntity.ok(paciente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> createPaciente(@RequestBody @Valid PacienteRegisterRecordDto pacienteDto) {
        try {
            EnderecoModel enderecoModel = new EnderecoModel();
            BeanUtils.copyProperties(pacienteDto.endereco(), enderecoModel);
            PacienteModel pacienteModel = new PacienteModel();
            BeanUtils.copyProperties(pacienteDto, pacienteModel);
            pacienteModel.setEndereco(enderecoModel);
            PacienteResponseRecordDto createdPaciente = pacienteService.createPaciente(pacienteModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPaciente);
        } catch (PacienteAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping("/{cpf}")
    public ResponseEntity<PacienteResponseRecordDto> updatePaciente(
            @PathVariable String cpf,
            @RequestBody PacienteUpdateRecordDto pacienteDto
    ) {
        PacienteModel pacienteModel = new PacienteModel();
        BeanUtils.copyProperties(pacienteDto, pacienteModel);
        if (pacienteDto.endereco() != null) {
            EnderecoModel enderecoModel = new EnderecoModel();
            BeanUtils.copyProperties(pacienteDto.endereco(), enderecoModel);
            pacienteModel.setEndereco(enderecoModel);
        }
        PacienteResponseRecordDto updatedPaciente = pacienteService.updatePaciente(cpf, pacienteModel);
        if (updatedPaciente == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedPaciente);
        }
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletePaciente(@PathVariable String cpf) {
        if (pacienteService.deletePaciente(cpf) != null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
        @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableExceptions(
            HttpMessageNotReadableException ex) {
        return ex.getMessage();
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PSQLException.class)
    public String handleHttpPSQLExceptions(
            PSQLException ex) {
        return ex.getServerErrorMessage().getMessage();
    }
}
