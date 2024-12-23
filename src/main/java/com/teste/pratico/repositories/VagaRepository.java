package com.teste.pratico.repositories;

import com.teste.pratico.entities.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    @Query(value = "SELECT * FROM vagas v WHERE v.data_inicio <= :data AND v.data_fim >= :data", nativeQuery = true)
    List<Vaga> findByPeriodo(@Param("data") Date data);

}
