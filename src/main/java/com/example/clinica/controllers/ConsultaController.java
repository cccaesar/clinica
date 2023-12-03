package com.example.clinica.controllers;

import com.example.clinica.dtos.CancelarConsultaRecordDto;
import com.example.clinica.dtos.ConsultaRegisterRecordDto;
import com.example.clinica.dtos.ConsultaResponseRecordDto;
import com.example.clinica.exceptions.ClientErrorException;
import com.example.clinica.exceptions.ConsultaNotFoundException;
import com.example.clinica.exceptions.NoAvailableMedicoException;
import com.example.clinica.services.ConsultaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/consulta")
public class ConsultaController {

    private final ConsultaService consultaService;

    @Autowired
    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<Object> createConsulta(@RequestBody @Valid ConsultaRegisterRecordDto consultaRequest) {
        try {
            ConsultaResponseRecordDto createdConsulta = consultaService.createConsulta(consultaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdConsulta);
        } catch (ClientErrorException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EntityNotFoundException | NoAvailableMedicoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ConsultaResponseRecordDto>> getConsultas(@RequestParam(name = "page", defaultValue = "0") int page) {
        List<ConsultaResponseRecordDto> consultaList = consultaService.getAllConsultas(page);
        return ResponseEntity.status(HttpStatus.OK).body(consultaList);
    }

    @PutMapping("/cancelar")
    public ResponseEntity<Object> cancelarConsulta(@RequestBody @Valid CancelarConsultaRecordDto cancelarConsulta) {
        try {
            ConsultaResponseRecordDto canceledConsulta = consultaService.cancelarConsulta(cancelarConsulta);
            return ResponseEntity.status(HttpStatus.OK).body(canceledConsulta);
        } catch (ConsultaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ClientErrorException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
