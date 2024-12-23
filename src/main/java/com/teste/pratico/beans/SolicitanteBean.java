package com.teste.pratico.beans;

import com.teste.pratico.entities.Solicitante;
import com.teste.pratico.repositories.SolicitanteRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("solicitanteBean")
@SessionScoped
@Data
public class SolicitanteBean implements Serializable {
    private String nome;
    private List<Solicitante> solicitantes = new ArrayList<>();

    @Autowired
    private SolicitanteRepository solicitanteRepository;

    public void salvarSolicitante() {
        Solicitante novoSolicitante = new Solicitante(nome);
        solicitanteRepository.save(novoSolicitante);
        solicitantes = solicitanteRepository.findAll();
        limparCampos();
    }

    public List<Solicitante> getSolicitantes() {
        solicitantes = solicitanteRepository.findAll();
        return solicitantes;
    }

    private void limparCampos() {
        nome = null;
    }
}
