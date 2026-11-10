package br.com.fiap.zelo.dto;

import br.com.fiap.zelo.model.CategoriaOcorrencia;
import br.com.fiap.zelo.model.StatusOcorrencia;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OcorrenciaUpdateRequest {

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 5, max = 120, message = "O título deve ter entre 5 e 120 caracteres")
    private String titulo;

    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    private String descricao;

    @NotNull(message = "A categoria é obrigatória")
    private CategoriaOcorrencia categoria;

    @NotNull(message = "A latitude é obrigatória")
    private Double latitude;

    @NotNull(message = "A longitude é obrigatória")
    private Double longitude;

    @Size(max = 500, message = "A URL da foto deve ter no máximo 500 caracteres")
    private String urlFoto;

    private Boolean proximoServicoEssencial;

    @NotNull(message = "O status é obrigatório (REGISTRADA, EM_ANALISE, EM_EXECUCAO ou RESOLVIDA)")
    private StatusOcorrencia status;

    @Min(value = 1, message = "A nota deve ser de 1 a 5")
    @Max(value = 5, message = "A nota deve ser de 1 a 5")
    private Integer notaAvaliacao;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriaOcorrencia getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaOcorrencia categoria) {
        this.categoria = categoria;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Boolean getProximoServicoEssencial() {
        return proximoServicoEssencial;
    }

    public void setProximoServicoEssencial(Boolean proximoServicoEssencial) {
        this.proximoServicoEssencial = proximoServicoEssencial;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public void setStatus(StatusOcorrencia status) {
        this.status = status;
    }

    public Integer getNotaAvaliacao() {
        return notaAvaliacao;
    }

    public void setNotaAvaliacao(Integer notaAvaliacao) {
        this.notaAvaliacao = notaAvaliacao;
    }
}
