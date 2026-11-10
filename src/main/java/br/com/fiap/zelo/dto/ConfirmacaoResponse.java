package br.com.fiap.zelo.dto;

import java.time.LocalDateTime;

public class ConfirmacaoResponse {

    private Long id;

    private Long idOcorrencia;

    private String tituloOcorrencia;

    private Long idMorador;

    private String nomeMorador;

    private String comentario;

    private Boolean problemaPersiste;

    private LocalDateTime dataConfirmacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOcorrencia() {
        return idOcorrencia;
    }

    public void setIdOcorrencia(Long idOcorrencia) {
        this.idOcorrencia = idOcorrencia;
    }

    public String getTituloOcorrencia() {
        return tituloOcorrencia;
    }

    public void setTituloOcorrencia(String tituloOcorrencia) {
        this.tituloOcorrencia = tituloOcorrencia;
    }

    public Long getIdMorador() {
        return idMorador;
    }

    public void setIdMorador(Long idMorador) {
        this.idMorador = idMorador;
    }

    public String getNomeMorador() {
        return nomeMorador;
    }

    public void setNomeMorador(String nomeMorador) {
        this.nomeMorador = nomeMorador;
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

    public LocalDateTime getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }
}
