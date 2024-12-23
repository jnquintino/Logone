package com.teste.pratico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioAgendamentosDTO {
    private String solicitante;
    private Long totalAgendamentos;
    private Long quantidadeVagas;
    private Double percentual;
}