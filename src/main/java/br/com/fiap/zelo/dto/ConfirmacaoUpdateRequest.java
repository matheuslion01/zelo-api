package br.com.fiap.zelo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ConfirmacaoUpdateRequest {

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    private String comentario;

    @NotNull(message = "Informe se o problema ainda persiste")
    private Boolean problemaPersiste;

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
