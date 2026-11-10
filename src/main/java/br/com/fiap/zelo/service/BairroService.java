package br.com.fiap.zelo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fiap.zelo.model.Bairro;
import br.com.fiap.zelo.repository.BairroRepository;

@Service
public class BairroService {
    @Autowired
    private BairroRepository repository;

    public Bairro create(Bairro bairro) {
        bairro.setDataCadastro(LocalDateTime.now());
        return repository.save(bairro);
    }

    public Bairro update(Bairro bairro) {
        return repository.save(bairro);
    }

    public Optional<Bairro> findById(Long id) {
        return repository.findById(id);
    }

    public List<Bairro> findAll() {
        return repository.findAll();
    }

    /**
     * Exclui o bairro. Retorna false se ele ainda tiver moradores ou ocorrências
     * vinculados (o banco não permite excluir).
     */
    public boolean deleteById(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }
}
