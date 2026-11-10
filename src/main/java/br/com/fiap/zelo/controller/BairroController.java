package br.com.fiap.zelo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.zelo.dto.BairroMapper;
import br.com.fiap.zelo.dto.BairroRequest;
import br.com.fiap.zelo.dto.BairroResponse;
import br.com.fiap.zelo.model.Bairro;
import br.com.fiap.zelo.service.BairroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/${api.version}/bairros")
public class BairroController {

    @Autowired
    private BairroService service;

    @Autowired
    private BairroMapper mapper;

    @PostMapping
    public ResponseEntity<BairroResponse> create(@Valid @RequestBody BairroRequest dtoRequest) {
        Bairro bairro = service.create(mapper.toModel(dtoRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(bairro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BairroResponse> findById(@PathVariable Long id) {
        return service
                .findById(id)
                .map(bairro -> mapper.toDto(bairro))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BairroResponse>> findAll() {
        return ResponseEntity.ok(
                service.findAll().stream()
                .map(bairro -> mapper.toDto(bairro))
                .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BairroResponse> update(@PathVariable Long id,
                                @Valid @RequestBody BairroRequest dtoRequest) {

        Optional<Bairro> optBairro = service.findById(id);

        if (optBairro.isPresent()) {
            Bairro bairro = mapper.toModel(id, dtoRequest, optBairro.get());
            return ResponseEntity.ok(mapper.toDto(service.update(bairro)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        // 409: o bairro ainda tem moradores ou ocorrências
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
