package br.com.fiap.zelo.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import br.com.fiap.zelo.model.Confirmacao;

@Component
public class ConfirmacaoMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public ConfirmacaoMapper() {
        // STRICT: os campos idOcorrencia e idMorador do request não são copiados para o id da confirmação
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public Confirmacao toModel(ConfirmacaoCreateRequest dto) {
        return modelMapper.map(dto, Confirmacao.class);
    }

    public ConfirmacaoResponse toDto(Confirmacao entity) {
        ConfirmacaoResponse dto = modelMapper.map(entity, ConfirmacaoResponse.class);
        dto.setIdOcorrencia(entity.getOcorrencia().getId());
        dto.setTituloOcorrencia(entity.getOcorrencia().getTitulo());
        dto.setIdMorador(entity.getMorador().getId());
        // LGPD: morador anônimo não tem o nome exibido
        if (Boolean.TRUE.equals(entity.getMorador().getAnonimo())) {
            dto.setNomeMorador("Anônimo");
        } else {
            dto.setNomeMorador(entity.getMorador().getNome());
        }
        return dto;
    }

    /** Copia comentário e "problemaPersiste" para a confirmação já existente. */
    public void updateModel(ConfirmacaoUpdateRequest dto, Confirmacao confirmacao) {
        modelMapper.map(dto, confirmacao);
    }
}
