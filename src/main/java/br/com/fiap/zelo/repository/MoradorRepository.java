package br.com.fiap.zelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.zelo.model.Morador;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Long> {

}
