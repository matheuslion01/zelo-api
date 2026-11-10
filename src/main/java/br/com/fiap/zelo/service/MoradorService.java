package br.com.fiap.zelo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fiap.zelo.model.Morador;
import br.com.fiap.zelo.repository.MoradorRepository;

@Service
public class MoradorService {
    @Autowired
    private MoradorRepository repository;

    public Morador create(Morador morador) {
        morador.setDataCadastro(LocalDateTime.now());
        return save(morador);
    }

    public Morador update(Morador morador) {
        return save(morador);
    }

    public Optional<Morador> findById(Long id) {
        return repository.findById(id);
    }

    public List<Morador> findAll() {
        return repository.findAll();
    }

    /**
     * Exclui o morador. Retorna false se ele tiver ocorrências ou confirmações
     * registradas (o banco não permite excluir).
     */
    public boolean deleteById(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    private Morador save(Morador morador) {
        // Se não informado, o morador não é anônimo
        if (morador.getAnonimo() == null) {
            morador.setAnonimo(false);
        }
        return repository.save(morador);
    }
}
