package com.example.clinica.repositories;

import com.example.clinica.models.EnderecoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author caio
 */
@Repository
public interface EnderecoRepository extends JpaRepository<EnderecoModel, String>{
    
}
