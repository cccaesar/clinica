package com.example.clinica.controllers;

import com.example.clinica.dtos.MedicoRegisterRecordDto;
import com.example.clinica.dtos.MedicoUpdateRecordDto;
import com.example.clinica.dtos.MedicoResponseRecordDto;
import com.example.clinica.exceptions.MedicoAlreadyExistsException;
import com.example.clinica.models.EnderecoModel;
import com.example.clinica.models.MedicoModel;
import com.example.clinica.services.MedicoService;
import com.example.clinica.utils.Especialidade;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.postgresql.util.PSQLException;

/**
 *
 * @author caio
 */
@RestController
@RequestMapping("/medico")
public class MedicoController {

    private final MedicoService medicoService;

    @Autowired
    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponseRecordDto>> getMedicos(@RequestParam(name = "page", defaultValue = "0") int page) {
        List<MedicoResponseRecordDto> medicoList = medicoService.getAllMedicos(page);
        return ResponseEntity.status(HttpStatus.OK).body(medicoList);
    }

    @GetMapping("/{crm}")
    public ResponseEntity<Object> getOneMedico(@PathVariable(value = "crm") String crm) {
        MedicoResponseRecordDto medico = medicoService.getMedicoByCrm(crm);
        if (medico != null) {
            return ResponseEntity.status(HttpStatus.OK).body(medico);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medico not found.");
        }
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Object> registrarMedico(@RequestBody @Valid MedicoRegisterRecordDto medicoRegisterRecordDto) throws Exception {
        try {
            EnderecoModel enderecoModel = new EnderecoModel();
            BeanUtils.copyProperties(medicoRegisterRecordDto.endereco(), enderecoModel);
            MedicoModel medicoModel = new MedicoModel();
            Especialidade especialidade = Especialidade.valueOf(medicoRegisterRecordDto.especialidade());
            medicoModel.setEspecialidade(especialidade);
            BeanUtils.copyProperties(medicoRegisterRecordDto, medicoModel);
            medicoModel.setEndereco(enderecoModel);
            MedicoResponseRecordDto createdMedico = medicoService.createMedico(medicoModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMedico);
        } catch (MedicoAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{crm}")
    public ResponseEntity<Object> deletarMedico(@PathVariable(value = "crm") String crm) {
        MedicoModel updatedMedico = medicoService.deleteMedicoByCrm(crm);
        if (updatedMedico != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedMedico);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medico not found.");
        }
    }

    @PatchMapping("/{crm}")
    public ResponseEntity<Object> updateMedico(@PathVariable(value = "crm") String crm,
            @RequestBody @Valid MedicoUpdateRecordDto medicoUpdateRecordDto) {
        MedicoResponseRecordDto updatedMedico = medicoService.updateMedico(crm, medicoUpdateRecordDto);
        if (updatedMedico != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedMedico);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medico not found.");
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
