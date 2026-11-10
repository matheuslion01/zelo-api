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

import br.com.fiap.zelo.dto.MoradorMapper;
import br.com.fiap.zelo.dto.MoradorRequest;
import br.com.fiap.zelo.dto.MoradorResponse;
import br.com.fiap.zelo.model.Bairro;
import br.com.fiap.zelo.model.Morador;
import br.com.fiap.zelo.service.BairroService;
import br.com.fiap.zelo.service.MoradorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/${api.version}/moradores")
public class MoradorController {

    @Autowired
    private MoradorService service;

    @Autowired
    private BairroService bairroService;

    @Autowired
    private MoradorMapper mapper;

    @PostMapping
    public ResponseEntity<MoradorResponse> create(@Valid @RequestBody MoradorRequest dtoRequest) {
        Optional<Bairro> optBairro = bairroService.findById(dtoRequest.getIdBairro());
        if (optBairro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Morador morador = mapper.toModel(dtoRequest);
        morador.setBairro(optBairro.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(morador)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoradorResponse> findById(@PathVariable Long id) {
        return service
                .findById(id)
                .map(morador -> mapper.toDto(morador))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MoradorResponse>> findAll() {
        return ResponseEntity.ok(
                service.findAll().stream()
                .map(morador -> mapper.toDto(morador))
                .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoradorResponse> update(@PathVariable Long id,
                                @Valid @RequestBody MoradorRequest dtoRequest) {

        Optional<Morador> optMorador = service.findById(id);
        Optional<Bairro> optBairro = bairroService.findById(dtoRequest.getIdBairro());

        if (optMorador.isPresent() && optBairro.isPresent()) {
            Morador morador = mapper.toModel(id, dtoRequest, optMorador.get());
            morador.setBairro(optBairro.get());
            return ResponseEntity.ok(mapper.toDto(service.update(morador)));
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
        // 409: o morador tem ocorrências ou confirmações registradas
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
