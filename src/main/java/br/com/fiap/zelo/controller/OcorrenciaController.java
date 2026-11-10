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

import br.com.fiap.zelo.dto.OcorrenciaCreateRequest;
import br.com.fiap.zelo.dto.OcorrenciaMapper;
import br.com.fiap.zelo.dto.OcorrenciaResponse;
import br.com.fiap.zelo.dto.OcorrenciaUpdateRequest;
import br.com.fiap.zelo.model.Bairro;
import br.com.fiap.zelo.model.Morador;
import br.com.fiap.zelo.model.Ocorrencia;
import br.com.fiap.zelo.model.StatusOcorrencia;
import br.com.fiap.zelo.service.BairroService;
import br.com.fiap.zelo.service.MoradorService;
import br.com.fiap.zelo.service.OcorrenciaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/${api.version}/ocorrencias")
public class OcorrenciaController {

    @Autowired
    private OcorrenciaService service;

    @Autowired
    private MoradorService moradorService;

    @Autowired
    private BairroService bairroService;

    @Autowired
    private OcorrenciaMapper mapper;

    @PostMapping
    public ResponseEntity<OcorrenciaResponse> create(@Valid @RequestBody OcorrenciaCreateRequest dtoRequest) {
        Optional<Morador> optMorador = moradorService.findById(dtoRequest.getIdMorador());
        Optional<Bairro> optBairro = bairroService.findById(dtoRequest.getIdBairro());

        if (optMorador.isEmpty() || optBairro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ocorrencia ocorrencia = mapper.toModel(dtoRequest);
        ocorrencia.setMorador(optMorador.get());
        ocorrencia.setBairro(optBairro.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(ocorrencia)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaResponse> findById(@PathVariable Long id) {
        return service
                .findById(id)
                .map(ocorrencia -> mapper.toDto(ocorrencia))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OcorrenciaResponse>> findAll() {
        return ResponseEntity.ok(
                service.findAll().stream()
                .map(ocorrencia -> mapper.toDto(ocorrencia))
                .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaResponse> update(@PathVariable Long id,
                                @Valid @RequestBody OcorrenciaUpdateRequest dtoRequest) {

        Optional<Ocorrencia> optOcorrencia = service.findById(id);
        if (optOcorrencia.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 400: nota só pode ser dada quando o status é RESOLVIDA
        if (!service.avaliacaoValida(dtoRequest.getStatus(), dtoRequest.getNotaAvaliacao())) {
            return ResponseEntity.badRequest().build();
        }

        Ocorrencia ocorrencia = optOcorrencia.get();
        StatusOcorrencia statusAnterior = ocorrencia.getStatus();
        mapper.updateModel(dtoRequest, ocorrencia);
        return ResponseEntity.ok(mapper.toDto(service.update(ocorrencia, statusAnterior)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        // 409: a ocorrência ainda tem confirmações
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
