package br.com.fiap.zelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.zelo.model.Confirmacao;

@Repository
public interface ConfirmacaoRepository extends JpaRepository<Confirmacao, Long> {

}
