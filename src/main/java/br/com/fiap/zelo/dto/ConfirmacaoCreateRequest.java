package br.com.fiap.zelo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ConfirmacaoCreateRequest {

    @NotNull(message = "O id da ocorrência é obrigatório")
    private Long idOcorrencia;

    @NotNull(message = "O id do morador é obrigatório")
    private Long idMorador;

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    private String comentario;

    private Boolean problemaPersiste;

    public Long getIdOcorrencia() {
        return idOcorrencia;
    }

    public void setIdOcorrencia(Long idOcorrencia) {
        this.idOcorrencia = idOcorrencia;
    }

    public Long getIdMorador() {
        return idMorador;
    }

    public void setIdMorador(Long idMorador) {
        this.idMorador = idMorador;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Boolean getProblemaPersiste() {
        return problemaPersiste;
    }

    public void setProblemaPersiste(Boolean problemaPersiste) {
        this.problemaPersiste = problemaPersiste;
    }
}
