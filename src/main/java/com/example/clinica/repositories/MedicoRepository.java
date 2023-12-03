package com.example.clinica.repositories;

import com.example.clinica.models.MedicoModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author caio
 */
@Repository
public interface MedicoRepository extends JpaRepository<MedicoModel, String>{
    @Query("SELECT c FROM MedicoModel c WHERE c.inativo = false")
    List<MedicoModel> findByInativoFalse();
}
