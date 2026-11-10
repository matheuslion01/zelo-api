package br.com.fiap.zelo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.zelo.model.Confirmacao;
import br.com.fiap.zelo.model.Morador;
import br.com.fiap.zelo.model.Ocorrencia;
import br.com.fiap.zelo.model.StatusOcorrencia;
import br.com.fiap.zelo.repository.ConfirmacaoRepository;

@Service
public class ConfirmacaoService {
    @Autowired
    private ConfirmacaoRepository repository;

    @Autowired
    private OcorrenciaService ocorrenciaService;

    /**
     * Regras para confirmar: a ocorrência não pode estar resolvida
     * e o autor não pode confirmar a própria ocorrência.
     */
    public boolean podeConfirmar(Ocorrencia ocorrencia, Morador morador) {
        boolean resolvida = ocorrencia.getStatus() == StatusOcorrencia.RESOLVIDA;
        boolean autor = ocorrencia.getMorador().getId().equals(morador.getId());
        return !resolvida && !autor;
    }

    /** Salva a confirmação e aumenta a prioridade da ocorrência. */
    public Confirmacao create(Confirmacao confirmacao) {
        confirmacao.setDataConfirmacao(LocalDateTime.now());
        if (confirmacao.getProblemaPersiste() == null) {
            confirmacao.setProblemaPersiste(true);
        }
        Confirmacao salva = repository.save(confirmacao);
        ocorrenciaService.atualizarConfirmacoes(confirmacao.getOcorrencia(), 1);
        return salva;
    }

    public Confirmacao update(Confirmacao confirmacao) {
        return repository.save(confirmacao);
    }

    public Optional<Confirmacao> findById(Long id) {
        return repository.findById(id);
    }

    public List<Confirmacao> findAll() {
        return repository.findAll();
    }

    /** Exclui a confirmação e diminui a prioridade da ocorrência. */
    public void delete(Confirmacao confirmacao) {
        repository.delete(confirmacao);
        ocorrenciaService.atualizarConfirmacoes(confirmacao.getOcorrencia(), -1);
    }
}
