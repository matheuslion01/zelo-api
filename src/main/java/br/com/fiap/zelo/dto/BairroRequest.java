package br.com.fiap.zelo.dto;

import br.com.fiap.zelo.model.Zona;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class BairroRequest {

    @NotBlank(message = "O nome do bairro é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @NotNull(message = "A zona é obrigatória (CENTRO, NORTE, SUL, LESTE ou OESTE)")
    private Zona zona;

    @NotNull(message = "Informe se o bairro é periférico")
    private Boolean periferico;

    @PositiveOrZero(message = "A população estimada não pode ser negativa")
    private Integer populacaoEstimada;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Boolean getPeriferico() {
        return periferico;
    }

    public void setPeriferico(Boolean periferico) {
        this.periferico = periferico;
    }

    public Integer getPopulacaoEstimada() {
        return populacaoEstimada;
    }

    public void setPopulacaoEstimada(Integer populacaoEstimada) {
        this.populacaoEstimada = populacaoEstimada;
    }
}
