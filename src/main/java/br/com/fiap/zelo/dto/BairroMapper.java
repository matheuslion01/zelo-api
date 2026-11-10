package br.com.fiap.zelo.dto;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.fiap.zelo.model.Bairro;

@Component
public class BairroMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public Bairro toModel(BairroRequest dto) {
        return modelMapper.map(dto, Bairro.class);
    }

    public BairroResponse toDto(Bairro entity) {
        return modelMapper.map(entity, BairroResponse.class);
    }

    public Bairro toModel(Long id, BairroRequest dto, Bairro atual) {
        Bairro bairro = modelMapper.map(dto, Bairro.class);
        bairro.setId(id);
        bairro.setDataCadastro(atual.getDataCadastro());
        return bairro;
    }
}
