package com.teste.pratico.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name = "agendamento")
@Table(name = "agendamento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date data;

    private String numero;

    private String motivo;

    @ManyToOne
//    @JoinColumn(name = "solicitante_id", nullable = false)
    private Solicitante solicitante;

    public Agendamento(Date data, String numero, String motivo, Solicitante solicitante) {
        this.data = data;
        this.numero = numero;
        this.motivo = motivo;
        this.solicitante = solicitante;
    }
}
