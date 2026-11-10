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

import br.com.fiap.zelo.dto.ConfirmacaoCreateRequest;
import br.com.fiap.zelo.dto.ConfirmacaoMapper;
import br.com.fiap.zelo.dto.ConfirmacaoResponse;
import br.com.fiap.zelo.dto.ConfirmacaoUpdateRequest;
import br.com.fiap.zelo.model.Confirmacao;
import br.com.fiap.zelo.model.Morador;
import br.com.fiap.zelo.model.Ocorrencia;
import br.com.fiap.zelo.service.ConfirmacaoService;
import br.com.fiap.zelo.service.MoradorService;
import br.com.fiap.zelo.service.OcorrenciaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/${api.version}/confirmacoes")
public class ConfirmacaoController {

    @Autowired
    private ConfirmacaoService service;

    @Autowired
    private OcorrenciaService ocorrenciaService;

    @Autowired
    private MoradorService moradorService;

    @Autowired
    private ConfirmacaoMapper mapper;

    @PostMapping
    public ResponseEntity<ConfirmacaoResponse> create(@Valid @RequestBody ConfirmacaoCreateRequest dtoRequest) {
        Optional<Ocorrencia> optOcorrencia = ocorrenciaService.findById(dtoRequest.getIdOcorrencia());
        Optional<Morador> optMorador = moradorService.findById(dtoRequest.getIdMorador());

        if (optOcorrencia.isEmpty() || optMorador.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 400: ocorrência já resolvida ou autor confirmando a própria ocorrência
        if (!service.podeConfirmar(optOcorrencia.get(), optMorador.get())) {
            return ResponseEntity.badRequest().build();
        }

        Confirmacao confirmacao = mapper.toModel(dtoRequest);
        confirmacao.setOcorrencia(optOcorrencia.get());
        confirmacao.setMorador(optMorador.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(confirmacao)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfirmacaoResponse> findById(@PathVariable Long id) {
        return service
                .findById(id)
                .map(confirmacao -> mapper.toDto(confirmacao))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ConfirmacaoResponse>> findAll() {
        return ResponseEntity.ok(
                service.findAll().stream()
                .map(confirmacao -> mapper.toDto(confirmacao))
                .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfirmacaoResponse> update(@PathVariable Long id,
                                @Valid @RequestBody ConfirmacaoUpdateRequest dtoRequest) {

        Optional<Confirmacao> optConfirmacao = service.findById(id);

        if (optConfirmacao.isPresent()) {
            Confirmacao confirmacao = optConfirmacao.get();
            mapper.updateModel(dtoRequest, confirmacao);
            return ResponseEntity.ok(mapper.toDto(service.update(confirmacao)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        Optional<Confirmacao> optConfirmacao = service.findById(id);

        if (optConfirmacao.isPresent()) {
            service.delete(optConfirmacao.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
