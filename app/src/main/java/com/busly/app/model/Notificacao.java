package com.busly.app.model;

/** Alerta exibido na central de notificações. */
public class Notificacao {

    public enum Tipo {
        PROXIMIDADE,
        EMBARQUE,
        DESEMBARQUE,
        ATRASO,
        AVISO
    }

    private final Tipo tipo;
    private final String titulo;
    private final String mensagem;
    private final String horario;
    private boolean naoLida;

    public Notificacao(Tipo tipo, String titulo, String mensagem, String horario, boolean naoLida) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.horario = horario;
        this.naoLida = naoLida;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getHorario() {
        return horario;
    }

    public boolean isNaoLida() {
        return naoLida;
    }

    public void setNaoLida(boolean naoLida) {
        this.naoLida = naoLida;
    }
}
