package br.com.fiap.zelo.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import br.com.fiap.zelo.model.Morador;

@Component
public class MoradorMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public MoradorMapper() {
        // STRICT: o campo idBairro do request não é copiado para o id do morador
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public Morador toModel(MoradorRequest dto) {
        return modelMapper.map(dto, Morador.class);
    }

    public MoradorResponse toDto(Morador entity) {
        MoradorResponse dto = modelMapper.map(entity, MoradorResponse.class);
        dto.setIdBairro(entity.getBairro().getId());
        dto.setNomeBairro(entity.getBairro().getNome());
        return dto;
    }

    public Morador toModel(Long id, MoradorRequest dto, Morador atual) {
        Morador morador = modelMapper.map(dto, Morador.class);
        morador.setId(id);
        morador.setDataCadastro(atual.getDataCadastro());
        return morador;
    }
}
