package br.com.fiap.zelo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fiap.zelo.model.CategoriaOcorrencia;
import br.com.fiap.zelo.model.Ocorrencia;
import br.com.fiap.zelo.model.StatusOcorrencia;
import br.com.fiap.zelo.repository.OcorrenciaRepository;

@Service
public class OcorrenciaService {
    @Autowired
    private OcorrenciaRepository repository;

    /** Registra a ocorrência com status inicial REGISTRADA e calcula a prioridade. */
    public Ocorrencia create(Ocorrencia ocorrencia) {
        ocorrencia.setStatus(StatusOcorrencia.REGISTRADA);
        ocorrencia.setTotalConfirmacoes(0);
        ocorrencia.setNotaAvaliacao(null);
        ocorrencia.setDataRegistro(LocalDateTime.now());
        if (ocorrencia.getProximoServicoEssencial() == null) {
            ocorrencia.setProximoServicoEssencial(false);
        }
        ocorrencia.setPrioridade(calcularPrioridade(ocorrencia));
        return repository.save(ocorrencia);
    }

    /**
     * Atualiza a ocorrência. Ao mudar para RESOLVIDA, registra a data de resolução;
     * se for reaberta, a data é apagada e a prioridade recalculada.
     */
    public Ocorrencia update(Ocorrencia ocorrencia, StatusOcorrencia statusAnterior) {
        if (ocorrencia.getProximoServicoEssencial() == null) {
            ocorrencia.setProximoServicoEssencial(false);
        }
        if (ocorrencia.getStatus() == StatusOcorrencia.RESOLVIDA) {
            if (statusAnterior != StatusOcorrencia.RESOLVIDA) {
                ocorrencia.setDataResolucao(LocalDateTime.now());
            }
        } else {
            ocorrencia.setDataResolucao(null);
            ocorrencia.setPrioridade(calcularPrioridade(ocorrencia));
        }
        return repository.save(ocorrencia);
    }

    /** A nota (1 a 5) só pode ser dada quando a ocorrência está RESOLVIDA. */
    public boolean avaliacaoValida(StatusOcorrencia status, Integer notaAvaliacao) {
        return notaAvaliacao == null || status == StatusOcorrencia.RESOLVIDA;
    }

    /** Soma (ou subtrai) confirmações e recalcula a prioridade. */
    public void atualizarConfirmacoes(Ocorrencia ocorrencia, int quantidade) {
        int total = ocorrencia.getTotalConfirmacoes() + quantidade;
        if (total < 0) {
            total = 0;
        }
        ocorrencia.setTotalConfirmacoes(total);
        if (ocorrencia.getStatus() != StatusOcorrencia.RESOLVIDA) {
            ocorrencia.setPrioridade(calcularPrioridade(ocorrencia));
        }
        repository.save(ocorrencia);
    }

    /**
     * Prioridade de 0 a 100:
     * gravidade da categoria + 5 por confirmação (máx. 40)
     * + 15 se perto de escola, hospital ou ponto de ônibus
     * + 10 se o bairro for periférico.
     */
    public int calcularPrioridade(Ocorrencia ocorrencia) {
        int pontos = gravidade(ocorrencia.getCategoria());

        int pontosConfirmacoes = ocorrencia.getTotalConfirmacoes() * 5;
        if (pontosConfirmacoes > 40) {
            pontosConfirmacoes = 40;
        }
        pontos = pontos + pontosConfirmacoes;

        if (Boolean.TRUE.equals(ocorrencia.getProximoServicoEssencial())) {
            pontos = pontos + 15;
        }
        if (Boolean.TRUE.equals(ocorrencia.getBairro().getPeriferico())) {
            pontos = pontos + 10;
        }

        if (pontos > 100) {
            pontos = 100;
        }
        return pontos;
    }

    private int gravidade(CategoriaOcorrencia categoria) {
        switch (categoria) {
            case ALAGAMENTO:
                return 35;
            case BURACO:
            case ARVORE_EM_RISCO:
                return 30;
            case ILUMINACAO_APAGADA:
            case CALCADA_QUEBRADA:
                return 25;
            case LIXO_ACUMULADO:
            case SINALIZACAO_DANIFICADA:
                return 20;
            default:
                return 10;
        }
    }

    public Optional<Ocorrencia> findById(Long id) {
        return repository.findById(id);
    }

    public List<Ocorrencia> findAll() {
        return repository.findAll();
    }

    /** Retorna false se a ocorrência ainda tiver confirmações (o banco não permite excluir). */
    public boolean deleteById(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }
}
