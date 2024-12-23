package com.teste.pratico.repositories;

import com.teste.pratico.dto.RelatorioAgendamentosDTO;
import com.teste.pratico.entities.Agendamento;
import com.teste.pratico.entities.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query(value = "SELECT * FROM agendamento a " +
                   "WHERE a.data BETWEEN :inicio AND :fim " +
                   "AND (:solicitanteId IS NULL OR a.solicitante_id = :solicitanteId)",
           nativeQuery = true)
    List<Object[]> findAgendamentosByPeriodoAndSolicitante(
            @Param("inicio") Date inicio,
            @Param("fim") Date fim,
            @Param("solicitanteId") Long solicitanteId);

    @Query(value = "SELECT s.nome AS solicitante, COUNT(a.id) AS total_agendamentos, v.quantidade AS quantidade_vagas, " +
                   "(COUNT(a.id) * 100.0 / v.quantidade) AS percentual " +
                   "FROM agendamento a " +
                   "JOIN solicitante s ON a.solicitante_id = s.id " +
                   "JOIN vagas v ON v.id = a.id " +
                   "WHERE a.data BETWEEN :inicio AND :fim " +
                   "GROUP BY s.nome, v.quantidade",
           nativeQuery = true)
    List<Object[]> findRelatorioAgendamentos(
            @Param("inicio") Date inicio,
            @Param("fim") Date fim);

    @Query(value = "SELECT COUNT(*) FROM agendamento a WHERE a.data = :data", nativeQuery = true)
    long countByData(@Param("data") Date data);

    @Query(value = "SELECT COUNT(*) FROM agendamento a WHERE a.data = :data AND a.solicitante_id = :solicitante", nativeQuery = true)
    long countByDataAndSolicitante(@Param("data") Date data, @Param("solicitante") Long solicitante);

}
