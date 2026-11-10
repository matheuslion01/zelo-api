package br.com.fiap.zelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.zelo.model.Bairro;

@Repository
public interface BairroRepository extends JpaRepository<Bairro, Long> {

}
