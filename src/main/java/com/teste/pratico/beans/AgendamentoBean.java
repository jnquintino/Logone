package com.teste.pratico.beans;

import com.teste.pratico.dto.RelatorioAgendamentosDTO;
import com.teste.pratico.entities.Agendamento;
import com.teste.pratico.entities.Solicitante;
import com.teste.pratico.entities.Vaga;
import com.teste.pratico.repositories.AgendamentoRepository;
import com.teste.pratico.repositories.SolicitanteRepository;
import com.teste.pratico.repositories.VagaRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named("agendamentoBean")
@SessionScoped
@Data
public class AgendamentoBean implements Serializable {
    private Date data;
    private String numero;
    private String motivo;
    private Long solicitante;
    private Date inicio;
    private Date fim;
    private List<Agendamento> agendamentos = new ArrayList<>();
    private List<Agendamento> agendamentosFiltrados = new ArrayList<>();
    private List<RelatorioAgendamentosDTO> relatorioAgendamentos = new ArrayList<>();

    @Autowired
    private SolicitanteRepository solicitanteRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private VagaRepository vagaRepository;

    private void adicionarMensagem(String titulo, String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, mensagem));
    }

    @Transactional
    public void salvarAgendamento() {
        try {
            if (data == null || motivo == null || numero == null || solicitante == null) {
                adicionarMensagem("Erro", "Todos os campos são obrigatórios.");
                return;
            }

            Solicitante solicitanteEntidade = solicitanteRepository.findById(solicitante).orElse(null);
            if (solicitanteEntidade == null) {
                adicionarMensagem("Erro", "Solicitante não encontrado.");
                return;
            }

            List<Vaga> vagas = vagaRepository.findByPeriodo(data);
            if (vagas.isEmpty()) {
                adicionarMensagem("Erro", "Não há vagas disponíveis para este período.");
                return;
            }

            Vaga vaga = vagas.get(0);
            long totalAgendamentos = agendamentoRepository.countByData(data);
            if (totalAgendamentos >= vaga.getQuantidade()) {
                adicionarMensagem("Erro", "Limite de vagas excedido para o período.");
                return;
            }

            long totalAgendamentosSolicitante = agendamentoRepository.countByDataAndSolicitante(data, solicitanteEntidade.getId());
            long limiteSolicitante = Math.round(vaga.getQuantidade() * 0.25);
            if (totalAgendamentosSolicitante >= limiteSolicitante) {
                adicionarMensagem("Erro", String.format("Limite de %d agendamentos para este solicitante no período.", limiteSolicitante));
                return;
            }

            Agendamento novoAgendamento = new Agendamento(data, numero, motivo, solicitanteEntidade);
            agendamentoRepository.save(novoAgendamento);
            agendamentos = agendamentoRepository.findAll();
            limparCampos();
            adicionarMensagem("Sucesso", "Agendamento salvo com sucesso.");
        } catch (Exception e) {
            adicionarMensagem("Erro", "Ocorreu um erro ao salvar o agendamento: " + e.getMessage());
        }
    }

    public List<Agendamento> getAgendamentos() {
        agendamentos = agendamentoRepository.findAll();
        return agendamentos;
    }

    private void limparCampos() {
        data = null;
        numero = null;
        motivo = null;
        solicitante = null;
    }

    public List<Agendamento> consultarAgendamentos() {
        List<Object[]> resultados = agendamentoRepository.findAgendamentosByPeriodoAndSolicitante(inicio, fim, solicitante);
        List<Agendamento> agendamentos = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Long agendamentoId = ((Number) resultado[0]).longValue();
            Date data = (Date) resultado[1];
            String numero = resultado[2].toString();
            String motivo = (String) resultado[3];
            Long solicitanteIdResult = ((Number) resultado[4]).longValue();

            Solicitante solicitante = solicitanteRepository.findById(solicitanteIdResult).orElse(null);

            Agendamento agendamento = new Agendamento();
            agendamento.setId(agendamentoId);
            agendamento.setData(data);
            agendamento.setNumero(numero);
            agendamento.setMotivo(motivo);
            agendamento.setSolicitante(solicitante);

            agendamentos.add(agendamento);
        }

        return agendamentos;
    }

    public List<RelatorioAgendamentosDTO> consultarRelatorio() {
        List<Object[]> resultados = agendamentoRepository.findRelatorioAgendamentos(inicio, fim);
        List<RelatorioAgendamentosDTO> relatorio = new ArrayList<>();

        for (Object[] resultado : resultados) {
            String solicitante = (String) resultado[0];
            Long totalAgendamentos = ((Number) resultado[1]).longValue();
            Long quantidadeVagas = ((Number) resultado[2]).longValue();
            Double percentual = ((Number) resultado[3]).doubleValue();

            RelatorioAgendamentosDTO dto = new RelatorioAgendamentosDTO();
            dto.setSolicitante(solicitante);
            dto.setTotalAgendamentos(totalAgendamentos);
            dto.setQuantidadeVagas(quantidadeVagas);
            dto.setPercentual(percentual);

            relatorio.add(dto);
        }

        return relatorio;
    }
}
