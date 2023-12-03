package com.example.clinica.services;

import com.example.clinica.models.EnderecoModel;
import com.example.clinica.repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author caio
 */
@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public EnderecoModel createEndereco(EnderecoModel enderecoModel) {
        return enderecoRepository.save(enderecoModel);
    }
    
    public EnderecoModel updateEndereco(EnderecoModel enderecoModel) {
        return enderecoRepository.save(enderecoModel);
    }
}
