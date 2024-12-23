package com.teste.pratico.beans;

import com.teste.pratico.entities.Vaga;
import com.teste.pratico.repositories.VagaRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named("vagaBean")
@SessionScoped
@Data
public class VagaBean implements Serializable {
    private List<Vaga> vagas = new ArrayList<>();
    private LocalDate inicio;
    private LocalDate fim;
    private Integer quantidade;

    @Autowired
    private VagaRepository vagaRepository;

    public void salvarVaga() {
        if (!validarCamposObrigatorios()) {
            return;
        }
        if (inicio.isBefore(LocalDate.now())) {
            adicionarMensagem("Erro", "A data de início não pode ser retroativa.");
            return;
        }
        if (fim.isBefore(inicio)) {
            adicionarMensagem("Erro", "A data de fim não pode ser anterior à data de início.");
            return;
        }
        if (quantidade == null || quantidade <= 0) {
            adicionarMensagem("Erro", "A quantidade de vagas deve ser maior que zero.");
            return;
        }

        Vaga novaVaga = new Vaga(inicio, fim, quantidade);
        vagaRepository.save(novaVaga);
        vagas = vagaRepository.findAll();
        limparCampos();
        adicionarMensagem("Sucesso", "Vaga salva com sucesso.");
    }

    public List<Vaga> getVagas() {
        vagas = vagaRepository.findAll();
        return vagas;
    }

    private void limparCampos() {
        inicio = null;
        fim = null;
        quantidade = null;
    }

    private boolean validarCamposObrigatorios() {
        if (inicio == null || fim == null || quantidade == null) {
            adicionarMensagem("Erro", "Todos os campos são obrigatórios.");
            return false;
        }
        return true;
    }

    private void adicionarMensagem(String titulo, String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, mensagem));
    }
}