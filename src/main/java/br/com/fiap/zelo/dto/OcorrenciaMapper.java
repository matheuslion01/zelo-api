package br.com.fiap.zelo.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import br.com.fiap.zelo.model.Ocorrencia;

@Component
public class OcorrenciaMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public OcorrenciaMapper() {
        // STRICT: os campos idMorador e idBairro do request não são copiados para o id da ocorrência
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public Ocorrencia toModel(OcorrenciaCreateRequest dto) {
        return modelMapper.map(dto, Ocorrencia.class);
    }

    public OcorrenciaResponse toDto(Ocorrencia entity) {
        OcorrenciaResponse dto = modelMapper.map(entity, OcorrenciaResponse.class);
        dto.setIdMorador(entity.getMorador().getId());
        dto.setIdBairro(entity.getBairro().getId());
        dto.setNomeBairro(entity.getBairro().getNome());
        // LGPD: morador anônimo não tem o nome exibido
        if (Boolean.TRUE.equals(entity.getMorador().getAnonimo())) {
            dto.setAutor("Anônimo");
        } else {
            dto.setAutor(entity.getMorador().getNome());
        }
        return dto;
    }

    /** Copia os dados do request para a ocorrência já existente (mantém autor, bairro e datas). */
    public void updateModel(OcorrenciaUpdateRequest dto, Ocorrencia ocorrencia) {
        modelMapper.map(dto, ocorrencia);
    }
}
