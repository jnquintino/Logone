package com.teste.pratico.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "vagas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate inicio;
    private LocalDate fim;
    private int quantidade;

    public Vaga(LocalDate inicio, LocalDate fim, Integer quantidade) {
        this.inicio = inicio;
        this.fim = fim;
        this.quantidade = quantidade;
    }
}
